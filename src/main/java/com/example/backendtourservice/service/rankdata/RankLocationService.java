package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RecommendDTO;

public interface RankLocationService {
    /**
     * 관광할 지역을 날씨 관광지수와 대기의 값으로 5개를 추천하여 ResultDTO<List<RecommendDTO>>를 리턴합니다.
     *
     * @return 지역의 rankData와 그 지역의 관광지들을 함께 5개를 ResultDTO<List<RecommendDTO>>로 리턴
     */
    ResultDTO<List<RecommendDTO>> getRecommendLocation();
}
