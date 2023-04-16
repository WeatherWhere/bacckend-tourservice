package com.example.backendtourservice.service.rankdata;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherCompositeKeyDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherDTO;
import com.example.backendtourservice.repository.rankdata.RankRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RankWeatherApiServiceImpl implements RankWeatherApiService {

    private final CalculateTCIService calculateTCIService;
    private final RankRepository rankRepository;

    // 해당 지역, 예보 날짜 Air api 대기 주간예보 url
    private URI makeWeatherUrl() throws URISyntaxException {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String baseUrl = "http://localhost:8080/weather/tour/data";
        log.info("baseUrl : {}", baseUrl);
        return new URI(baseUrl);
    }

    // json 데이터를 RankWeatherDto로 파싱
    private List<RankWeatherDTO> jsonWeatherParsing(String result) throws ParseException {
        List<RankWeatherDTO> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONArray dat = (JSONArray)jsonObject.get("data");

        int len = dat.size();

        for (int i = 0 ; i < len ; i++) {
            JSONObject data = (JSONObject)dat.get(i);
            String level1 = (String)data.get("level1");
            String level2 = (String)data.get("level2");
            LocalDate baseDate = LocalDate.parse((String)data.get("fcstDate"));
            Double sumPcp = (Double)data.get("sumPcp");
            Double avgSky = (Double)data.get("avgSky");
            Double avgTmp = (Double)data.get("avgTmp");
            Double avgWsd = (Double)data.get("avgWsd");
            Double avgReh = (Double)data.get("avgReh");
            Double minReh = (Double)data.get("minReh");
            Double maxTmp = (Double)data.get("maxTmp");
            Integer weatherX = ((Long)((Long)data.get("weatherX"))).intValue();
            Integer weatherY = ((Long)((Long)data.get("weatherY"))).intValue();

            RankWeatherDTO dto = RankWeatherDTO.builder()
                .level1(level1)
                .level2(level2)
                .weatherX(weatherX)
                .weatherY(weatherY)
                .baseDate(baseDate)
                .sumPcp(sumPcp)
                .avgSky(avgSky)
                .avgTmp(avgTmp)
                .avgWsd(avgWsd)
                .avgReh(avgReh)
                .minReh(minReh)
                .maxTmp(maxTmp)
                .build();

            list.add(dto);
        }
        log.info("RankWeatherDataList : {}", list);
        return  list;
    }

    // 날씨 rank Data api 호출하여 데이터 받아오기
    private List<RankWeatherDTO> getRankWeatherData() throws ParseException, URISyntaxException {
        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 날씨 중기예보 data 받아오기
        String result = restTemplate.getForObject(makeWeatherUrl(),String.class);
        log.info("api 호출 데이터 : {}", result);
        // Json 파싱해서 data 저장
        List<RankWeatherDTO> list = jsonWeatherParsing(result);
        log.info("날씨 중기예보 호출 데이터:{}", list);
        return list;
    }

    // db 저장
    private RankWeatherCompositeKeyDTO saveDb(RankDTO dto) {
        RankEntity entity = dtoToEntity(dto);
        rankRepository.save(entity);
        return RankWeatherCompositeKeyDTO.builder()
            .weatherY(dto.getWeatherY())
            .weatherX(dto.getWeatherX())
            .baseDate(dto.getBaseDate())
            .build();
    }

    // 날씨 rankWeatherData를 api를 호출하여 받아와서 RankData DB 업데이트
    @Override
    public ResultDTO<List<RankWeatherCompositeKeyDTO>> updateRankData() {
        List<RankWeatherCompositeKeyDTO> list = new ArrayList<>();
        try {
            // 날씨 rankWeatherData 가져오기
            List<RankWeatherDTO> rankWeatherDTOList = getRankWeatherData();
            int len = rankWeatherDTOList.size();
            for (int i = 0; i < len; i++) {
                log.info("rankWeatherDTO : {}", rankWeatherDTOList.get(i));
                RankDTO rankDataDTO = calculateTCIService.makeRankData(rankWeatherDTOList.get(i));
                log.info("rankDataDTO : {}", rankDataDTO);
                list.add(saveDb(rankDataDTO));
            }
            log.info("list : {}", rankWeatherDTOList);
        } catch (ParseException e) {
            // json 데이터 파싱할 때 error
            e.printStackTrace();
            log.error("ParseException이 발생");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("예기치 못한 에러가 발생");
        }
        return ResultDTO.of(HttpStatus.OK.value(), "RankData를 저장하는데 성공하였습니다.", list);
    }
}
