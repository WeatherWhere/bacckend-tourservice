package com.example.backendtourservice.service.weatherair.tour;
import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.repository.tour.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final TourRepository tourRepository;

    /**
     * 입력받은 위경도 x, y의 위치에서 반경 10KM의 콘턴츠타입 아이디에 해당하는 관광 정보를 ResultDTO<List<TourEntity>> 로 리턴
     *
     * @param contentTypeId 콘텐츠타입 아이디
     * @param x 경도
     * @param y 위도
     * @return 콘턴츠타입 아이디에 해당하는 관광 정보를 ResultDTO<List<TourEntity>> 로 리턴
     */
    @Override
    public ResultDTO<List<TourEntity>> getSearchData(int contentTypeId, double x, double y) {
        return ResultDTO.of(HttpStatus.OK.value(), "반경 10KM 내의 관광지 검색결과", tourRepository.findBySql(contentTypeId, x, y));
    }
}


