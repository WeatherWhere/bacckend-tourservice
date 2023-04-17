package com.example.backendtourservice.service.rankdata;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.example.backendtourservice.dto.rankdata.RankAirDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherCompositeKeyDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherDTO;
import com.example.backendtourservice.repository.rankdata.RankRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RankDataApiServiceImpl implements RankDataApiService {

    private final CalculateTCIService calculateTCIService;
    private final RankRepository rankRepository;

    // 해당 지역, 예보 날짜 api 대기 주간예보 url
    private URI makeWeatherUrl() throws URISyntaxException {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String baseUrl = "http://k8s-weatherw-weatherw-96e049a27a-1334965090.ap-northeast-2.elb.amazonaws.com/weather/tour/data";
        log.info("baseUrl : {}", baseUrl);
        return new URI(baseUrl);
    }

    // 해당 위경도 대기 실시간 데이터 호출 api url
    private URI makeAirUrl(Double locationX, Double locationY) throws URISyntaxException {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String baseUrl = "http://k8s-weatherw-weatherw-96e049a27a-1334965090.ap-northeast-2.elb.amazonaws.com/air/tour/data?";
        // 경도
        String x = "x=" + locationX;
        // 위도
        String y = "&y=" + locationY;

        String resultUrl = baseUrl + x + y;
        log.info("대기 호출 url : {}", resultUrl);
        return new URI(resultUrl);
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
            Double locationX = (Double)data.get("locationX");
            log.info("locationX : {}", locationX);
            Double locationY = (Double)data.get("locationY");
            RankWeatherDTO dto = RankWeatherDTO.builder()
                .level1(level1)
                .level2(level2)
                .weatherX(weatherX)
                .weatherY(weatherY)
                .locationX(locationX)
                .locationY(locationY)
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

    // json 데이터를 RankAirDto로 파싱
    private RankAirDTO jsonAirParsing(String result) throws ParseException {
        List<RankWeatherDTO> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONArray dat = (JSONArray)jsonObject.get("data");

        JSONObject data = (JSONObject)dat.get(0);
        String stationName = (String)data.get("stationName");
        String pm10Grade = (String)data.get("pm10Grade");
        LocalDateTime dataTime = LocalDateTime.parse((String)data.get("dataTime"));

        RankAirDTO dto = RankAirDTO.builder()
            .stationName(stationName)
            .dataTime(dataTime)
            .pm10Grade(pm10Grade)
            .build();

        log.info("RankAirData : {}", dto);
        return dto;
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

    private RankDTO getAirData(RankDTO dto) throws ParseException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate으로 대기 실시간 data 받아오기
        String result = restTemplate.getForObject(makeAirUrl(dto.getLocationX(), dto.getLocationY()), String.class);
        log.info("api 호출 데이터 : {}", result);

        // Json 파싱
        RankAirDTO rankAirDTO = jsonAirParsing(result);

        dto.setPm10Grade(rankAirDTO.getPm10Grade());
        log.info("대기 업데이트한 데이터 : {}", dto);
        return dto;
    }

    // db 저장
    private RankWeatherCompositeKeyDTO saveDb(RankDTO dto) {
        RankEntity entity = dtoToEntity(dto);
        log.info("entity: {}", entity);
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

                // 대기 데이터 추가
                rankDataDTO = getAirData(rankDataDTO);

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
