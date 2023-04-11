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
public class MinMaxDataDTO {
    private LocalDate baseTime;
    private double minTemperature;
    private double maxTemperature;
    private int minAir;
    private int maxAir;
    private int minRAm;
    private int maxRAm;
    private int minRPm;
    private int maxRPm;
    private int minWAm;
    private int maxWAm;
    private int minWPm;
    private int maxWPm;
}
