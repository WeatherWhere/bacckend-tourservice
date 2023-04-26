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
 * 소개정보 음식점 데이터를 DTO로 생성
 */
public class FoodDTO {
    private String chkCreditCardFood;
    private String discountInfoFoof;
    private String firstMenu;
    private String infoCenterFood;
    private String kidsFacility;
    private String openTimeFood;
    private String packing;
    private String parkingFood;
    private String reservationFood;
    private String restDateFood;
    private String seat;
    private String smoking;
    private String treatMenu;
    private long lcnsno;
}
