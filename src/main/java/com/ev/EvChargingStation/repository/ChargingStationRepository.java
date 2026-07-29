package com.ev.EvChargingStation.repository;

import com.ev.EvChargingStation.entity.Charger;
import com.ev.EvChargingStation.entity.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  ChargingStationRepository extends JpaRepository<ChargingStation,Long> {

}
