package com.example.backendtourservice.controller;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.tour.TourDTO;
import com.example.backendtourservice.service.weatherair.tour.SearchService;
import com.example.backendtourservice.service.weatherair.tour.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
    private final SearchService searchService;

    /**
     * DB에 Open API에서 받아온 데이터를 저장하고 DB에 저장했다는 메시지를 리턴합니다.
     *
     * @return DB에 저장을 성공했다는 메시지
     * @throws ParseException
     * @throws org.json.simple.parser.ParseException
     */
    @GetMapping("/save")
    public Object saveTourData() throws org.json.simple.parser.ParseException {
        return tourService.saveTourData();
    }

    /**
     * 지역코드, 시군구코드, 콘텐츠타입 아이디를 받아서 해당하는 관광 정보를 DB에서 가져와서,
     * ResponseEntity<List<TourDTO>> 로 리턴합니다.
     *
     * @param areaCode 지역코드
     * @param sigunguCode 시군구코드
     * @param contentTypeId 콘텐츠타입 아이디
     * @return 지역코드, 시군구코드, 콘텐츠타입 아이디를 받아서 해당하는 관광 정보를 리턴, 없을 시 예외처리
     */
    @GetMapping("/dbdata")
    public ResponseEntity<List<TourDTO>> getTourDBData(@RequestParam Integer areaCode, @RequestParam Integer sigunguCode, @RequestParam Long contentTypeId) {
        List<TourDTO> tourList = (List<TourDTO>) tourService.getTourDBData(areaCode, sigunguCode, contentTypeId);
        return ResponseEntity.ok(tourList);
    }

    /**
     * 콘텐츠 타입 아이디와 콘텐츠타입 아이디를 받아서 소개정보 Open API를 호출해 소개정보 데이터를 리턴합니다.
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return 소개정보 데이터
     * @throws org.json.simple.parser.ParseException
     */
    @GetMapping("/detail")
    public Object getDetailInfo(@RequestParam Long contentId, @RequestParam Long contentTypeId) throws org.json.simple.parser.ParseException {
        return tourService.getDetailInfo(contentId, contentTypeId);
    }

    /**
     * 콘텐츠 아이디와 콘텐츠타입 아이디를 받아서 공통정보 Open API를 호출해 공통정보를 리턴합니다.
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return 공통정보 데이터
     * @throws org.json.simple.parser.ParseException
     */
    @GetMapping("/common")
    public Object getCommonInfo(@RequestParam Long contentId, @RequestParam Long contentTypeId) throws org.json.simple.parser.ParseException {
        return tourService.getCommonInfo(contentId, contentTypeId);
    }

    /**
     * 콘텐츠타입 아이디와 위경도 x, y를 받아서 반경 10KM 내의 관광정보를 DB에서 받아옵니다.
     *
     * @param contentTypeId 콘텐츠타입 아이디
     * @param x 경도
     * @param y 위도
     * @return 좌표의 반경 10KM의 콘텐츠타입에 해당하는 관광 정보
     */
    @GetMapping("/search")
    public ResultDTO<List<TourEntity>> getSearchData(@RequestParam Integer contentTypeId, @RequestParam Double x, @RequestParam Double y) {
        return searchService.getSearchData(contentTypeId, x, y);
    }
}
