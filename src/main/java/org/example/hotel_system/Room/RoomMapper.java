package org.example.hotel_system.Room;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toEntity(RoomRequestDto dto);

    RoomResponseDto toResponseDto(Room room);

    void updateEntityFromDto(RoomRequestDto dto, @MappingTarget Room room);
}