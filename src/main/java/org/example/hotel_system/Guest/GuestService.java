package org.example.hotel_system.Guest;

import lombok.RequiredArgsConstructor;
import org.example.hotel_system.Exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestResponseDto createGuest(GuestRequestDto dto) {
        if (guestRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }
        Guest guest = guestMapper.toEntity(dto);
        Guest saved = guestRepository.save(guest);
        return guestMapper.toResponseDto(saved);
    }

    public GuestResponseDto getGuestById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + id));
        return guestMapper.toResponseDto(guest);
    }

    public GuestResponseDto getGuestByEmail(String email) {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with email: " + email));
        return guestMapper.toResponseDto(guest);
    }

    public List<GuestResponseDto> getAllGuests() {
        return guestRepository.findAll()
                .stream()
                .map(guestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public GuestResponseDto updateGuest(Long id, GuestRequestDto dto) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + id));
        guestMapper.updateEntityFromDto(dto, existing);
        return guestMapper.toResponseDto(guestRepository.save(existing));
    }

    public void deleteGuest(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guest not found with id: " + id);
        }
        guestRepository.deleteById(id);
    }
}