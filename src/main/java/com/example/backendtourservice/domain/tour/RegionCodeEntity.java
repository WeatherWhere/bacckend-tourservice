package com.example.backendtourservice.domain.tour;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "region_code", schema = "tour")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegionCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시군구 코드
    @Column(name = "sigungu_code")
    private Integer sigunguCode;

    // 시군구 이름
    @Column(name = "sigungu")
    private String sigungu;

    // 지역 코드
    @Column(name = "region_code")
    private Integer regionCode;

    // 지역 이름
    @Column(name = "region")
    private String region;
}
