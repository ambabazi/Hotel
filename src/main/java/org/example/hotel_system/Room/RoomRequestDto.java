package org.example.hotel_system.Room;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Enum.RoomType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDto {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Floor is required")
    private Integer floor;

    // @Positive ensures price is never 0 or negative
    @NotNull(message = "Price per night is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal pricePerNight;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    private RoomStatus roomStatus;

    private String description;
}

