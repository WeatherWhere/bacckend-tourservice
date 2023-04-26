package com.example.backendtourservice.repository.rankdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backendtourservice.domain.tour.RegionCodeEntity;

@Repository
public interface RegionCodeRepository extends JpaRepository<RegionCodeEntity, Long> {
    /**
     * 시군구 명과 지역 명으로 관광 DB에서 사용하는 시군구 코드와 지역 코드 조회하여 List<Integer[]>를 리턴합니다.
     *
     * @param sigunguValue 시군구 명
     * @param regionValue 지역 명
     * @return 해당 이름에 해당하는 시군구 코드와 지역 코드를 포함한 List<Integer[]> 리턴
     */
    @Query("SELECT r.sigunguCode, r.regionCode FROM RegionCodeEntity r WHERE r.sigungu = :sigunguValue AND r.region = :regionValue")
    List<Integer[]> findBySigunguAndRegionCode(@Param("sigunguValue") String sigunguValue, @Param("regionValue") String regionValue);
}
