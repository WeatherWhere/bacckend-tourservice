package com.example.backendtourservice.controller.weatherair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;
import com.example.backendtourservice.service.weatherair.CalculateRankDataService;
import com.example.backendtourservice.service.weatherair.WeatherAirDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
@Log4j2
public class WeatherAirDataController {
    private final WeatherAirDataService weatherAirDataService;
    private final CalculateRankDataService calculateRankDataService;

    @GetMapping("/data")
    public ResultDTO<List<WeatherAirCompositeKeyDTO>> updateRankData () {
        ResultDTO<List<WeatherAirCompositeKeyDTO>> result = weatherAirDataService.updateRankData(LocalDate.now());
        return result;
    }

    @GetMapping("/rank/update")
    public ResultDTO<List<WeatherAirCompositeKeyDTO>> updateRankValue(@RequestParam LocalDate baseDate) {
        ResultDTO<List<WeatherAirCompositeKeyDTO>> result = calculateRankDataService.updateRankValue(baseDate);
        return result;
    }

    @GetMapping("/test")
    public String getTest() {

        // weatherservice Pod에 요청을 보내고 응답을 반환
        String weatherserivceUrl = "https://api.weatherwhere.link/weather/forecast/short/main/now?locationX=37.541578&locationY=127.0487023";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(weatherserivceUrl, String.class);

        // 받은 응답을 출력
        System.out.println("Response from weathervice: " + response);

        return "Response from weatherservice: " + response;
    }
}
