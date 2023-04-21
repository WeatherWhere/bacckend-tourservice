package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;

import java.util.List;

public interface SearchService {
    ResultDTO<List<TourEntity>> getSearchData(int contentTypeId, double x, double y);
}
