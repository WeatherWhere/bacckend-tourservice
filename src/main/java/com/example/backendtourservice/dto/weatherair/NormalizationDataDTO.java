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
public class NormalizationDataDTO {
    private LocalDate baseTime;
    private double temperature;
    private double air;
    private double rAm;
    private double rPm;
    private double wAm;
    private double wPm;
}
