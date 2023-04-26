package com.example.backendtourservice.controller.rankdata;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankCompositeKeyDTO;
import com.example.backendtourservice.dto.rankdata.RecommendDTO;
import com.example.backendtourservice.service.rankdata.RankDataApiService;
import com.example.backendtourservice.service.rankdata.RankLocationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
@Log4j2
public class RankWeatherDataController {
    private final RankDataApiService rankDataApiService;
    private final RankLocationService rankLocationService;

    /**
     * 순위를 매길 데이터를 db에 업데이트하고 ResultDTO<List<RankCompositeKeyDTO>>를 리턴합니다.
     *
     * @return db에 업데이트한 RankData의 복합키를 ResultDTO<List<RankCompositeKeyDTO>> 형태로 리턴
     */
    @GetMapping("/rank/update")
    public ResultDTO<List<RankCompositeKeyDTO>> updateRankData() {
        ResultDTO<List<RankCompositeKeyDTO>> result = rankDataApiService.updateRankData();
        return result;
    }

    /**
     * 관광할 지역을 날씨 관광지수와 대기의 값으로 5개를 추천하여 ResultDTO<List<RecommendDTO>>를 리턴합니다.
     *
     * @return 지역의 rankData와 그 지역의 관광지들을 함께 5개를 ResultDTO<List<RecommendDTO>>로 리턴
     */
    @GetMapping("/recommend")
    public ResultDTO<List<RecommendDTO>> recommendLocation() {
        ResultDTO<List<RecommendDTO>> resultDTO = rankLocationService.getRecommendLocation();
        return resultDTO;
    }

}
