package com.example.backendtourservice.dto.rankdata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * 대기 실시간 api를 호출하여 받아온 DTO
 */
public class RankAirDTO {

    // 측정소명
    private String stationName;

    // 예보시간
    private LocalDateTime dataTime;

    // 미세먼지 1시간 등급
    private String pm10Grade;

    // 미세먼지 농도
    private Integer pm10Value;

    // 초미세먼지 농도
    private Integer pm25Value;

    // 초미세먼지 1시간 등급
    private String pm25Grade;
}
