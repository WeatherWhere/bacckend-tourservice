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
public class RecommendRankDTO {

    // 지역
    private String region;

    // 시군구
    private String sigungu;

    // 경도
    private Double longitude;

    // 위도
    private Double latitude;

    //예보날짜
    private LocalDate baseDate;

    // 한낮 열쾌적지수 등급
    private String HNGrade;

    // 일평균 열쾌적지수 등급
    private String HDGrade;

    // 바람지수 등급
    private String WGrade;

    // 강수지수 등급
    private String RGrade;

    // 일사지수 등급
    private String SIGrade;

    // 관광기후지수
    private Double TCI;

    // 관광기후지수 등급
    private String TCIGrade;

    // 미세먼지(pm10) 1시간 등급
    private String pm10Grade;


    // 초미세먼지 1시간 등급
    private String pm25Grade;
}
