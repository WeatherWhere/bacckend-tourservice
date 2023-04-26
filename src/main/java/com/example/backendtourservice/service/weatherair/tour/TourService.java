package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.tour.TourDTO;

public interface TourService {

    /**
     * makeUrl 메서드로 만들어진 url을 통해서 지역기반 광관정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱하고 리턴하는 메서드
     *
     * @return 지역기반 관광정보 result
     * @throws org.json.simple.parser.ParseException
     */
    Object getTourData() throws org.json.simple.parser.ParseException;

    /**
     * Open API를 통해서 받아온 데이터를 itemList의 수만큼 for문을 통해 DB에 저장,
     * 예외 발생시 숫자 데이터에는 0을 저장하고 문자열 데이터에는 값을 안넣고 저장하는 메서드
     *
     * @return DB에 저장을 완료했다는 "성공" 리턴
     * @throws org.json.simple.parser.ParseException
     */
    Object saveTourData() throws org.json.simple.parser.ParseException;

    /**
     * 지역코드, 시군구코드, 콘텐츠타입 아이디와 일치하는 관광 데이터를 DB에서 List<TourEntity> 로 리턴
     *
     * @param areaCode 지역코드
     * @param sigunguCode 시군구코드
     * @param contentTypeId 콘텐츠타입 아이디
     * @return tourList(관광 데이터 List<TourEntity>) 리턴
     */
    Object getTourDBData(Integer areaCode, Integer sigunguCode, Integer contentTypeId);

    /**
     * makeUrl2 메서드로 만들어진 url를 통해서 소개정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱,
     * 콘텐츠 아이디에 해당하는 관광 정보를 콘텐츠타입 아이디 타입에 따라 다른게 소개정보를 리턴
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return dto(콘텐츠타입에 따른 콘텐츠 아이디에 해당하는 관광지의 소개정보)
     * @throws org.json.simple.parser.ParseException
     */
    Object getDetailInfo(Long contentId, Integer contentTypeId) throws org.json.simple.parser.ParseException;

    /**
     * makeUrl3 메서드로 만들어진 url를 통해서 공통정보 Open API를 호출하고 jsonParser 메서드를 통해서 파싱,
     * 콘텐츠 아이디와 콘텐츠타입 아이디에 따른 관광지의 공통정보를 CommonDTO 로 리턴
     *
     * @param contentId 콘텐츠 아이디
     * @param contentTypeId 콘텐츠타입 아이디
     * @return dto(콘텐츠 아이디와 콘텐츠타입 아이디에 따른 관광지의 공통정보 CommonDTO) 리턴
     * @throws org.json.simple.parser.ParseException
     */
    Object getCommonInfo(Long contentId, Integer contentTypeId) throws org.json.simple.parser.ParseException;


    //Dto -> Entity 메서드
    default TourEntity ToEntity(TourDTO dto) {
        TourEntity entity = TourEntity.builder()
                .contentId(dto.getContentId())
                .contentTypeId(dto.getContentTypeId())
                .areaCode(dto.getAreaCode())
                .sigunguCode(dto.getSigunguCode())
                .mapx(dto.getMapx())
                .mapy(dto.getMapy())
                .title(dto.getTitle())
                .firstImage(dto.getFirstImage())
                .addr(dto.getAddr())
                .tel(dto.getTel())
                .zipcode(dto.getZipcode())
                .build();
        return entity;
    }

    //Entity -> Dto 메서드
    default TourDTO ToDto(TourEntity entity) {
        TourDTO dto = TourDTO.builder()
                .contentId(entity.getContentId())
                .contentTypeId(entity.getContentTypeId())
                .areaCode(entity.getAreaCode())
                .sigunguCode(entity.getSigunguCode())
                .mapx(entity.getMapx())
                .mapy(entity.getMapy())
                .title(entity.getTitle())
                .firstImage(entity.getFirstImage())
                .addr(entity.getAddr())
                .tel(entity.getTel())
                .zipcode(entity.getZipcode())
                .build();
        return dto;
    }
}


