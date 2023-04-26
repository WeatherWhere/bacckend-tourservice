package com.example.backendtourservice.dto.rankdata;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * 지역을 추천시 반환할 DTO
 */
public class RecommendDTO {
   RecommendRankDTO rankValue;
   List<RecommendTourDTO> spots;
}
