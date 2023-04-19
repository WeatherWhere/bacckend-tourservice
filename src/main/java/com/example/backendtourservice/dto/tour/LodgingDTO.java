package com.example.backendtourservice.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LodgingDTO {
    private String checkinTime;
    private String checkoutTime;
    private String chkCooking;
    private String foodPlace;
    private String infoCenterLodging;
    private String parkingLodging;
    private String pickUp;
    private String reservationLodging;
    private String reservationUrl;
    private String roomType;
    private String subFacility;
    private String barbecue;
    private String beauty;
    private String beverage;
    private String bicycle;
    private String campfire;
    private String fitness;
    private String karaoke;
    private String publicBath;
    private String publicPc;
    private String sauna;
    private String seminar;
    private String sports;
    private String refundRegulation;
}
