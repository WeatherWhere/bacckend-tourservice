package com.example.backendtourservice.dto.rankdata;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * RankEntitiy의 복합키 DTO
 */
public class RankCompositeKeyDTO {
    private String level1;
    private String level2;

    //예보날짜
    private LocalDate baseDate;
}
