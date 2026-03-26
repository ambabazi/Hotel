package org.example.hotel_system.Guest;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest toEntity(GuestRequestDto dto);

    GuestResponseDto toResponseDto(Guest guest);

    void updateEntityFromDto(GuestRequestDto dto, @MappingTarget Guest guest);
}