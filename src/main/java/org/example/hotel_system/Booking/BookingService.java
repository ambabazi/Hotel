package org.example.hotel_system.Booking;

import lombok.RequiredArgsConstructor;
import org.example.hotel_system.Enum.BookingStatus;
import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Exceptions.ResourceNotFoundException;
import org.example.hotel_system.Exceptions.RoomNotAvailableException;
import org.example.hotel_system.Guest.Guest;
import org.example.hotel_system.Guest.GuestRepository;
import org.example.hotel_system.Room.Room;
import org.example.hotel_system.Room.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {

        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Guest not found with id: " + dto.getGuestId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room not found with id: " + dto.getRoomId()));

        if (room.getRoomStatus() == RoomStatus.UNDER_MAINTENANCE) {
            throw new RoomNotAvailableException(
                    "Room " + room.getRoomNumber() + " is currently under maintenance");
        }

//        if (dto.getNumberOfGuests() > room.getCapacity()) {
//            throw new IllegalArgumentException(
//                    "Room " + room.getRoomNumber() + " capacity is " + room.getCapacity() +
//                            " but you requested " + dto.getNumberOfGuests() + " guests");
//        }

        boolean hasOverlap = bookingRepository.existsOverlappingBooking(
                room.getId(), dto.getCheckInDate(), dto.getCheckOutDate(), null);
        if (hasOverlap) {
            throw new RoomNotAvailableException(
                    "Room " + room.getRoomNumber() + " is already booked for the selected dates");
        }

        Booking booking = bookingMapper.toEntity(dto);
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setBookingStatus(BookingStatus.PENDING);

        long nights = dto.getCheckOutDate().toEpochDay() - dto.getCheckInDate().toEpochDay();
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        booking.setTotalPrice(total);

        Booking saved = bookingRepository.save(booking);
        return toEnrichedResponse(saved);
    }

    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return toEnrichedResponse(booking);
    }

    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDto> getBookingsByGuest(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new ResourceNotFoundException("Guest not found with id: " + guestId);
        }
        return bookingRepository.findByGuestId(guestId)
                .stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDto> getBookingsByRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDto> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status)
                .stream()
                .map(this::toEnrichedResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDto updateBookingStatus(Long id, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a cancelled booking");
        }

        booking.setBookingStatus(newStatus);

        if (newStatus == BookingStatus.CHECKED_IN) {
            booking.getRoom().setRoomStatus(RoomStatus.OCCUPIED);
            roomRepository.save(booking.getRoom());
        }

        if (newStatus == BookingStatus.CHECKED_OUT || newStatus == BookingStatus.CANCELLED) {
            booking.getRoom().setRoomStatus(RoomStatus.AVAILABLE);
            roomRepository.save(booking.getRoom());
        }

        return toEnrichedResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto updateBooking(Long id, BookingRequestDto dto) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        boolean hasOverlap = bookingRepository.existsOverlappingBooking(
                existing.getRoom().getId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                id
        );
        if (hasOverlap) {
            throw new RoomNotAvailableException("Room is already booked for the selected dates");
        }

        bookingMapper.updateEntityFromDto(dto, existing);

        long nights = dto.getCheckOutDate().toEpochDay() - dto.getCheckInDate().toEpochDay();
        existing.setTotalPrice(existing.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(nights)));

        return toEnrichedResponse(bookingRepository.save(existing));
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private BookingResponseDto toEnrichedResponse(Booking booking) {
        BookingResponseDto dto = bookingMapper.toResponseDto(booking);
        dto.setGuestFullName(
                booking.getGuest().getFirstName() + " " + booking.getGuest().getLastName()
        );
        return dto;
    }
}