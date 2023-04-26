package com.example.backendtourservice.dto.rankdata;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * 날씨 단기예보 api를 호출하여 받아온 DTO
 */
public class RankWeatherDTO {
    private String level1;
    private String level2;

    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;

    // 경도
    private Double locationX;

    // 위도
    private Double locationY;

    //예보날짜
    private LocalDate baseDate;

    // 6 ~ 18시 누적 강수량
    private Double sumPcp;

    // 6 ~ 18시 하늘상태 평균
    private Double avgSky;

    // 일평균기온
    private Double avgTmp;

    // 6 ~ 18시 풍속 평균
    private Double avgWsd;

    // 일평균 습도
    private Double avgReh;

    // 최소 습도
    private Double minReh;

    //일 최고기온
    private Double maxTmp;
}
