package com.example.backendtourservice.repository.rankdata;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backendtourservice.domain.rankdata.RankCompositeKey;
import com.example.backendtourservice.domain.rankdata.RankEntity;


public interface RankRepository extends JpaRepository<RankEntity, RankCompositeKey> {

    // 시군구로 지역 추천 조회
    @Query("SELECT r FROM RankEntity r WHERE r.id.baseDate = :baseDate ORDER BY r.TCI DESC")
    List<RankEntity> findByIdBaseDateOrderByTCIDesc(LocalDate baseDate, Pageable pageable);

}
