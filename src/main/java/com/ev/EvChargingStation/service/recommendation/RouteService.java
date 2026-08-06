package com.ev.EvChargingStation.service.recommendation;

import com.ev.EvChargingStation.service.recommendation.OpenRouteServiceClient;
import com.ev.EvChargingStation.service.recommendation.model.CandidateStation;
import com.ev.EvChargingStation.service.recommendation.model.RouteInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final OpenRouteServiceClient openRouteServiceClient;

    public List<CandidateStation> enrichCandidatesWithRoutes(
            List<CandidateStation> candidates,
            double userLatitude,
            double userLongitude
    ) {

        for (CandidateStation candidate : candidates) {

            RouteInfo route =
                    openRouteServiceClient.getRoute(
                            userLatitude,
                            userLongitude,
                            candidate.getStation().getLatitude(),
                            candidate.getStation().getLongitude()
                    );

            candidate.setDrivingDistanceKm(
                    route.drivingDistanceKm()
            );

            candidate.setEstimatedTravelTimeMinutes(
                    route.estimatedTravelTimeMinutes()
            );
        }

        return candidates;
    }
}