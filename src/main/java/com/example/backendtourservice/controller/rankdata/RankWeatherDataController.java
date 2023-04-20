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
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
@Log4j2
public class RankWeatherDataController {
    private final RankDataApiService rankDataApiService;
    private final RankLocationService rankLocationService;

    @GetMapping("/rank/update")
    public ResultDTO<List<RankCompositeKeyDTO>> updateRankData() {
        ResultDTO<List<RankCompositeKeyDTO>> result = rankDataApiService.updateRankData();
        return result;
    }

    @GetMapping("/recommend")
    public ResultDTO<List<RecommendDTO>> recommendLocation() {
        ResultDTO<List<RecommendDTO>> resultDTO = rankLocationService.getRecommendLocation();
        return resultDTO;
    }

    @GetMapping("/test")
    public String getTest() {

        // weatherservice Pod에 요청을 보내고 응답을 반환
        String weatherserivceUrl = "https://weatherservice:8080/weather/forecast/short/main/now?locationX=37.541578&locationY=127.0487023";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(weatherserivceUrl, String.class);

        // 받은 응답을 출력
        System.out.println("Response from weathervice: " + response);

        return "Response from weatherservice: " + response;
    }

}
