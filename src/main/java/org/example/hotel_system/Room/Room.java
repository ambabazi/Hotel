package org.example.hotel_system.Room;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Enum.RoomType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus roomStatus;

    @Column(length = 500)
    private String description;

}