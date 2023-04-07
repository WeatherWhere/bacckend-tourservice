package com.example.backendtourservice.domain.weatherair;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class WeatherAirDataCompositeKey implements Serializable {
    // 예보 구역 코드
    @Column(name = "region_code")
    private String regionCode;
    // 예보 날짜
    @Column(name = "base_date")
    private LocalDate baseTime;


}
