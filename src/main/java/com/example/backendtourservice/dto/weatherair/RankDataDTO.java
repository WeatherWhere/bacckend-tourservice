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
public class RankDataDTO {
    private String regionCode;
    private LocalDate baseTime;
    private String regionName;
    private String city;
    private double temperature;
    private int air;
    private int rAm;
    private int rPm;
    private int wAm;
    private int wPm;
}
