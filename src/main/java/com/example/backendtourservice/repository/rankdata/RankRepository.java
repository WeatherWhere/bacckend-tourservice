package com.example.backendtourservice.repository.rankdata;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backendtourservice.domain.rankdata.RankCompositeKey;
import com.example.backendtourservice.domain.rankdata.RankEntity;

@Repository
public interface RankRepository extends JpaRepository<RankEntity, RankCompositeKey> {

    /**
     *  해당 예보날짜와 원하는 조회 데이터 개수만큼의 TCI 지수를 내림차순으로 조회한 값 중에 미세먼지와 초미세먼지 농도가 매우 나쁨이 아닌 범주를 조회하여  List<RankEntity>를 리턴합니다.
     *
     * @param baseDate 예보 날짜
     * @param pageable 조회 데이터 개수
     * @return 추천 지역 리스트 List<RankEntity> 리턴
     */
    // 시군구로 지역 추천 조회
    @Query("SELECT r FROM RankEntity r WHERE r.id.baseDate = :baseDate AND r.pm10Value BETWEEN 0 AND 150 AND r.pm25Value BETWEEN 0 AND 75 ORDER BY r.TCI DESC")
    List<RankEntity> findByIdBaseDateOrderByTCIDesc(LocalDate baseDate, Pageable pageable);

}
