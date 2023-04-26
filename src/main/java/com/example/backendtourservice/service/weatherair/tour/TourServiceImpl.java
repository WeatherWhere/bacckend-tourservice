package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.tour.*;
import com.example.backendtourservice.repository.tour.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    /**
     * 지역기반 관광정보 Open API URL를 String으로 리턴하는 메서드
     *
     * @return url(지역기반 관광정보 Open API URL)
     */
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

    /**
     * 소개정보 Open API URL를  String으로 리턴하는 메서드
     *
     * @param contentid 콘텐츠 아이디
     * @param contenttypeid 콘텐츠타입 아이디
     * @return url2(소개정보 Open API URL)
     */
    private String makeUrl2(long contentid, long contenttypeid) {
        String BASE_URL = "http://apis.data.go.kr/B551011/KorService/detailIntro";
        String serviceKey = "?ServiceKey="+System.getProperty("TOUR_SERVICE_KEY");
        String numOfRows = "&numOfRows=10";
        String pageNo = "&pageNo=1";
        String MobileOS = "&MobileOS=ETC";
        String MobileApp = "&MobileApp=AppTest";
        String returnType = "&_type=json";
        String contentId = "&contentId=" + contentid;
        String contentTypeId = "&contentTypeId=" + contenttypeid;

        String url2= BASE_URL+serviceKey+numOfRows+pageNo+MobileOS+MobileApp+returnType+contentId+contentTypeId;
        return url2;
    }

    /**
     * 공통정보 Open API URL을 String으로 리턴하는 메서드
     *
     * @param contentid 콘텐츠 아이디
     * @param contenttypeid 콘텐츠타입 아이디
     * @return url3(공통정보 Open API URL)
     */
    private String makeUrl3(long contentid, long contenttypeid) {
        String BASE_URL = "http://apis.data.go.kr/B551011/KorService/detailCommon";
        String serviceKey = "?ServiceKey="+System.getProperty("TOUR_SERVICE_KEY");
        String numOfRows = "&numOfRows=10";
        String pageNo = "&pageNo=1";
        String MobileOS = "&MobileOS=ETC";
        String MobileApp = "&MobileApp=AppTest";
        String returnType = "&_type=json";
        String contentId = "&contentId=" + contentid;
        String contentTypeId = "&contentTypeId=" + contenttypeid;
        String defaultYN = "&defaultYN=Y";
        String overviewYN = "&overviewYN=Y";

        String url3= BASE_URL+serviceKey+numOfRows+pageNo+MobileOS+MobileApp+returnType+contentId+contentTypeId+defaultYN+overviewYN;
        return url3;
    }

    /**
     * Open API로부터 받아온 JSON 문자열을 JSON으로 파싱하는 메서드로 JSONArray로 리턴
     *
     * @param jsonString Open API를 통해 받아오는 JSON 형태의 문자열
     * @return response.body.items.item을 List로 리턴
     * @throws ParseException
     */
    private JSONArray jsonParser(String jsonString) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemList = (JSONArray) items.get("item");
        return itemList;
    }

    /**
     * makeUrl 메서드로 만들어진 url을 통해서 지역기반 광관정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱하고 리턴하는 메서드
     *
     * @return 지역기반 관광정보 result
     * @throws ParseException
     */
    @Override
    //지역기반 관광정보
    public Object getTourData() throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = makeUrl();
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        Object result = jsonParser(jsonString);
        return result;
    }

    /**
     * makeUrl2 메서드로 만들어진 url를 통해서 소개정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱,
     * 콘텐츠 아이디에 해당하는 관광 정보를 콘텐츠타입 아이디 타입에 따라 다른게 소개정보를 리턴
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return dto(콘텐츠타입에 따른 콘텐츠 아이디에 해당하는 관광지의 소개정보)
     * @throws ParseException
     */
    @Override
    public Object getDetailInfo(Long contentId, Long contentTypeId) throws ParseException {
        RestTemplate  restTemplate = new RestTemplate();
        String apiUrl = makeUrl2(contentId, contentTypeId);
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        JSONArray itemList = jsonParser(jsonString);
        JSONObject item = (JSONObject) itemList.get(0);

        Object dto = null;
        if (contentTypeId == 12) { //관광지
            dto = TouristSpotDTO.builder()
                    .chkBabyCarriage(parseEmptyString(item.get("chkbabycarriage")))
                    .chkCreditCard(parseEmptyString(item.get("chkcreditcard")))
                    .chkPet(parseEmptyString(item.get("chkpet")))
                    .expGuide(parseEmptyString(item.get("expguide")))
                    .infoCenter(parseEmptyString(item.get("infocenter")))
                    .parking(parseEmptyString(item.get("parking")))
                    .restDate(parseEmptyString(item.get("restdate")))
                    .useTime(parseEmptyString(item.get("usetime")))
                    .build();
        } else if (contentTypeId == 14) { //문화시설
             dto = CultureDTO.builder()
                    .chkBabyCarriageCulture(parseEmptyString(item.get("chkbabycarriageculture")))
                    .chkCreditCardCulture(parseEmptyString(item.get("chkcreditcardculture")))
                    .chkPetCulture(parseEmptyString(item.get("chkpetculture")))
                    .discountInfo(parseEmptyString(item.get("discountinfo")))
                    .infoCenterCulture(parseEmptyString(item.get("infocenterculture")))
                    .parkingCulture(parseEmptyString(item.get("parkingcluture")))
                    .parkingFee(parseEmptyString(item.get("parkingfee")))
                    .restDateCulture(parseEmptyString(item.get("restdateculture")))
                    .useFee(parseEmptyString(item.get("usefee")))
                    .useTimeCulture(parseEmptyString(item.get("usetimeculture")))
                    .build();
        } else if (contentTypeId == 32) { //숙박
            dto = LodgingDTO.builder()
                    .checkinTime(parseEmptyString(item.get("checkintime")))
                    .checkoutTime(parseEmptyString(item.get("checkouttime")))
                    .chkCooking(parseEmptyString(item.get("chkcooking")))
                    .foodPlace(parseEmptyString(item.get("foodplace")))
                    .infoCenterLodging(parseEmptyString(item.get("infocenterlodging")))
                    .parkingLodging(parseEmptyString(item.get("parkinglodging")))
                    .pickUp(parseEmptyString(item.get("pickup")))
                    .reservationLodging(parseEmptyString(item.get("reservationlodging")))
                    .reservationUrl(parseEmptyString(item.get("reservationurl")))
                    .roomType(parseEmptyString(item.get("roomtype")))
                    .subFacility(parseEmptyString(item.get("subfacility")))
                    .barbecue(parseInt(item.get("barbecue")))
                    .beauty(parseInt(item.get("beauty")))
                    .beverage(parseInt(item.get("beverage")))
                    .bicycle(parseInt(item.get("bicycle")))
                    .campfire(parseInt(item.get("campfire")))
                    .fitness(parseInt(item.get("fitness")))
                    .karaoke(parseInt(item.get("karaoke")))
                    .publicBath(parseInt(item.get("publicbath")))
                    .publicPc(parseInt(item.get("publicpc")))
                    .sauna(parseInt(item.get("sauna")))
                    .seminar(parseInt(item.get("seminar")))
                    .sports(parseInt(item.get("sports")))
                    .refundRegulation(parseEmptyString(item.get("refundregulation")))
                    .build();
        } else if (contentTypeId == 39) { //음식
            dto = FoodDTO.builder()
                    .chkCreditCardFood(parseEmptyString(item.get("chkcreditcardfood")))
                    .discountInfoFoof(parseEmptyString(item.get("discountinfofood")))
                    .firstMenu(parseEmptyString(item.get("firstmenu")))
                    .infoCenterFood(parseEmptyString(item.get("infocenterfood")))
                    .kidsFacility(parseInt(item.get("kidsfacility")))
                    .openTimeFood(parseEmptyString(item.get("opentimefood")))
                    .packing(parseEmptyString(item.get("packing")))
                    .parkingFood(parseEmptyString(item.get("parkingfood")))
                    .reservationFood(parseEmptyString(item.get("reservationfood")))
                    .restDateFood(parseEmptyString(item.get("restdatefood")))
                    .seat(parseEmptyString(item.get("seat")))
                    .smoking(parseEmptyString(item.get("smoking")))
                    .treatMenu(parseEmptyString(item.get("treatmenu")))
                    .lcnsno(Long.parseLong((String) item.get("lcnsno")))
                    .build();
        }
        return dto;
    }

    /**
     * 소개정보 builder 사용되는 메서드, Open API에서 데이터가 빈 값으로 올때 String으로 "정보가 없습니다." 리턴,
     * 빈 값이 아닐때는 받아온 데이토 값을 String으로 리턴
     *
     * @param value
     * @return value(데이터가 빈 값일 때는 "정보가 없습니다.", 빈값이 아닐때는 받아온 데이터의 값)
     */
    private String parseEmptyString(Object value) {
        if (value instanceof String) {
            String strValue = (String) value;
            if (strValue.isEmpty()) {
            return "정보가 없습니다.";
            } else {
                return (String) value;
            }
        }
        return (String) value;
    }

    /**
     * 소개정보 builder 사용되는 메서드, Open API에서 받아온 데이터가 1이면 String으로 "있음" 리턴,
     * 0이면 String으로 "없음" 리턴
     *
     * @param value
     * @return value(데이터가 1일 때는 "있음", 0일때는 "없음")
     */
    private String parseInt(Object value) {
        if (value instanceof String) {
            String strValue = (String) value;
            if (strValue == "1") {
                return "있음";
            } else {
                return "없음";
            }
        }
        return (String) value;
    }

    /**
     * makeUrl3 메서드로 만들어진 url를 통해서 공통정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱,
     * 콘텐츠 아이디와 콘텐츠타입 아이디에 따른 관광지의 공통정보를 CommonDTO 로 리턴
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return dto(콘텐츠 아이디와 콘텐츠타입 아이디에 따른 관광지의 공통정보 CommonDTO) 리턴
     * @throws ParseException
     */
    @Override
    public CommonDTO getCommonInfo(Long contentId, Long contentTypeId) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = makeUrl3(contentId, contentTypeId);
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        JSONArray itemList = jsonParser(jsonString);
        JSONObject item = (JSONObject) itemList.get(0);
        CommonDTO dto = CommonDTO.builder()
                .homePage((String) item.get("homepage"))
                .overView((String) item.get("overview"))
                .build();
        return dto;
    }

    /**
     * Open API를 통해서 받아온 데이터를 itemList의 수만큼 for문을 통해 DB에 저장,
     * 예외 발생시 숫자 데이터에는 0을 저장하고 문자열 데이터에는 값을 안넣고 저장하는 메서드
     *
     * @return DB에 저장을 완료했다는 "성공" 리턴
     * @throws ParseException
     */
    @Override
    public Object saveTourData() throws ParseException {
        JSONArray itemList = (JSONArray) getTourData();

        for (Object obj : itemList){
            try {
                JSONObject item = (JSONObject) obj;
                TourDTO dto = TourDTO.builder()
                        .contentId(Long.parseLong(item.get("contentid").toString()))
                        .contentTypeId(Long.parseLong(item.get("contenttypeid").toString()))
                        .areaCode(Integer.parseInt(item.get("areacode").toString()))
                        .sigunguCode(Integer.parseInt(item.get("sigungucode").toString()))
                        .mapx(Double.parseDouble(item.get("mapx").toString()))
                        .mapy(Double.parseDouble(item.get("mapy").toString()))
                        .title((String) item.get("title"))
                        .firstImage((String) item.get("firstimage"))
                        .addr((String) item.get("addr1"))
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
                        .contentId(item.get("contentid") != null && !item.get("contentid").toString().isEmpty() ? Long.parseLong(item.get("contentid").toString()) : 0L)
                        .contentTypeId(item.get("contentTypeid") != null && !item.get("contentTypeid").toString().isEmpty() ? Long.parseLong(item.get("contentTypeid").toString()) : 0L)
                        .areaCode(item.get("areacode") != null && !item.get("areacode").toString().isEmpty() ? Integer.parseInt(item.get("areacode").toString()) : 0)
                        .sigunguCode(item.get("sigungucode") != null && !item.get("sigungucode").toString().isEmpty() ? Integer.parseInt(item.get("sigungucode").toString()) : 0)
                        .mapx(item.get("mapx") != null && !item.get("mapx").toString().isEmpty() ? Double.parseDouble(item.get("mapx").toString()) : 0.0)
                        .mapy(item.get("mapy") != null && !item.get("mapy").toString().isEmpty() ? Double.parseDouble(item.get("mapy").toString()) : 0.0)
                        .title((String) item.get("title"))
                        .firstImage((String) item.get("firstimage"))
                        .addr((String) item.get("addr1"))
                        .tel((String) item.get("tel"))
                        .zipcode((String) item.get("zipcode"))
                        .build();
                TourEntity entity = ToEntity(dto);
                tourRepository.save(entity);
            }
        }
        return "성공";

    }

    /**
     * 지역코드, 시군구코드, 콘텐츠타입 아이디와 일치하는 관광 데이터를 DB에서 List<TourEntity> 로 리턴
     *
     * @param areaCode 지역코드
     * @param sigunguCode 시군구코드
     * @param contentTypeId 콘텐츠타입 아이디
     * @return tourList(관광 데이터 List<TourEntity>) 리턴
     */
    @Override
    public List<TourEntity> getTourDBData(Integer areaCode, Integer sigunguCode, Long contentTypeId) {
        List<TourEntity> tourList = tourRepository.findByAreaCodeAndSigunguCodeAndContentTypeId(areaCode, sigunguCode, contentTypeId);
        if (tourList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return tourList;
    }
}
