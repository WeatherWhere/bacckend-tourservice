package com.example.backendtourservice.service.weatherair.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.tour.TourDTO;

import java.text.ParseException;
import java.util.List;

public interface TourService {
    Object getTourData() throws ParseException, org.json.simple.parser.ParseException;

    Object saveTourData() throws ParseException, org.json.simple.parser.ParseException;

    Object getTourDBData(Integer areaCode, Integer sigunguCode, Long contenttypeId);

    Object getDetailInfo(Long contentId, Long contentTypeId) throws ParseException, org.json.simple.parser.ParseException;

    Object getCommonInfo(Long contentId, Long contentTypeId) throws org.json.simple.parser.ParseException;


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


