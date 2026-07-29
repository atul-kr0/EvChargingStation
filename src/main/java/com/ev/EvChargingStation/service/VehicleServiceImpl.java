package com.ev.EvChargingStation.service;

import com.ev.EvChargingStation.dto.vehicle.VehicleRequestDTO;
import com.ev.EvChargingStation.dto.vehicle.VehicleResponseDTO;
import com.ev.EvChargingStation.entity.User;
import com.ev.EvChargingStation.entity.Vehicle;
import com.ev.EvChargingStation.exception.VehicleNotFoundException;
import com.ev.EvChargingStation.mapper.VehicleMapper;
import com.ev.EvChargingStation.repository.VehicleRepository;
import com.ev.EvChargingStation.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final SecurityUtil securityUtil;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponseDTO addVehicle(VehicleRequestDTO request){

        User user = securityUtil.getCurrentUser();

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setUser(user);
        Vehicle saved = vehicleRepository.save(vehicle);

        return vehicleMapper.entityToDto(saved);
    }

    @Override
    public List<VehicleResponseDTO> getMyVehicles() {

        User user = securityUtil.getCurrentUser();

        List<Vehicle> vehicles = vehicleRepository.findByUserId(user.getId());   // ← fresh query, not user.getVehicles()

        return vehicles.stream()
                .map(vehicleMapper::entityToDto)
                .toList();
    }

    @Override
    public VehicleResponseDTO getVehicle(Long id){
        User user = securityUtil.getCurrentUser();

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        if (!vehicle.getUser().getId().equals(user.getId())) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + id);
        }
        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicle(Long id,VehicleRequestDTO request){
        User user = securityUtil.getCurrentUser();

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        if (!vehicle.getUser().getId().equals(user.getId())) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleMapper.updateEntityFromDto(request,vehicle);

        vehicleRepository.save(vehicle);

        return vehicleMapper.entityToDto(vehicle);

    }

    @Override
    public void deleteVehicle(Long id){
        User user = securityUtil.getCurrentUser();

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        if (!vehicle.getUser().getId().equals(user.getId())) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

}
