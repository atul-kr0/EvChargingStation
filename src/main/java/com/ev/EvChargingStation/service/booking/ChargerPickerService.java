package com.ev.EvChargingStation.service.booking;
import com.ev.EvChargingStation.dto.booking.ChargerSelectionResult;
import com.ev.EvChargingStation.entity.Booking;
import com.ev.EvChargingStation.entity.Charger;
import com.ev.EvChargingStation.entity.ChargingStation;
import com.ev.EvChargingStation.entity.Vehicle;
import com.ev.EvChargingStation.enums.BookingStatus;
import com.ev.EvChargingStation.enums.ChargerStatus;
import com.ev.EvChargingStation.repository.BookingRepository;
import com.ev.EvChargingStation.repository.ChargerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChargerPickerService {

    private final BookingRepository bookingRepository;
    private final ChargerRepository chargerRepository;

    public Integer calculateEstimatedChargingDuration(
            Vehicle vehicle,
            Charger charger,
            Integer currentBatteryPercentage,
            Integer targetBatteryPercentage
    ) {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        if (charger == null) {
            throw new IllegalArgumentException("Charger cannot be null.");
        }

        if (currentBatteryPercentage == null || targetBatteryPercentage == null) {
            throw new IllegalArgumentException("Battery percentages cannot be null.");
        }

        if (currentBatteryPercentage < 0 || currentBatteryPercentage > 100) {
            throw new IllegalArgumentException("Current battery percentage must be between 0 and 100.");
        }

        if (targetBatteryPercentage < 0 || targetBatteryPercentage > 100) {
            throw new IllegalArgumentException("Target battery percentage must be between 0 and 100.");
        }

        if (currentBatteryPercentage >= targetBatteryPercentage) {
            throw new IllegalArgumentException(
                    "Target battery percentage must be greater than current battery percentage.");
        }

        if (vehicle.getBatteryCapacity() == null || vehicle.getBatteryCapacity() <= 0) {
            throw new IllegalArgumentException("Invalid vehicle battery capacity.");
        }

        if (charger.getOutputPower() == null || charger.getOutputPower() <= 0) {
            throw new IllegalArgumentException("Invalid charger output power.");
        }

        // The real charging speed is capped by whichever is SLOWER — the charger's
        // max output, or the vehicle's max acceptance rate. A fast charger can't
        // force a slow car to charge faster than its own hardware allows.
        double effectivePower = charger.getOutputPower();

        if (vehicle.getMaxChargingPower() != null && vehicle.getMaxChargingPower() > 0) {
            effectivePower = Math.min(effectivePower, vehicle.getMaxChargingPower());
        }

        double energyNeeded =
                vehicle.getBatteryCapacity()
                        * (targetBatteryPercentage - currentBatteryPercentage)
                        / 100.0;

        double chargingHours =
                energyNeeded / effectivePower;   // ← uses the capped value, not raw charger power

        return (int) Math.ceil(chargingHours * 60);
    }

    public Integer calculateWaitingTime(Charger charger) {

        if (charger == null) {
            throw new IllegalArgumentException("Charger cannot be null.");
        }

        List<Booking> bookings =
                bookingRepository.findByChargerAndStatusInOrderByBookedAtAsc(
                        charger,
                        List.of(BookingStatus.CHARGING, BookingStatus.WAITING)
                );

        int waitingTime = 0;

        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CHARGING) {
                waitingTime += getRemainingChargingTime(booking);
            } else {
                waitingTime += getSafeDuration(booking.getEstimatedChargingDuration());
            }
        }

        return waitingTime;
    }

    private int getRemainingChargingTime(Booking booking) {

        int estimated = getSafeDuration(booking.getEstimatedChargingDuration());

        // If we don't know when they checked in, we can't compute elapsed time —
        // safest assumption is that none of their session has passed yet,
        // rather than silently contributing zero.
        if (booking.getCheckedInAt() == null) {
            return estimated;
        }

        long elapsedMinutes = Duration.between(booking.getCheckedInAt(), LocalDateTime.now()).toMinutes();
        int remaining = estimated - (int) elapsedMinutes;

        return Math.max(remaining, 0);
    }

    private int getSafeDuration(Integer duration) {
        return duration != null ? duration : 0;
    }

    public ChargerSelectionResult calculatePrediction(
            Charger charger,
            Vehicle vehicle,
            Integer currentBatteryPercentage,
            Integer targetBatteryPercentage
    ) {
        int waitingTime = calculateWaitingTime(charger);

        int chargingDuration = calculateEstimatedChargingDuration(
                vehicle,
                charger,
                currentBatteryPercentage,
                targetBatteryPercentage
        );

        int totalTime = waitingTime + chargingDuration;

        return new ChargerSelectionResult(
                charger,
                waitingTime,
                chargingDuration,
                totalTime
        );
    }

    public ChargerSelectionResult pickBestCharger(
            ChargingStation station,
            Vehicle vehicle,
            Integer currentBatteryPercentage,
            Integer targetBatteryPercentage
    ) {

        if (station == null) {
            throw new IllegalArgumentException("Station cannot be null.");
        }

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        List<Charger> chargers = chargerRepository.findByChargingStation(station);

        ChargerSelectionResult bestResult = null;
        int minimumTotalTime = Integer.MAX_VALUE;

        for (Charger charger : chargers) {

            if (charger.getChargerStatus() != ChargerStatus.AVAILABLE) {
                continue;
            }

            if (charger.getConnectorType() != vehicle.getConnectorType()) {
                continue;
            }

            ChargerSelectionResult prediction = calculatePrediction(
                    charger,
                    vehicle,
                    currentBatteryPercentage,
                    targetBatteryPercentage
            );

            if (prediction.getEstimatedCompletionTime() < minimumTotalTime) {
                minimumTotalTime = prediction.getEstimatedCompletionTime();
                bestResult = prediction;
            }
        }

        if (bestResult == null) {
            throw new IllegalStateException("No compatible charger found.");
        }

        return bestResult;
    }
}

