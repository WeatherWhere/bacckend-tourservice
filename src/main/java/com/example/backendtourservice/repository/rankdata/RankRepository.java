package com.example.backendtourservice.repository.rankdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.domain.rankdata.RankCompositeKey;

public interface RankRepository extends JpaRepository<RankEntity, RankCompositeKey> {
}
