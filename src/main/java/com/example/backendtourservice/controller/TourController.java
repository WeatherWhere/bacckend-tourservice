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

    @GetMapping("/save")
    public Object saveTourData() throws ParseException, org.json.simple.parser.ParseException {
        return tourService.saveTourData();
    }

    @GetMapping("/dbdata")
    public ResponseEntity<List<TourDTO>> getTourDBData(@RequestParam Integer areaCode, @RequestParam Integer sigunguCode, @RequestParam Long contentTypeId) {
        List<TourDTO> tourList = (List<TourDTO>) tourService.getTourDBData(areaCode, sigunguCode, contentTypeId);
        return ResponseEntity.ok(tourList);
    }

    @GetMapping("/detail")
    public Object getDetailInfo(@RequestParam Long contentId, @RequestParam Long contentTypeId) throws ParseException, org.json.simple.parser.ParseException {
        return tourService.getDetailInfo(contentId, contentTypeId);
    }

    @GetMapping("/common")
    public Object getCommonInfo(@RequestParam Long contentId, @RequestParam Long contentTypeId) throws ParseException, org.json.simple.parser.ParseException {
        return tourService.getCommonInfo(contentId, contentTypeId);
    }

    @GetMapping("/search")
    public ResultDTO<List<TourEntity>> getSearchData(@RequestParam Integer contentTypeId, @RequestParam Double x, @RequestParam Double y) {
        return searchService.getSearchData(contentTypeId, x, y);
    }
}
