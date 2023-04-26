package com.example.backendtourservice.dto.rankdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * 추천한 지역에 해당하는 관광지 DTO
 */
public class RecommendTourDTO {
    // 관광지 Id
    private long contentId;

    // 관광지 타입 Id
    private long contentTypeId;

    // 경도
    private Double longitude;

    // 위도
    private Double latitude;

    // 관광지 이름
    private String title;

    // 관광지 이미지
    private String firstImage;

    // 관광지 주소
    private String addr;

    // 관광지 전화번호
    private String tel;


    private String zipcode;
}
