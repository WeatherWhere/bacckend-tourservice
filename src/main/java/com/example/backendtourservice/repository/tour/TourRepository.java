package com.example.backendtourservice.repository.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.tour.TourDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<TourEntity, Long> {
    List<TourEntity> findByAreaCodeAndSigunguCodeAndContentTypeId(Integer areaCode, Integer sigunguCode, Long contentTypeId);
}
