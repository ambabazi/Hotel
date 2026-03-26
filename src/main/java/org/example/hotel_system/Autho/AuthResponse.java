package org.example.hotel_system.Autho;

import lombok.*;
import org.example.hotel_system.Enum.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private Role role;
}