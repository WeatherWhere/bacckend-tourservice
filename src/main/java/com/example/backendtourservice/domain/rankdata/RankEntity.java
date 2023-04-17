package com.example.backendtourservice.domain.rankdata;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rank_data", schema = "tour")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RankEntity {
    @EmbeddedId
    private RankWeatherCompositeKey id;

    // 경도
    @Column(name = "location_x")
    private Double locationX;

    // 위도
    @Column(name = "location_y")
    private Double locationY;

    @Column(name = "level1")
    private String level1;

    @Column(name = "level2")
    private String level2;

    // 한낮 열쾌적지수 등급
    @Column(name = "hn_grade")
    private String HNGrade;

    // 일평균 열쾌적지수 등급
    @Column(name = "hd_grade")
    private String HDGrade;

    // 바람지수 등급
    @Column(name = "w_grade")
    private String WGrade;

    // 강수지수 등급
    @Column(name = "r_grade")
    private String RGrade;

    // 일사지수 등급
    @Column(name = "si_grade")
    private String SIGrade;

    // 관광기후지수
    @Column(name = "tci")
    private Double TCI;

    // 관광기후지수 등급
    @Column(name = "tci_grade")
    private String TCIGrade;
}
