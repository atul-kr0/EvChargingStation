package com.ev.EvChargingStation.helper;

import com.ev.EvChargingStation.entity.User;
import com.ev.EvChargingStation.enums.BookingStatus;
import com.ev.EvChargingStation.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingValidationHelper {

    private final BookingRepository bookingRepository;
    private final UserHelper userHelper;

    public void validateNoActiveBooking(User user) {

        if (bookingRepository.existsByUserAndStatusIn(
                user,
                List.of(
                        BookingStatus.WAITING,
                        BookingStatus.CHARGING
                )
        )) {

            throw new IllegalStateException(
                    "You already have an active booking."
            );
        }
    }
}