package com.ev.EvChargingStation.service.booking;

import com.ev.EvChargingStation.dto.booking.BookingRequestDTO;
import com.ev.EvChargingStation.dto.booking.BookingResponseDTO;

public interface BookingService {

    public BookingResponseDTO bookCharger(BookingRequestDTO request);

//
//
//    public void cancelBooking(Long bookingId);

}
