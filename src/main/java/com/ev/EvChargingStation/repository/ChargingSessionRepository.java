package com.ev.EvChargingStation.repository;

import com.ev.EvChargingStation.entity.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession,Long> {
}
