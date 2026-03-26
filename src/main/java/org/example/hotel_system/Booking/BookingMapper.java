package org.example.hotel_system.Booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Booking toEntity(BookingRequestDto dto);

    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(source = "guest.firstName", target = "guestFullName")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "room.roomType", target = "roomType")
    @Mapping(target = "numberOfNights", expression = "java(booking.getNumberOfNights())")
    BookingResponseDto toResponseDto(Booking booking);

    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    void updateEntityFromDto(BookingRequestDto dto, @MappingTarget Booking booking);
}