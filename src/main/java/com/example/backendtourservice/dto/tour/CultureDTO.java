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
 * 소개정보 문화시설 데이터를 DTO로 생성
 */
public class CultureDTO {
    private String chkBabyCarriageCulture;
    private String chkCreditCardCulture;
    private String chkPetCulture;
    private String discountInfo;
    private String infoCenterCulture;
    private String parkingCulture;
    private String parkingFee;
    private String restDateCulture;
    private String useFee;
    private String useTimeCulture;
}
