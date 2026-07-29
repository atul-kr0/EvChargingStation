package com.ev.EvChargingStation.repository;

import com.ev.EvChargingStation.entity.Booking;
import com.ev.EvChargingStation.entity.Charger;
import com.ev.EvChargingStation.entity.User;
import com.ev.EvChargingStation.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByChargerAndStatusInOrderByQueuePositionAsc(
            Charger charger,
            List<BookingStatus> statuses
    );

    Integer countByChargerAndStatusIn(
            Charger charger,
            List<BookingStatus> statuses
    );

    boolean existsByTokenNumber(String tokenNumber);

    boolean existsByUserAndStatusIn(
            User user,
            List<BookingStatus> statuses
    );
}
