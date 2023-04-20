package com.example.backendtourservice.repository.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<TourEntity, Long> {
    List<TourEntity> findByAreaCodeAndSigunguCodeAndContentTypeId(Integer areaCode, Integer sigunguCode, Long contentTypeId);

    List<TourEntity> findBySigunguCodeAndAreaCode(Integer sigunguCode, Integer areaCode);


    @Query(value = "SELECT * FROM tour.tour_spot WHERE ST_DWithin("
            + "(ST_MakePoint(CAST(location_x AS double precision), CAST(location_y AS double precision))), "
            + "ST_SetSRID(ST_MakePoint(?, ?), 4326), "
            + "20000, true)", nativeQuery = true)
    List<TourEntity> findBySql(double x, double y);

}
