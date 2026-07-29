package com.ev.EvChargingStation.entity;

import com.ev.EvChargingStation.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_id", nullable = false)
    private Charger charger;

    // ---- Estimation (used before/during charging to predict duration) ----
    private Double currentBattery;   // % at start
    private Double targetBattery;    // % user wants to reach
    private Double energyRequired;   // kWh needed to go from current -> target

    // ---- Actual session lifecycle ----
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime plannedEndTime;   // estimated finish, based on energyRequired
    private boolean endedEarly;

    // ---- Billing (simulated, since there's no real IoT meter) ----
    private Double energyDelivered;   // simulated = actualMinutes * charger.outputPower / 60
    private Double pricePerKwh;
    private Double penaltyAmount;
    private Double totalAmount;


    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}