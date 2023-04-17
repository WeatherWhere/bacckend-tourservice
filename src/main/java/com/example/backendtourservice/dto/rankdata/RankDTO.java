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
public class RankDTO {
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
}
