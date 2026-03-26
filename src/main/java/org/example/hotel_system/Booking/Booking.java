package org.example.hotel_system.Booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.hotel_system.Enum.BookingStatus;
import org.example.hotel_system.Guest.Guest;
import org.example.hotel_system.Room.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "guest_id", nullable = false)
        private Guest guest;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_id", nullable = false)
        private Room room;

        @NotNull(message = "Check-in date is required")
        @Column(name = "check_in_date", nullable = false)
        private LocalDate checkInDate;

        @NotNull(message = "Check-out date is required")
        @Column(name = "check_out_date", nullable = false)
        private LocalDate checkOutDate;

        @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
        private BigDecimal totalPrice;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private BookingStatus bookingStatus;

        @Column(name = "number_of_guests")
        private Integer numberOfGuests;

        @Column(length = 500)
        private String specialRequests;

        public long getNumberOfNights() {
                return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
}