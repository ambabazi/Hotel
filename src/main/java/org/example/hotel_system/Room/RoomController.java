package org.example.hotel_system.Room;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'GUEST')")
    public List<Room> getAllRooms() {
        return List.of();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can add rooms
    public Room createRoom(@RequestBody RoomRequestDto req) { return new Room();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoom(@PathVariable Long id) { }
}