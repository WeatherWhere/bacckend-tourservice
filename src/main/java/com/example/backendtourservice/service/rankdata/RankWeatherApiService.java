package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.domain.rankdata.RankWeatherCompositeKey;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherCompositeKeyDTO;

public interface RankWeatherApiService {
    ResultDTO<List<RankWeatherCompositeKeyDTO>> updateRankData();

    default RankEntity dtoToEntity(RankDTO dto) {
        RankWeatherCompositeKey id = RankWeatherCompositeKey.builder()
            .weatherY(dto.getWeatherY())
            .weatherX(dto.getWeatherX())
            .baseDate(dto.getBaseDate())
            .build();

        return RankEntity.builder()
            .id(id)
            .level1(dto.getLevel1())
            .level2(dto.getLevel2())
            .locationX(dto.getLocationX())
            .locationY(dto.getLocationY())
            .SIGrade(dto.getSIGrade())
            .TCI(dto.getTCI())
            .HNGrade(dto.getHNGrade())
            .RGrade(dto.getRGrade())
            .HDGrade(dto.getHDGrade())
            .WGrade(dto.getWGrade())
            .TCIGrade(dto.getTCIGrade())
            .build();
    }
}
