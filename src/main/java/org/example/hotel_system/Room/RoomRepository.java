package org.example.hotel_system.Room;

import org.example.hotel_system.Enum.RoomStatus;
import org.example.hotel_system.Enum.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByRoomStatus(RoomStatus roomStatus);

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByRoomStatusAndRoomType(RoomStatus roomStatus, RoomType roomType);

    List<Room> findByPricePerNightBetween(BigDecimal min, BigDecimal max);

    boolean existsByRoomNumber(String roomNumber);
}