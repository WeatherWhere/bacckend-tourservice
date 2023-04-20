package com.example.backendtourservice.domain.rankdata;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class RankCompositeKey implements Serializable {
    @Column(name = "level1")
    private String level1;

    @Nullable
    @Column(name = "level2")
    private String level2;

    //예보날짜
    @Column(name = "base_date")
    private LocalDate baseDate;
}
