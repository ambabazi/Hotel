package org.example.hotel_system.Booking;

import lombok.*;
import org.example.hotel_system.Enum.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private Long id;


    private Long guestId;
    private String guestFullName;

    private Long roomId;
    private String roomNumber;
    private String roomType;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long numberOfNights;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
    private Integer numberOfGuests;
    private String specialRequests;
}