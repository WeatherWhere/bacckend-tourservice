package com.example.backendtourservice.repository.rankdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backendtourservice.domain.tour.RegionCodeEntity;

public interface RegionCodeRepository extends JpaRepository<RegionCodeEntity, Long> {
    @Query("SELECT r.sigunguCode, r.regionCode FROM RegionCodeEntity r WHERE r.sigungu = :sigunguValue AND r.region = :regionValue")
    List<Integer[]> findBySigunguAndRegionCode(@Param("sigunguValue") String sigunguValue, @Param("regionValue") String regionValue);

    List<RegionCodeEntity> findAll();

}
