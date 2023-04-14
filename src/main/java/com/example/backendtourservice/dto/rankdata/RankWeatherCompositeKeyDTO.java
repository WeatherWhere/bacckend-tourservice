package com.example.backendtourservice.dto.rankdata;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RankWeatherCompositeKeyDTO {
    //격자 x
    private Integer weatherX;

    //격자 y
    private Integer weatherY;

    //예보날짜
    private LocalDate baseDate;
}
