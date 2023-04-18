package com.example.backendtourservice.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TouristSpotDTO {
    private String chkBabyCarriage;
    private String chkCreditCard;
    private String chkPet;
    private String expGuide;
    private String infoCenter;
    private String parking;
    private String restDate;
    private String useTime;

}
