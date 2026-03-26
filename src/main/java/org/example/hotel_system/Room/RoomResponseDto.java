package org.example.hotel_system.Room;

import lombok.*;
import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Enum.RoomType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {

    private Long id;
    private String roomNumber;
    private Integer floor;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private RoomType roomType;
    private RoomStatus roomStatus;
    private String description;
}