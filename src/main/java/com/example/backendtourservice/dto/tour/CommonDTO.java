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
 * 공통정보 데이터를 DTO로 생성
 */
public class CommonDTO {
    private String homePage;
    private String overView;
}
