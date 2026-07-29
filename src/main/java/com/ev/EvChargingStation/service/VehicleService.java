package com.ev.EvChargingStation.service;

import com.ev.EvChargingStation.dto.vehicle.VehicleRequestDTO;
import com.ev.EvChargingStation.dto.vehicle.VehicleResponseDTO;
import com.ev.EvChargingStation.entity.Vehicle;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface VehicleService {

    public VehicleResponseDTO addVehicle(VehicleRequestDTO request);
    public List<VehicleResponseDTO> getMyVehicles();
    public VehicleResponseDTO getVehicle(Long id);
    public VehicleResponseDTO updateVehicle(Long id,VehicleRequestDTO request);
    public void deleteVehicle(Long id);


    }
