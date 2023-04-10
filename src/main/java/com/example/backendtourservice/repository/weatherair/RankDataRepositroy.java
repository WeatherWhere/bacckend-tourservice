package com.example.backendtourservice.repository.weatherair;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backendtourservice.domain.weatherair.RankDataEntity;
import com.example.backendtourservice.domain.weatherair.WeatherAirDataCompositeKey;

public interface RankDataRepositroy extends JpaRepository<RankDataEntity, WeatherAirDataCompositeKey> {
    Optional<RankDataEntity> findById(WeatherAirDataCompositeKey weatherAirDataCompositeKey);

    List<RankDataEntity> findByIdBaseTime(LocalDate baseTime);

    // 해당 날짜의 기온 rank 데이터 최솟값
    @Query("SELECT MIN(r.temperature) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    Double findMinValueOfTemperatureByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 기온 rank 데이터 최댓값
    @Query("SELECT MAX (r.temperature) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    Double findMaxValueOfTemperatureByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 대기 rank 데이터 최솟값
    @Query("SELECT MIN(r.air) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMinValueOfAirByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 대기 rank 데이터 최댓값
    @Query("SELECT MAX(r.air) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMaxValueOfAirByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오전 강수 rank 데이터 최솟값
    @Query("SELECT MIN(r.rAm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMinValueOfRAmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오전 강수 rank 데이터 최댓값
    @Query("SELECT MAX(r.rAm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMaxValueOfRAmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오후 강수 rank 데이터 최솟값
    @Query("SELECT MIN(r.rPm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMinValueOfRPmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오후 강수 rank 데이터 최댓값
    @Query("SELECT MAX(r.rPm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMaxValueOfRPmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오전 날씨 rank 데이터 최솟값
    @Query("SELECT MIN(r.wAm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMinValueOfWAmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오전 날씨 rank 데이터 최댓값
    @Query("SELECT MAX(r.wAm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMaxValueOfWAmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오후 날씨 rank 데이터 최솟값
    @Query("SELECT MIN(r.wPm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMinValueOfWPmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // 해당 날짜의 오후 날씨 rank 데이터 최댓값
    @Query("SELECT MAX(r.wPm) FROM RankDataEntity r WHERE r.id.baseTime = :baseTime")
    int findMaxValueOfWPmByBaseTime(@Param("baseTime")LocalDate baseTime);

    // rankValue 속성만 업데이트 해주기
    @Modifying
    @Query("UPDATE RankDataEntity r SET r.rankValue =?1 WHERE r.id =?2")
    void updateRankValueById(double rankValue, WeatherAirDataCompositeKey id);
}
