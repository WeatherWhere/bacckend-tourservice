package com.example.backendtourservice.domain.rankdata;

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
public class RankWeatherCompositeKey implements Serializable {
    //격자 x
    @Column(name = "weather_x")
    private Integer weatherX;

    //격자 y
    @Column(name = "weather_y")
    private Integer weatherY;

    //예보날짜
    @Column(name = "base_date")
    private LocalDate baseDate;
}
