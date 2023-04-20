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

    @Override
    public ResultDTO<List<TourEntity>> getSearchData(double x, double y) {
        return ResultDTO.of(HttpStatus.OK.value(), "반경 20KM 내의 관광지 검색결과", tourRepository.findBySql(x, y));
    }
}


