package com.example.backendtourservice.controller;

import com.example.backendtourservice.service.weatherair.tour.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @GetMapping("/api")
    public Object saveTourData() throws ParseException, org.json.simple.parser.ParseException {
        return tourService.saveTourData();
    }
}
