package com.example.backendtourservice.service.weatherair;

import java.time.LocalDate;
import java.util.List;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.domain.weatherair.WeatherAirDataCompositeKey;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.RankDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;

public interface RankService {
    RankDataDTO makeRankData(WeatherAirDataDTO waDTO);
    WeatherAirCompositeKeyDTO updateRankData(RankDataDTO dto);

    // dto -> entity
    default RankDataEntity dtoToEntity(RankDataDTO dto) {
        WeatherAirDataCompositeKey id = new WeatherAirDataCompositeKey(dto.getRegionCode(), dto.getBaseTime());

        return RankDataEntity.builder()
            .id(id)
            .air(dto.getAir())
            .wAm(dto.getWAm())
            .wPm(dto.getWPm())
            .regionName(dto.getRegionName())
            .city(dto.getCity())
            .temperature(dto.getTemperature())
            .rPm(dto.getRPm())
            .rAm(dto.getRAm())
            .build();
    }
}
