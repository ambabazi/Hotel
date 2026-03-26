package org.example.hotel_system.Guest;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.hotel_system.Enum.Gender;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Length
    @NotBlank
    @Column(name = "phone_number", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "national_id", unique = true)
    private String nationalId;


}