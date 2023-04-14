package com.example.backendtourservice.service.weatherair;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.RankDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;
import com.example.backendtourservice.repository.weatherair.RankDataRepositroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {
    // 날씨
    // 맑음 1
    // 구름많음 2, 구름많고 비 4, 구름많고 눈 6, 구름많고 비/눈 8, 구름많고 소나기 10
    // 흐림 3 , 흐리고 비 5, 흐리고 눈 7, 흐리고 비/눈 9, 흐리고 소나기 11
    private final RankDataRepositroy rankDataRepositroy;
    private int makeRankWeatherData(String w) {
        switch (w) {
            case "맑음":
                return 1;
            case "구름많음":
                return 2;
            case "흐림":
                return 3;
            case "구름많고 비":
                return 4;
            case "흐리고 비":
                return 5;
            case "구름많고 눈":
                return 6;
            case "흐리고 눈":
                return 7;
            case "구름많고 비/눈":
                return 8;
            case "흐리고 비/눈":
                return 9;
            case "구름많고 소나기":
                return 10;
            case "흐리고 소나기":
                return 11;
            default:
                return 12;
        }
    }

    //  ((최저기온과 최고기온의 평균) - 18(최적 온도) )의 절댓값
    private double makeRankTemperatureData(Long tmn, Long tmx) {
        double averageTemperature = (double)(tmn + tmx) / 2;
        double optimumTemperature = 18.0;
        return Math.abs(averageTemperature - optimumTemperature);
    }

    // 대기 & 신뢰도
    // 낮음일 때만 조회하고 신뢰도-(높음, 보통, 낮음)순서로 조회
    // 낮음 1, 2, 3 높음 4, 5, 6 순으로 조회
    // 값이 없을 때 일단 7로 출
    private int makeRankAirData(String forecast, String reliability) {
        if (forecast.equals("낮음")) {
            switch (reliability) {
                case "높음":
                    return 1;
                case "보통":
                    return 2;
                case "낮음":
                    return 3;
                default:
                    break;
            }
        } else if (forecast.equals("높음")) {
            switch (reliability) {
                case "높음":
                    return 4;
                case "보통":
                    return 5;
                case "낮음":
                    return 6;
                default:
                    break;
            }
        }
        return 7;
    }

    // WeatherAirDataDTO -> RankDataDTO
    @Override
    public RankDataDTO makeRankData(WeatherAirDataDTO waDTO) {
        double temperature = makeRankTemperatureData(waDTO.getTmn(), waDTO.getTmx());

        int air = makeRankAirData(waDTO.getForecast(), waDTO.getReliability());
        int wAm = makeRankWeatherData(waDTO.getWAm());
        int wPm = makeRankWeatherData(waDTO.getWPm());

        return RankDataDTO.builder()
            .regionCode(waDTO.getRegionCode())
            .baseTime(waDTO.getBaseTime())
            .regionName(waDTO.getRegionName())
            .city(waDTO.getCity())
            .temperature(temperature)
            .air(air)
            .rAm(waDTO.getRAm().intValue())
            .rPm(waDTO.getRPm().intValue())
            .wAm(wAm)
            .wPm(wPm)
            .build();
    }

    // rankData DB에 저장
    @Override
    public WeatherAirCompositeKeyDTO updateRankData(RankDataDTO dto) {
        RankDataEntity entity = dtoToEntity(dto);
        rankDataRepositroy.save(entity);
        log.info("save RankData : {}", dto);
        return new WeatherAirCompositeKeyDTO(dto.getRegionCode(), dto.getBaseTime());
    }

}
