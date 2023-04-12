package com.example.backendtourservice.repository.tour;

import com.example.backendtourservice.domain.tour.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<TourEntity, Long> {
}
