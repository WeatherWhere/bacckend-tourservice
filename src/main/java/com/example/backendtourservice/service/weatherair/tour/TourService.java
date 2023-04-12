package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.tour.TourDTO;

import java.text.ParseException;

public interface TourService {
    Object getTourData() throws ParseException, org.json.simple.parser.ParseException;

    Object saveTourData() throws ParseException, org.json.simple.parser.ParseException;

    //Dto -> Entity 메서드
    default TourEntity ToEntity(TourDTO dto) {
        TourEntity entity = TourEntity.builder()
                .contentid(dto.getContentid())
                .contenttypeid(dto.getContenttypeid())
                .areacode(dto.getAreacode())
                .mapx(dto.getMapx())
                .mapy(dto.getMapy())
                .title(dto.getTitle())
                .firstimage(dto.getFirstimage())
                .tel(dto.getTel())
                .zipcode(dto.getZipcode())
                .build();
        return entity;
    }

    //Entity -> Dto 메서드
    default TourDTO ToDto(TourEntity entity) {
        TourDTO dto = TourDTO.builder()
                .contentid(entity.getContentid())
                .contenttypeid(entity.getContenttypeid())
                .areacode(entity.getAreacode())
                .mapx(entity.getMapx())
                .mapy(entity.getMapy())
                .title(entity.getTitle())
                .firstimage(entity.getFirstimage())
                .tel(entity.getTel())
                .zipcode(entity.getZipcode())
                .build();
        return dto;
    }
}


