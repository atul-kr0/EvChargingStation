package com.ev.EvChargingStation.dto.chargingSession;

import com.ev.EvChargingStation.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSessionResponseDTO {

    private Long id;
    private Long bookingId;
    private Long chargerId;
    private LocalDateTime startTime;
    private LocalDateTime plannedEndTime;
    private LocalDateTime endTime;
    private boolean endedEarly;
    private Double energyDelivered;
    private Double totalAmount;
    private PaymentStatus paymentStatus;
}