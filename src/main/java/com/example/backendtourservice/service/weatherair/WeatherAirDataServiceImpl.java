package com.example.backendtourservice.service.weatherair;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.weatherair.AirForecastDTO;
import com.example.backendtourservice.dto.weatherair.WeatherAirDataDTO;
import com.example.backendtourservice.dto.weatherair.WeatherMidDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherAirDataServiceImpl implements WeatherAirDataSerivce {

    // 해당 지역, 예보 날짜 Air api 대기 주간예보 url
    private String makeAirUrl(LocalDate date,String city) {
        String baseUrl="http://localhost:8090/air/forecast/tour/data?";
        String baseDate="baseDate="+date;
        String region="&city="+city;

        return baseUrl+baseDate+region;
    }

    // 해당 지역, 예보 날짜 Air api 대기 주간예보 url
    private String makeWeatherUrl(String regionCode, LocalDate date) {
        // weather db에 BaseDate 형식:"20231011"이므로 LocalDate -> String으로 바꿔주는 과정 필요
        String sDate=date+"";
        sDate=sDate.replaceAll("-","");
        String baseUrl="http://localhost:8080/weather/tour/data?";
        String region="regionCode="+regionCode;
        String baseDate="&baseDate="+sDate;

        return baseUrl+region+baseDate;
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
    private WeatherMidDTO jsonWeatherParsing(String result) throws ParseException {
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject)jsonParser.parse(result);
        JSONObject data=(JSONObject)jsonObject.get("data");
        String regionCode=(String)data.get("regionCode");
        String baseTime=(String)data.get("baseTime");
        String regionName=(String)data.get("regionName");
        String city=(String)data.get("city");
        Long tmn=(Long)data.get("tmn");
        Long tmx=(Long)data.get("tmx");
        Long ram=(Long)data.get("ram");
        Long rpm=(Long)data.get("rpm");
        String wam=(String)data.get("wam");
        String wpm=(String)data.get("wpm");

        WeatherMidDTO dto=WeatherMidDTO.builder()
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

        return  dto;
    }

    private WeatherMidDTO getWeatherMid(String regionCode, LocalDate date) throws ParseException {
        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 날씨 중기예보 data 받아오기
        String result=restTemplate.getForObject(makeWeatherUrl(regionCode,date),String.class);
        // Json 파싱해서 data 저장
        WeatherMidDTO weatherMidDTO=jsonWeatherParsing(result);
        log.info("날씨 중기예보 호출 데이터:{}", weatherMidDTO);
        return weatherMidDTO;
    }

    private AirForecastDTO getAirForecast(String city, LocalDate date) throws ParseException{
        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 대기 주간예보 data 받아오기
        String result=  restTemplate.getForObject(makeAirUrl(date,city),String.class);
        // Json 파싱해서 data 저장
        AirForecastDTO airForecastDTO=jsonAirParsing(result);

        log.info("대기 주간예보 호출 데이터:{}",airForecastDTO);
        return airForecastDTO;

    }


    // 대기, 날씨 합친 데이터
    @Override
    public List<WeatherAirDataDTO> updateWeatherAirData(String regionCode, LocalDate date) {
        try{
            List<WeatherAirDataDTO> list=new ArrayList<>();
            for(int i=0; i<6; i++){
                LocalDate searchDate=date.plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
                WeatherMidDTO weatherMidDTO=getWeatherMid(regionCode,searchDate);
                // 대기 받아오기
                AirForecastDTO airForecastDTO=getAirForecast(weatherMidDTO.getCity(), searchDate);
                WeatherAirDataDTO dto=WeatherAirDataDTO.builder()
                    .regionCode(weatherMidDTO.getRegionCode())
                    .baseTime(airForecastDTO.getBaseDate())
                    .regionName(weatherMidDTO.getRegionName())
                    .city(weatherMidDTO.getCity())
                    .tmn(weatherMidDTO.getTmn())
                    .tmx(weatherMidDTO.getTmx())
                    .rAm(weatherMidDTO.getRAm())
                    .rPm(weatherMidDTO.getRPm())
                    .wAm(weatherMidDTO.getWAm())
                    .wPm(weatherMidDTO.getWPm())
                    .reliability(airForecastDTO.getReliability())
                    .forecast(airForecastDTO.getForecast())
                    .build();
                log.info("날씨, 대기 데이터: {}",dto);
                list.add(dto);
            }
            return list;
        }catch (ParseException e){
            // json 데이터 파싱할 때 error
            e.printStackTrace();
            log.error("ParseException이 발생");
            //return ResultDTO.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ParseException이 발생했습니다.", null);
            // 수정해야함
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            log.error("예기치 못한 에러가 발생");
           // return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
            return null;
        }
    }
}
