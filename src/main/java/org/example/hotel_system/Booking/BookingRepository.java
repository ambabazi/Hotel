package org.example.hotel_system.Booking;

import org.example.hotel_system.Enum.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByBookingStatus(BookingStatus status);

    @Query("""
    SELECT COUNT(b) > 0 FROM Booking b
    WHERE b.room.id = :roomId
    AND b.checkInDate < :checkOut
    AND b.checkOutDate > :checkIn
    AND (:bookingId IS NULL OR b.id != :bookingId)
""")
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("bookingId") Long bookingId
    );
}