package com.ev.EvChargingStation.service.booking;


import com.ev.EvChargingStation.dto.booking.BookingResponseDTO;
import com.ev.EvChargingStation.entity.User;
import com.ev.EvChargingStation.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelBooking {

    private final UserHelper userHelper;

    public BookingResponseDTO getBooking(Long bookingId){

        User user = userHelper.getLoggedInUser();


    }
}
