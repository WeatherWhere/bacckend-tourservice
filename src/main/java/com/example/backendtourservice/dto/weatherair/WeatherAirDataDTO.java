package com.example.backendtourservice.dto.weatherair;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeatherAirDataDTO {
    private String regionCode;
    private LocalDate baseTime;
    private String forecast;
    private String reliability;
    private String regionName;
    private String city;
    private Long tmn;
    private Long tmx;
    private Long rAm;
    private Long rPm;
    private String wAm;
    private String wPm;
}
