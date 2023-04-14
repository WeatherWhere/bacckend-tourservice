package com.example.backendtourservice.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourDTO {
    private long contentId;
    private long contentTypeId;
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
