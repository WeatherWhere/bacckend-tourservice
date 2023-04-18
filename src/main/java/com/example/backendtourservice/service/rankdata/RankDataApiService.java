package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.domain.rankdata.RankCompositeKey;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankCompositeKeyDTO;

public interface RankDataApiService {
    ResultDTO<List<RankCompositeKeyDTO>> updateRankData();

    default RankEntity dtoToEntity(RankDTO dto) {
        RankCompositeKey id = RankCompositeKey.builder()
            .level1(dto.getLevel1())
            .level2(dto.getLevel2())
            .baseDate(dto.getBaseDate())
            .build();

        return RankEntity.builder()
            .id(id)
            .weatherY(dto.getWeatherY())
            .weatherX(dto.getWeatherX())
            .locationX(dto.getLocationX())
            .locationY(dto.getLocationY())
            .SIGrade(dto.getSIGrade())
            .TCI(dto.getTCI())
            .HNGrade(dto.getHNGrade())
            .RGrade(dto.getRGrade())
            .HDGrade(dto.getHDGrade())
            .WGrade(dto.getWGrade())
            .TCIGrade(dto.getTCIGrade())
            .pm10Grade(dto.getPm10Grade())
            .build();
    }
}
