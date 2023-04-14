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
public class WeatherAirCompositeKeyDTO {
    private String regionCode;
    private LocalDate baseTime;
}
