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
    private int goodStay;
    private int hanok;
    private String infoCenterLodging;
    private String parkingLodging;
    private String pickUp;
    private String reservationLodging;
    private String reservationUrl;
    private String roomType;
    private String subFacility;
    private int barbecue;
    private int beauty;
    private int beverage;
    private int bicycle;
    private int campfire;
    private int fitness;
    private int karaoke;
    private int publicBath;
    private int publicPc;
    private int sauna;
    private int seminar;
    private int sports;
    private String refundRegulation;
}
