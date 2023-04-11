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
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rank_weather_air_data", schema = "tour")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RankDataEntity extends BaseEntity {
    @EmbeddedId
    private WeatherAirDataCompositeKey id;

    // 지역 이름
    @Column(name = "region_name")
    private String regionName;

    // 도시
    @Column(name = "city")
    private String city;

    //  ((최저기온과 최고기온의 평균) - 18(최적 온도) )의 절댓값
    @Column(name = "temperature")
    private double temperature;

    // 대기 & 신뢰도
    // 낮음, 높음을 바탕으로 신뢰도-(높음, 보통, 낮음)순서로 조회
    // 낮음 1, 2, 3 높음 4, 5, 6 순으로 조회
    @Column(name = "air")
    private int air;

    // 오전 강수 확률
    @Column(name = "r_am")
    private int rAm;

    // 오후 강수 확률
    @Column(name = "r_pm")
    private int rPm;

    // 날씨
    // 맑음 1
    // 구름많음 2, 구름많고 비 4, 구름많고 눈 6, 구름많고 비/눈 8, 구름많고 소나기 10
    // 흐림 3 , 흐리고 비 5, 흐리고 눈 7, 흐리고 비/눈 9, 흐리고 소나기 11

    // 오전 날씨
    @Column(name = "w_am")
    private int wAm;

    // 오후 날씨
    @Column(name = "w_pm")
    private int wPm;

    // 가중치를 곱한 값
    @Column(name = "rank_value")
    private Double rankValue;
}
