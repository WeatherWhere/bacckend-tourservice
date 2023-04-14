package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherCompositeKeyDTO;

public interface RankWeatherApiService {
    ResultDTO<List<RankWeatherCompositeKeyDTO>> updateRankData();
}
