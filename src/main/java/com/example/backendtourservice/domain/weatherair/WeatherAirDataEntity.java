package com.example.backendtourservice.domain.weatherair;

import com.example.backendtourservice.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "weather_air_data", schema = "tour")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherAirDataEntity extends BaseEntity {

    @EmbeddedId
    private WeatherAirDataCompositeKey id;

    // 지역 이름
    @Column(name = "region_name")
    private String regionName;

    // 도시
    @Column(name = "city")
    private String city;

    // 일 최저기온
    @Column(name = "tmn")
    private Long tmn;

    // 일 최고기온
    @Column(name = "tmx")
    private Long tmx;

    // 오전 강수 확률
    @Column(name = "r_am")
    private Long rAm;

    // 오후 강수 확률
    @Column(name = "r_pm")
    private Long rPm;

    // 오전 날씨
    @Column(name = "w_am")
    private String wAm;

    // 오후 날씨
    @Column(name = "w_pm")
    private String wPm;

    // 대기 예보
    @Column(name="forecast")
    private String forecast;

    // 대기 신뢰도
    @Column(name="reliability")
    private String reliability;
}
