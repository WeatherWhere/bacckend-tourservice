package com.example.backendtourservice.service.rankdata;

import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherDTO;

public interface CalculateTCIService {
    RankDTO makeRankData(RankWeatherDTO rankWeatherDTO);
}
