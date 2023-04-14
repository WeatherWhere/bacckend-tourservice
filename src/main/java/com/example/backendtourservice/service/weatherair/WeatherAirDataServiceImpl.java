package com.example.backendtourservice.service.weatherair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.AirForecastDTO;
import com.example.backendtourservice.dto.weatherair.RankDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirCompositeKeyDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherMidDTO;
import com.example.backendtourservice.repository.weatherair.RankDataRepositroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherAirDataServiceImpl implements WeatherAirDataService {
    private final RankDataRepositroy rankDataRepositroy;

    private final RankService rankService;

    // 해당 지역, 예보 날짜 Air api 대기 주간예보 url
    private String makeAirUrl(LocalDate date,String city) {
        String baseUrl="http://localhost:8090/air/forecast/tour/data?";
        String baseDate="baseDate="+date;
        String region="&city="+city;

        return baseUrl+baseDate+region;
    }

    // 해당 지역, 예보 날짜 Air api 대기 주간예보 url
    private String makeWeatherUrl(LocalDate date) {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String sDate = date + "";
        sDate = sDate.replaceAll("-","");
        String baseUrl = "http://localhost:8080/weather/tour/data?";
        String baseDate = "&baseDate=" + sDate;

        return baseUrl + baseDate;
    }

    // json 데이터를 airForecastDto로 파싱
    private AirForecastDTO jsonAirParsing(String result) throws ParseException {
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject)jsonParser.parse(result);
        JSONObject data=(JSONObject)jsonObject.get("data");
        LocalDate date=LocalDate.parse((String)data.get("baseDate"), DateTimeFormatter.ISO_DATE);
        String city=(String)data.get("city");
        String forecast=(String)data.get("forecast");
        String reliability=(String)data.get("reliability");
        AirForecastDTO dto=AirForecastDTO.builder()
            .baseDate(date)
            .city(city)
            .forecast(forecast)
            .reliability(reliability)
            .build();
        return  dto;
    }

    // json 데이터를 weatherMidDto로 파싱
    private List<WeatherMidDTO> jsonWeatherParsing(String result) throws ParseException {
        List<WeatherMidDTO> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONArray dat = (JSONArray)jsonObject.get("data");
        int len = dat.size();
        for (int i = 0 ; i < len ; i++) {
            JSONObject data = (JSONObject)dat.get(i);
            String regionCode = (String)data.get("regionCode");
            String baseTime = (String)data.get("baseTime");
            String regionName = (String)data.get("regionName");
            String city = (String)data.get("city");
            Long tmn = (Long)data.get("tmn");
            Long tmx = (Long)data.get("tmx");
            Long ram = (Long)data.get("ram");
            Long rpm = (Long)data.get("rpm");
            String wam = (String)data.get("wam");
            String wpm = (String)data.get("wpm");

            WeatherMidDTO dto = WeatherMidDTO.builder()
                .regionCode(regionCode)
                .baseTime(baseTime)
                .regionName(regionName)
                .city(city)
                .tmn(tmn)
                .tmx(tmx)
                .rAm(ram)
                .rPm(rpm)
                .wAm(wam)
                .wPm(wpm)
                .build();

            list.add(dto);
        }
        return  list;
    }

    private List<WeatherMidDTO> getWeatherMid(LocalDate date) throws ParseException {
        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 날씨 중기예보 data 받아오기
        String result = restTemplate.getForObject(makeWeatherUrl(date),String.class);
        // Json 파싱해서 data 저장
        List<WeatherMidDTO> list = jsonWeatherParsing(result);
        log.info("날씨 중기예보 호출 데이터:{}", list);
        return list;
    }

    private AirForecastDTO getAirForecast(String city, LocalDate date) throws ParseException{
        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate으로 대기 주간예보 data 받아오기
        String result =  restTemplate.getForObject(makeAirUrl(date,city),String.class);
        // Json 파싱해서 data 저장
        AirForecastDTO airForecastDTO = jsonAirParsing(result);
        log.info("대기 주간예보 호출 데이터:{}",airForecastDTO);
        return airForecastDTO;
    }




    // 대기, 날씨 합친 데이터를 RankData로 변환하고 db에 저장
    @Override
    public ResultDTO<List<WeatherAirCompositeKeyDTO>> updateRankData(LocalDate date) {
        List<WeatherAirCompositeKeyDTO> list = new ArrayList<>();
        try {
            for ( int i = 0 ; i < 6 ; i++) {
                LocalDate searchDate = date.plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
                List<WeatherMidDTO> weatherList = getWeatherMid(searchDate);
                for ( WeatherMidDTO weatherDTO : weatherList) {
                    // 대기 받아오기
                    // 제주 데이터가 중기예보에 없어서 넘겨주기
                    if (weatherDTO.getCity().equals("제주")) continue;
                    AirForecastDTO airForecastDTO = getAirForecast(weatherDTO.getCity(), searchDate);
                    WeatherAirDataDTO dto = WeatherAirDataDTO.builder()
                        .regionCode(weatherDTO.getRegionCode())
                        .baseTime(airForecastDTO.getBaseDate())
                        .regionName(weatherDTO.getRegionName())
                        .city(weatherDTO.getCity())
                        .tmn(weatherDTO.getTmn())
                        .tmx(weatherDTO.getTmx())
                        .rAm(weatherDTO.getRAm())
                        .rPm(weatherDTO.getRPm())
                        .wAm(weatherDTO.getWAm())
                        .wPm(weatherDTO.getWPm())
                        .reliability(airForecastDTO.getReliability())
                        .forecast(airForecastDTO.getForecast())
                        .build();

                    log.info("날씨, 대기 데이터: {}",dto);
                    // WeatherAirDataDTO -> RankDataDTO
                    RankDataDTO rankDataDTO = rankService.makeRankData(dto);
                    // rankData DB에 저장
                    list.add(rankService.updateRankData(rankDataDTO));
                }
            }
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
