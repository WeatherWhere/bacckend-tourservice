package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;

import java.util.List;

public interface SearchService {
    /**
     * 입력받은 위경도 x, y의 위치에서 반경 10KM의 콘턴츠타입 아이디에 해당하는 관광 정보를 ResultDTO<List<TourEntity>> 로 리턴
     *
     * @param contentTypeId 콘텐츠타입 아이디
     * @param x 경도
     * @param y 위도
     * @return 콘턴츠타입 아이디에 해당하는 관광 정보를 ResultDTO<List<TourEntity>> 로 리턴
     */
    ResultDTO<List<TourEntity>> getSearchData(int contentTypeId, double x, double y);
}
