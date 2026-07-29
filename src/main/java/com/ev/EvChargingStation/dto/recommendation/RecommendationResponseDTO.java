package com.ev.EvChargingStation.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDTO {

    private Long stationId;
    private String stationName;
    private Double distance;
    private Double eta;
    private Double waitingTime;
    private Double pricePerKwh;
    private Integer availableChargers;
    private Double recommendationScore;
    private String reason;
}
