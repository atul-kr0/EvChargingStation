package com.ev.EvChargingStation.mapper;

import com.ev.EvChargingStation.dto.booking.BookingRequestDTO;
import com.ev.EvChargingStation.dto.booking.BookingResponseDTO;
import com.ev.EvChargingStation.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toEntity(BookingRequestDTO request);

    BookingResponseDTO entityToDto(Booking booking);
}
