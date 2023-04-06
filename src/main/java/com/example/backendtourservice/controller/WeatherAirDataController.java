package com.example.backendtourservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;
import com.example.backendtourservice.service.weatherair.WeatherAirDataSerivce;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class WeatherAirDataController {
    private final WeatherAirDataSerivce weatherAirDataSerivce;

    @GetMapping("/data")
    public ResultDTO<List<WeatherAirDataDTO>> getWeatherMidForecast (@RequestParam("regionCode") String regionCode) {
        try {
            List<WeatherAirDataDTO> list = weatherAirDataSerivce.updateWeatherAirData(regionCode, LocalDate.now());
            return ResultDTO.of(HttpStatus.OK.value(),"날씨&대기 데이터를 호출하는데 성공하였습니다.", list);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }
}
