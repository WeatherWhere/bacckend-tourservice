package com.example.backendtourservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendtourservice.domain.weatherair.WeatherAirDataCompositeKey;
import com.example.backendtourservice.domain.weatherair.WeatherAirDataEntity;

public interface WeatherAirDataRepository extends JpaRepository<WeatherAirDataEntity, WeatherAirDataCompositeKey> {
}
