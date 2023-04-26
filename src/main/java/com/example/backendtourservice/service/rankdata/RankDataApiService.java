package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.domain.rankdata.RankCompositeKey;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankCompositeKeyDTO;

public interface RankDataApiService {
    /**
     * 순위를 매길 데이터를 db에 업데이트하고 ResultDTO<List<RankCompositeKeyDTO>>를 리턴합니다.
     *
     * @return db에 업데이트한 RankData의 복합키를 ResultDTO<List<RankCompositeKeyDTO>> 형태로 리턴
     */
    ResultDTO<List<RankCompositeKeyDTO>> updateRankData();

    /**
     * RankDTO를 RankEntity로 변환하여 RankEntity를 리턴합니다.
     *
     * @param dto RankDTO 순위 지수 데이터
     * @return dto를 entity로 변환하였다면 RankEntity 리턴
     */
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
            .pm10Value(dto.getPm10Value())
            .pm25Value(dto.getPm25Value())
            .pm25Grade(dto.getPm25Grade())
            .build();
    }
}
