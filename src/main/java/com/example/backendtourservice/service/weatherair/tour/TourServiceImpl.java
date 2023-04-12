package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.tour.TourDTO;
import com.example.backendtourservice.repository.tour.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    // 공공데이터 api url
    private String makeUrl() {
        String BASE_URL = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1";
        String serviceKey = "?ServiceKey="+System.getProperty("TOUR_SERVICE_KEY");
        String numOfRows = "&numOfRows=50214";
        String pageNo = "&pageNo=1";
        String MobileOS = "&MobileOS=ETC";
        String MobileApp = "&MobileApp=AppTest";
        String returnType = "&_type=json";
        String listYN = "&listYN=Y";
        String arrange = "&arrange=A";

        String url= BASE_URL+serviceKey+numOfRows+pageNo+MobileOS+MobileApp+returnType+listYN+arrange;
        return url;
    }

    //JSON 파싱
    private JSONArray jsonParser(String jsonString) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemList = (JSONArray) items.get("item");
        return itemList;
    }

    @Override
    //Open API에서 데이터 받아오고 파싱
    public Object getTourData() throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = makeUrl();
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        Object result = jsonParser(jsonString);
        return result;
    }

    @Override
    //@Transactional
    public Object saveTourData() throws java.text.ParseException, ParseException {
        JSONArray itemList = (JSONArray) getTourData();
        for (Object obj : itemList){
            try {
                JSONObject item = (JSONObject) obj;
                TourDTO dto = TourDTO.builder()
                        .contentid(Long.parseLong(item.get("contentid").toString()))
                        .contenttypeid(Long.parseLong(item.get("contenttypeid").toString()))
                        .areacode(Integer.parseInt(item.get("areacode").toString()))
                        .mapx(Double.parseDouble(item.get("mapx").toString()))
                        .mapy(Double.parseDouble(item.get("mapy").toString()))
                        .title((String) item.get("title"))
                        .firstimage((String) item.get("firstimage"))
                        .tel((String) item.get("tel"))
                        .zipcode((String) item.get("zipcode"))
                        .build();
                TourEntity entity = ToEntity(dto);
                tourRepository.save(entity);

            } catch (NumberFormatException e) {
                // 에러에 대한 로그 표시 및 null 값이나 빈 문자열("")이 오면 숫자 데이터는 0을 넣고 문자열 데이터는 값을 안넣음
                e.printStackTrace();
                log.error("Invalid number format for tour data: {}", e.getMessage());
                JSONObject item = (JSONObject) obj;
                TourDTO dto = TourDTO.builder()
                        .contentid(item.get("contentid") != null && !item.get("contentid").toString().isEmpty() ? Long.parseLong(item.get("contentid").toString()) : 0L)
                        .contenttypeid(item.get("contenttypeid") != null && !item.get("contenttypeid").toString().isEmpty() ? Long.parseLong(item.get("contenttypeid").toString()) : 0L)
                        .areacode(item.get("areacode") != null && !item.get("areacode").toString().isEmpty() ? Integer.parseInt(item.get("areacode").toString()) : 0)
                        .mapx(item.get("mapx") != null && !item.get("mapx").toString().isEmpty() ? Double.parseDouble(item.get("mapx").toString()) : 0.0)
                        .mapy(item.get("mapy") != null && !item.get("mapy").toString().isEmpty() ? Double.parseDouble(item.get("mapy").toString()) : 0.0)
                        .title((String) item.get("title"))
                        .firstimage((String) item.get("firstimage"))
                        .tel((String) item.get("tel"))
                        .zipcode((String) item.get("zipcode"))
                        .build();
                TourEntity entity = ToEntity(dto);
                tourRepository.save(entity);
            }
        }
        return "성공";
    }
}
