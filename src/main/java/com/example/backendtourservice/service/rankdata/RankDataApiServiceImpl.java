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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RankAirDTO;
import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankCompositeKeyDTO;
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


    /**
     * 전국 시군구 지역의 오늘의 대기 단기 예보 호출 api url을 리턴합니다.
     *
     * @return 대기 단기 예보 호출 url을 리턴, 그렇지 않다면 throw 예외
     * @throws URISyntaxException
     */
    private URI makeWeatherUrl() throws URISyntaxException {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String baseUrl = "https://api.weatherwhere.link/weather/tour/data";

        log.info("날씨 호출 URL : {}", baseUrl);
        return new URI(baseUrl);
    }


    /**
     * 위경도를 가지고 대기 실시간 데이터 호출 api url을 리턴합니다.
     * @param locationX 경도
     * @param locationY 위도
     * @return 대기 실시간 데이터 호출 api url을 리턴, 그렇지 않다면 throw 예외
     * @throws URISyntaxException
     */
    // 해당 위경도 대기 실시간 데이터 호출 api url
    private URI makeAirUrl(Double locationX, Double locationY) throws URISyntaxException {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String baseUrl = "https://api.weatherwhere.link/air/realtime/tour/data?";

        // 경도
        String x = "x=" + locationX;
        // 위도
        String y = "&y=" + locationY;

        String resultUrl = baseUrl + x + y;
        log.info("대기 호출 URL : {}", resultUrl);
        return new URI(resultUrl);
    }


    /**
     * 미세먼지 농도로 등급 조회하여 리턴합니다.
     *
     * @param pm10Value 미세먼지 농도
     * @return 미세먼지 농도로 조회한 등급을 리턴
     */
    private String getPm10Grade(Integer pm10Value) {
        String grade = "";

        if (pm10Value >= 0 && pm10Value < 31) grade = "좋음";
        else if (pm10Value >= 31 && pm10Value < 81) grade = "보통";
        else if (pm10Value >= 81 && pm10Value < 151) grade = "나쁨";
        else if (pm10Value >= 151) grade = "매우나쁨";

        return grade;
    }


    /**
     * 초미세먼지 농도로 등급 조회하여 리턴합니다.
     *
     * @param pm25Value 초미세먼지 농도
     * @return 초미세먼지 농도로 조회한 등급을 리턴
     */
    private String getPm25Grade(Integer pm25Value) {
        String grade = "";

        if (pm25Value >= 0 && pm25Value < 16) grade = "좋음";
        else if (pm25Value >= 16 && pm25Value < 36) grade = "보통";
        else if (pm25Value >= 36 && pm25Value < 76) grade = "나쁨";
        else if (pm25Value >= 76) grade = "매우나쁨";

        return grade;
    }


    /**
     * 날씨 단기예보 api를 호출해서 얻은 json을 파싱해서 날씨 Rank 데이터로 가공하여 List<RankWeatherDTO>로 리턴합니다.
     *
     * @param result 날씨 단기예보 api를 호출해서 얻은 json
     * @return json을 파싱해서 날씨 Rank 데이터로 가공하여 List<RankWeatherDTO>로 리턴, 그렇지 않으면 throw 예외
     * @throws ParseException
     */
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


    /**
     * 대기 실시간 예보 api를 호출해서 얻은 json을 파싱해서 대기 Rank 데이터로 가공하여 RankAirDTO로 리턴합니다.
     *
     * @param result 대기 실시간 예보 api를 호출해서 얻은 json
     * @return json을 파싱해서 대기 Rank 데이터로 가공하여 RankAirDTO로 리턴, 그렇지 않으면 throw 예외
     * @throws ParseException
     */
    // json 데이터를 RankAirDto로 파싱
    private RankAirDTO jsonAirParsing(String result) {
        try {
            List<RankWeatherDTO> list = new ArrayList<>();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONObject data = (JSONObject)jsonObject.get("data");

            String stationName = (String)data.get("stationName");
            String pm10Grade = (String)data.get("pm10Grade");
            String pm25Grade = (String)data.get("pm25Grade");
            Integer pm10Value = ((Long)data.get("pm10Value")).intValue();
            Integer pm25Value = ((Long)data.get("pm25Value")).intValue();
            LocalDateTime dataTime = LocalDateTime.parse((String)data.get("dataTime"));

            RankAirDTO dto = RankAirDTO.builder()
                .stationName(stationName)
                .dataTime(dataTime)
                .pm10Grade(getPm10Grade(pm10Value))
                .pm10Value(pm10Value)
                .pm25Value(pm25Value)
                .pm25Grade(getPm25Grade(pm25Value))
                .build();

            log.info("RankAirData : {}", dto);
            return dto;
        } catch (ParseException e) {
            e.getStackTrace();
            log.warn("jsonAirParsing- ParseException 발생");
        } catch (NullPointerException e) {
            e.getStackTrace();
            log.warn("jsonAirParsing- NullPointerException 발생");
        } catch (Exception e) {
            e.getStackTrace();
            log.warn("jsonAirParsing- 예기치 못한 에러 발생");
        }
        return new RankAirDTO();
    }


    /**
     * 날씨 api를 호출하여 받은 json 값을 가공하여 날씨 RankData를 리턴합니다.
     *
     * @return 날씨 api를 호출하여 받은 데이터를 가공하여 날씨 RankData를 리턴, 그렇지 않으면 throw 예외
     * @throws ParseException
     * @throws URISyntaxException
     */
    private List<RankWeatherDTO> getRankWeatherData() throws ParseException, URISyntaxException {
        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 날씨 중기예보 data 받아오기
        String result = restTemplate.getForObject(makeWeatherUrl(),String.class);
        log.info("api 호출 데이터 : {}", result);
        // Json 파싱해서 data 저장
        List<RankWeatherDTO> list = jsonWeatherParsing(result);
        log.info("날씨 단기예보 호출 데이터:{}", list);
        return list;
    }

    /**
     * 대기 api를 호출하여 받은 json 값을 가공하여 대기 RankData를 가진 RankDTO를 리턴합니다.
     * @param dto 날씨 Rank 데이터를 포함하고 있는 RankDTO
     * @return 날씨와 대기 RankData를 포함한 RankDTO를 리턴
     */
    private RankDTO getAirData(RankDTO dto) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // RestTemplate으로 대기 실시간 data 받아오기
            String result = restTemplate.getForObject(makeAirUrl(dto.getLocationX(), dto.getLocationY()), String.class);
            log.info("api 호출 데이터 : {}", result);

            // Json 파싱
            RankAirDTO rankAirDTO = jsonAirParsing(result);

            dto.setPm10Grade(rankAirDTO.getPm10Grade());
            dto.setPm10Value(rankAirDTO.getPm10Value());
            dto.setPm25Grade(rankAirDTO.getPm25Grade());
            dto.setPm25Value(rankAirDTO.getPm25Value());

            log.info("대기 업데이트한 데이터 : {}", dto);
        } catch (URISyntaxException e) {
            e.getStackTrace();
            log.warn("getAirData()- URISyntaxException 발생");
        } catch (HttpClientErrorException e) {
            e.getStackTrace();
            log.warn("getAirData()- HttpClientErrorException 발생");
        }
        return dto;
    }


    /**
     * RankDTO를 DB에 업데이트 한 Entity의 복합키 RankCompositeKeyDTO를 리턴합니다.
     *
     * @param dto DB에 업데이트할 관광 순위를 가지고 있는 RankDTO
     * @return  DB에 업데이트 한 Entity의 복합키 RankCompositeKeyDTO를 리턴
     */
    private RankCompositeKeyDTO saveDb(RankDTO dto) {
        RankEntity entity = dtoToEntity(dto);
        log.info("entity: {}", entity);
        rankRepository.save(entity);
        return RankCompositeKeyDTO.builder()
            .level1(dto.getLevel1())
            .level2(dto.getLevel2())
            .baseDate(dto.getBaseDate())
            .build();
    }

    /**
     * 날씨 api와 대기 api를 호출하여 Rank Data를 DB에 업데이트하고 ResultDTO<List<RankCompositeKeyDTO>>를 리턴
     *
     * @return Rank Data를 DB에 업데이트하고 ResultDTO<List<RankCompositeKeyDTO>>를 리턴, 그렇지 않으면 예외 처리
     */
    // 날씨 rankWeatherData를 api를 호출하여 받아와서 RankData DB 업데이트
    @Override
    public ResultDTO<List<RankCompositeKeyDTO>> updateRankData() {
        List<RankCompositeKeyDTO> list = new ArrayList<>();
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.warn("대기 DB 해당 데이터가 null 값입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("예기치 못한 에러가 발생");
        }
        return ResultDTO.of(HttpStatus.OK.value(), "RankData를 저장하는데 성공하였습니다.", list);
    }
}
