package com.example.backendtourservice.service.weatherair;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.example.backendtourservice.domain.weatherair.WeatherAirDataCompositeKey;
import com.example.backendtourservice.domain.weatherair.WeatherAirDataEntity;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;

public interface WeatherAirDataSerivce {
    List<WeatherAirDataDTO> updateWeatherAirData(String regionCode, LocalDate date) throws
        ParseException,
        URISyntaxException;

    default WeatherAirDataEntity dtoToEntity(WeatherAirDataDTO dto){
        WeatherAirDataCompositeKey weatherAirDataCompositeKey=new WeatherAirDataCompositeKey(dto.getRegionCode(),dto.getBaseTime());
        WeatherAirDataEntity entity= WeatherAirDataEntity.builder()
            .id(weatherAirDataCompositeKey)
            .regionName(dto.getRegionName())
            .city(dto.getCity())
            .tmn(dto.getTmn())
            .tmx(dto.getTmx())
            .rAm(dto.getRAm())
            .rPm(dto.getRPm())
            .wAm(dto.getWAm())
            .wPm(dto.getWPm())
            .forecast(dto.getForecast())
            .reliability(dto.getReliability())
            .build();
        return entity;
    }

    // entity를 dto로 변환해주는 메서드
    default WeatherAirDataDTO entityToDTO(WeatherAirDataEntity entity) {
        WeatherAirDataDTO dto = WeatherAirDataDTO.builder()
            .regionCode(entity.getId().getRegionCode())
            .baseTime(entity.getId().getBaseTime())
            .regionName(entity.getRegionName())
            .city(entity.getCity())
            .tmn(entity.getTmn())
            .tmx(entity.getTmx())
            .rAm(entity.getRAm())
            .rPm(entity.getRPm())
            .wAm(entity.getWAm())
            .wPm(entity.getWPm())
            .forecast(entity.getForecast())
            .reliability(entity.getReliability())
            .build();
        return dto;
    }
}
