package org.example.hotel_system.Guest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController               // @Controller + @ResponseBody: returns JSON automatically
@RequestMapping("/api/guests") // All endpoints in this class start with /api/guests
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;


    @PostMapping
    public ResponseEntity<GuestResponseDto> createGuest(@Valid @RequestBody GuestRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.createGuest(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponseDto> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuestResponseDto> getGuestByEmail(@PathVariable String email) {
        return ResponseEntity.ok(guestService.getGuestByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<GuestResponseDto>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    // PUT /api/guests/{id}  → update a guest
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponseDto> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody GuestRequestDto dto) {
        return ResponseEntity.ok(guestService.updateGuest(id, dto));
    }

    // DELETE /api/guests/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
