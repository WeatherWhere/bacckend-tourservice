package com.example.backendtourservice.service.weatherair;

import java.time.LocalDate;
import java.util.List;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.RankDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;

public interface CalculateRankDataService {
    ResultDTO<List<WeatherAirCompositeKeyDTO>> updateRankValue(LocalDate baseDate);

    default RankDataDTO entityToDto(RankDataEntity entity) {
        return RankDataDTO.builder()
            .regionCode(entity.getId().getRegionCode())
            .baseTime(entity.getId().getBaseTime())
            .regionName(entity.getRegionName())
            .city(entity.getCity())
            .temperature(entity.getTemperature())
            .air(entity.getAir())
            .rAm(entity.getRAm())
            .rPm(entity.getRPm())
            .wAm(entity.getWAm())
            .wPm(entity.getWPm())
            .build();
    }
}
