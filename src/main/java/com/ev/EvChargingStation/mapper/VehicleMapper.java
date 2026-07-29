package com.ev.EvChargingStation.mapper;

import com.ev.EvChargingStation.dto.vehicle.VehicleRequestDTO;
import com.ev.EvChargingStation.dto.vehicle.VehicleResponseDTO;
import com.ev.EvChargingStation.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toEntity(VehicleRequestDTO requestDTO);

    VehicleResponseDTO entityToDto(Vehicle vehicle);

    void updateEntityFromDto(VehicleRequestDTO requestDTO, @MappingTarget Vehicle vehicle);
}
