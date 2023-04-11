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
}
