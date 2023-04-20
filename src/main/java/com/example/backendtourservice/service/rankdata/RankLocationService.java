package com.example.backendtourservice.service.rankdata;

import java.util.List;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RecommendDTO;

public interface RankLocationService {
    ResultDTO<List<RecommendDTO>> getRecommendLocation();
}
