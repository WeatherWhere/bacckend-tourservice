package com.example.backendtourservice.service.rankdata;

import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherDTO;

public interface CalculateTCIService {
    /**
     * rank 날씨 데이터 rankWeatherDTO를 Rank 데이터로 가공하여 RankDTO를 리턴합니다.
     *
     * @param rankWeatherDTO rank 날씨 데이터
     * @return rankWeatherDTO를 Rank 데이터로 가공한 RankDTO를 리턴
     */
    RankDTO makeRankData(RankWeatherDTO rankWeatherDTO);
}
