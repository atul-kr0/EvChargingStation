package com.ev.EvChargingStation.entity;

import com.ev.EvChargingStation.enums.ChargingType;
import com.ev.EvChargingStation.enums.ConnectorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String registrationNumber;

    private String manufacturer;

    private String model;

    private Double batteryCapacity; // kWh

    @Enumerated(EnumType.STRING)
    private ConnectorType connectorType;

    @Enumerated(EnumType.STRING)
    private ChargingType chargingType;

    private Double maxChargingPower; // kW


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> bookings = new ArrayList<>();

}
