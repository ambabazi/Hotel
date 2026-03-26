package org.example.hotel_system.Room;


import lombok.RequiredArgsConstructor;
import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Enum.RoomType;
import org.example.hotel_system.Exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomResponseDto createRoom(RoomRequestDto dto) {
        if (roomRepository.existsByRoomNumber(dto.getRoomNumber())) {
            throw new IllegalArgumentException("Room number already exists: " + dto.getRoomNumber());
        }

        Room room = roomMapper.toEntity(dto);

        // If no status was provided, default to AVAILABLE
        if (room.getRoomStatus() == null) {
            room.setRoomStatus(RoomStatus.AVAILABLE);
        }

        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    public RoomResponseDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return roomMapper.toResponseDto(room);
    }

    public RoomResponseDto getRoomByNumber(String roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomNumber));
        return roomMapper.toResponseDto(room);
    }

    public List<RoomResponseDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByRoomStatus(status)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> getRoomsByType(RoomType type) {
        return roomRepository.findByRoomType(type)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> getAvailableRoomsByType(RoomType type) {
        return roomRepository.findByRoomStatusAndRoomType(RoomStatus.AVAILABLE, type)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> getRoomsByPriceRange(BigDecimal min, BigDecimal max) {
        return roomRepository.findByPricePerNightBetween(min, max)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public RoomResponseDto updateRoom(Long id, RoomRequestDto dto) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        roomMapper.updateEntityFromDto(dto, existing);
        return roomMapper.toResponseDto(roomRepository.save(existing));
    }

    // A dedicated endpoint to just change a room's status
    // e.g. mark a room as UNDER_MAINTENANCE without updating everything else
    public RoomResponseDto updateRoomStatus(Long id, RoomStatus status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setRoomStatus(status);
        return roomMapper.toResponseDto(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }
}
