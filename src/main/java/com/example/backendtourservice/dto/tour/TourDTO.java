package com.example.backendtourservice.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * DB에 조회하거나 저장할 관광 정보 DTO
 */
public class TourDTO {
    private long contentId;
    private int contentTypeId;
    private int areaCode;
    private int sigunguCode;
    private double mapx;
    private double mapy;
    private String title;
    private String firstImage;
    private String tel;
    private String zipcode;
    private String addr;
}
