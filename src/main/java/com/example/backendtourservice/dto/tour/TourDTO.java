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
    private long contentid;
    private long contenttypeid;
    private int areacode;
    private double mapx;
    private double mapy;
    private String title;
    private String firstimage;
    private String tel;
    private String zipcode;
}
