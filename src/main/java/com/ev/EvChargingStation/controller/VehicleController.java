package com.ev.EvChargingStation.controller;

import com.ev.EvChargingStation.dto.vehicle.VehicleRequestDTO;
import com.ev.EvChargingStation.dto.vehicle.VehicleResponseDTO;
import com.ev.EvChargingStation.entity.Vehicle;
import com.ev.EvChargingStation.service.VehicleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/addVehicle")
    public VehicleResponseDTO addVehicle(@RequestBody VehicleRequestDTO request){

        return vehicleService.addVehicle(request);
    }

    @GetMapping("/getAllVehicles")
    public List<VehicleResponseDTO> getMyVehicles(){

        return vehicleService.getMyVehicles();
    }

    @GetMapping("/getVehicle/{id}")
    public VehicleResponseDTO getVehicle(@PathVariable Long id){

        return vehicleService.getVehicle(id);
    }

    @PutMapping("/update/{id}")
    public VehicleResponseDTO updateVehicle(@PathVariable Long id,@RequestBody VehicleRequestDTO request){

        return vehicleService.updateVehicle(id,request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVehicle(@PathVariable Long id){

        vehicleService.deleteVehicle(id);
    }
}
