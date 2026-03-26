package org.example.hotel_system.Guest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.hotel_system.Enum.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationalId;
}
