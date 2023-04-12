package com.example.backendtourservice.domain.tour;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tour_spot2", schema = "tour")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TourEntity {
    @Id
    @Column(name = "tour_id")
    private long contentid;
    @Column (name = "content_type_id")
    private long contenttypeid;
    @Column(name = "region_code")
    private int areacode;
    @Column(name = "location_x")
    private double mapx;
    @Column(name = "location_y")
    private double mapy;
    @Column(name = "title")
    private String title;
    @Column(name = "first_image")
    private String firstimage;
    @Column(name = "tel")
    private String tel;
    @Column(name = "zip_code")
    private String zipcode;

}
