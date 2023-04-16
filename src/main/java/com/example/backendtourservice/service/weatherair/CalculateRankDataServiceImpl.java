package com.example.backendtourservice.service.weatherair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.MinMaxDataDTO;
import com.example.backendtourservice.dto.weatherair.NormalizationDataDTO;
import com.example.backendtourservice.dto.weatherair.RankDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;
import com.example.backendtourservice.repository.weatherair.RankDataRepositroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalculateRankDataServiceImpl implements CalculateRankDataService{
    private final RankDataRepositroy rankDataRepositroy;

    // 최소-최대 정규화를 위해 필요한 해당 날짜의 각 속성의 최솟값과 최댓값 구하기
    private MinMaxDataDTO findMinMaxValue(LocalDate baseTime) {
        Double minTemperature = rankDataRepositroy.findMinValueOfTemperatureByBaseTime(baseTime);
        Double maxTemprature = rankDataRepositroy.findMaxValueOfTemperatureByBaseTime(baseTime);
        int minAir = rankDataRepositroy.findMinValueOfAirByBaseTime(baseTime);
        int maxAir = rankDataRepositroy.findMaxValueOfAirByBaseTime(baseTime);
        int minRAm = rankDataRepositroy.findMinValueOfRAmByBaseTime(baseTime);
        int maxRAm = rankDataRepositroy.findMaxValueOfRAmByBaseTime(baseTime);
        int minRPm = rankDataRepositroy.findMinValueOfRPmByBaseTime(baseTime);
        int maxRPm = rankDataRepositroy.findMaxValueOfRPmByBaseTime(baseTime);
        int minWAm = rankDataRepositroy.findMinValueOfWAmByBaseTime(baseTime);
        int maxWAm = rankDataRepositroy.findMaxValueOfWAmByBaseTime(baseTime);
        int minWPm = rankDataRepositroy.findMinValueOfWPmByBaseTime(baseTime);
        int maxWPm = rankDataRepositroy.findMaxValueOfWPmByBaseTime(baseTime);

        return MinMaxDataDTO.builder()
            .baseTime(baseTime)
            .minTemperature(minTemperature)
            .maxTemperature(maxTemprature)
            .minAir(minAir)
            .maxAir(maxAir)
            .minRAm(minRAm)
            .maxRAm(maxRAm)
            .minRPm(minRPm)
            .maxRPm(maxRPm)
            .minWAm(minWAm)
            .maxWAm(maxWAm)
            .minWPm(minWPm)
            .maxWPm(maxWPm)
            .build();
    }

    // 최소-최대 정규화 공식
    private double minMaxNormalization(Object data, Object min, Object max) {
        double dData = 0.0;
        double dMin = 0.0;
        double dMax = 0.0;

        // Object로 들어온 데이터의 타입이 int일 때와 double일 때 double로 형변환해주기
        if (data instanceof Integer) {
            dData = (double)(Integer)data;
        } else if (data instanceof Double) {
            dData = (double)(Double)data;
        }
        if (min instanceof Integer) {
            dMin = (double)(Integer)min;
            dMax = (double)(Integer)max;
        } else if (min instanceof Double) {
            dMin = (double)(Double)min;
            dMax = (double)(Double)max;
        }
        // min 값과 max 값이 같을 때는 정규화 값 0
        if (dMin == dMax) return 0.0;

        return (dData - dMin) / (dMax - dMin);
    }

    // 정규화 데이터로 만들기
    private NormalizationDataDTO normalizationData(RankDataDTO dto, MinMaxDataDTO minMaxDataDTO) {

        return NormalizationDataDTO.builder()
            .baseTime(dto.getBaseTime())
            .temperature(minMaxNormalization(dto.getTemperature(), minMaxDataDTO.getMinTemperature(), minMaxDataDTO.getMaxTemperature()))
            .air(minMaxNormalization(dto.getAir(), minMaxDataDTO.getMinAir(), minMaxDataDTO.getMaxAir()))
            .rAm(minMaxNormalization(dto.getRAm(), minMaxDataDTO.getMinRAm(), minMaxDataDTO.getMaxRAm()))
            .rPm(minMaxNormalization(dto.getRPm(), minMaxDataDTO.getMinRPm(), minMaxDataDTO.getMaxRPm()))
            .wAm(minMaxNormalization(dto.getWAm(), minMaxDataDTO.getMinWAm(), minMaxDataDTO.getMaxWAm()))
            .wPm(minMaxNormalization(dto.getWPm(), minMaxDataDTO.getMinWPm(), minMaxDataDTO.getMaxWPm()))
            .build();
    }

    // 정규화한 데이터에 가중치 곱하기
    private Double calculateRankValue(RankDataDTO dataDTO, MinMaxDataDTO minMaxDataDTO) {
        // 가중치 임의로 줌
        double rPmW =  0.35;
        double rAmW = 0.1;
        double airW = 0.2;
        double tempW = 0.2;
        double wAmW = 0.1;
        double wPmW = 0.05;

        log.info("rankData dto : {}", dataDTO);
        // 정규화 데이터
        NormalizationDataDTO dto = normalizationData(dataDTO, minMaxDataDTO);
        log.info("정규화 dto : {}", dto);

        // 가중치 곱해서 더하기
        double result = (dto.getTemperature() * tempW)
            + (dto.getAir() * airW)
            + (dto.getRAm() * rAmW)
            + (dto.getRPm() * rPmW)
            + (dto.getWAm() * wAmW)
            + (dto.getWPm() * wPmW);

        return result;
    }

    // rankValue: 순위를 매길 데이터
    // 각 속성을 정규화한 다음 가중치를 곱하여 더해준 데이터
    // rankValue를 RankDataEntity에 업데이트하는 메서드
   @Override
   @Transactional
    public ResultDTO<List<WeatherAirCompositeKeyDTO>> updateRankValue(LocalDate baseDate) {
       List<WeatherAirCompositeKeyDTO> list = new ArrayList<>();
        // 정규화할 때 필요한 min, max 값
        MinMaxDataDTO minMaxDataDTO = findMinMaxValue(baseDate);
        log.info("minMaxDTO : {}", minMaxDataDTO);

        // 해당 날짜의 rankData
        List<RankDataEntity> rankDataList = rankDataRepositroy.findByIdBaseTime(baseDate);

        for (RankDataEntity entity : rankDataList) {
            RankDataDTO dto = entityToDto(entity);
            Double rankValue = calculateRankValue(dto, minMaxDataDTO);
            log.info("rankValue : {}", rankValue);
            // rankValue 값 업데이트
            rankDataRepositroy.updateRankValueById(rankValue, entity.getId());
            list.add(new WeatherAirCompositeKeyDTO(entity.getId().getRegionCode(), entity.getId().getBaseTime()));
        }

        return ResultDTO.of(HttpStatus.OK.value(), "RankValue를 업데이트하는데 성공하였습니다.", list);
    }
}
