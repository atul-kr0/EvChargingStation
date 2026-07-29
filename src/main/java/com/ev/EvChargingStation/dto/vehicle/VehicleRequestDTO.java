package com.ev.EvChargingStation.dto.vehicle;

import com.ev.EvChargingStation.enums.ChargingType;
import com.ev.EvChargingStation.enums.ConnectorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDTO {

    private String manufacturer;
    private String model;
    private String registrationNumber;
    private Double batteryCapacity;
    private ConnectorType connectorType;
    private ChargingType chargingType;
    private Double maxChargingPower;
}
