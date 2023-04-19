package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;

import java.util.List;

public interface SearchService {
    ResultDTO<List<TourEntity>> getSearchData(double x, double y);
}
