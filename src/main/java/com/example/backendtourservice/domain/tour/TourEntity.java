package com.example.backendtourservice.domain.tour;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_spot", schema = "tour")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TourEntity {
    @Id
    @Column(name = "tour_id")
    private long contentId;
    @Column (name = "content_type_id")
    private int contentTypeId;
    @Column(name = "region_code")
    private int areaCode;
    @Column(name = "sigungu_code")
    private int sigunguCode;
    @Column(name = "location_x")
    private double mapx;
    @Column(name = "location_y")
    private double mapy;
    @Column(name = "title")
    private String title;
    @Column(name = "first_image")
    private String firstImage;
    @Column(name = "addr")
    private String addr;
    @Column(name = "tel")
    private String tel;
    @Column(name = "zip_code")
    private String zipcode;

}
