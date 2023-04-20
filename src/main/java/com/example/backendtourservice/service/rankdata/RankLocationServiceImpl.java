package com.example.backendtourservice.service.rankdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.backendtourservice.domain.rankdata.RankEntity;
import com.example.backendtourservice.domain.tour.RegionCodeEntity;
import com.example.backendtourservice.domain.tour.TourEntity;
import com.example.backendtourservice.dto.ResultDTO;
import com.example.backendtourservice.dto.rankdata.RecommendDTO;
import com.example.backendtourservice.dto.rankdata.RecommendRankDTO;
import com.example.backendtourservice.dto.rankdata.RecommendTourDTO;
import com.example.backendtourservice.repository.rankdata.RankRepository;
import com.example.backendtourservice.repository.rankdata.RegionCodeRepository;
import com.example.backendtourservice.repository.tour.TourRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class RankLocationServiceImpl implements RankLocationService {

    private final RankRepository rankRepository;
    private final RegionCodeRepository regionCodeRepository;
    private final TourRepository tourRepository;

    // 시군구에 해당하는 관광지 찾기
    private List<RecommendTourDTO> findTouristSpots(String region, String sigungu) {
        log.info("region : {}", region);
        log.info("sigungu: {}", sigungu);
        List<Integer[]> codes = regionCodeRepository.findBySigunguAndRegionCode(sigungu, region);
        List<RegionCodeEntity> list = regionCodeRepository.findAll();

        Integer sigunguCode = codes.get(0)[0];
        Integer regionCode = codes.get(0)[1];
        List<TourEntity> spots = tourRepository.findBySigunguCodeAndAreaCode(sigunguCode, regionCode);
        List<RecommendTourDTO> tourDTOS = new ArrayList<>();
        int len = spots.size();
        for (int i = 0; i < len; i++) {
            TourEntity tourEntity = spots.get(i);
            RecommendTourDTO recommendTourDTO = RecommendTourDTO.builder()
                .title(tourEntity.getTitle())
                .addr(tourEntity.getAddr())
                .contentId(tourEntity.getContentId())
                .firstImage(tourEntity.getFirstImage())
                .latitude(tourEntity.getMapy())
                .longitude(tourEntity.getMapx())
                .build();
            tourDTOS.add(recommendTourDTO);
        }

        return tourDTOS;
    }

    private RecommendRankDTO makeRecommendRankDTO(RankEntity rankEntity) {
        return RecommendRankDTO.builder()
            .HDGrade(rankEntity.getHDGrade())
            .pm10Grade(rankEntity.getPm10Grade())
            .HNGrade(rankEntity.getHNGrade())
            .pm25Grade(rankEntity.getPm25Grade())
            .pm25Value(rankEntity.getPm10Value())
            .pm10Value(rankEntity.getPm10Value())
            .RGrade(rankEntity.getRGrade())
            .TCI(rankEntity.getTCI())
            .SIGrade(rankEntity.getSIGrade())
            .baseDate(rankEntity.getId().getBaseDate())
            .TCIGrade(rankEntity.getTCIGrade())
            .WGrade(rankEntity.getWGrade())
            .region(rankEntity.getId().getLevel1())
            .sigungu(rankEntity.getId().getLevel2())
            .longitude(rankEntity.getLocationX())
            .latitude(rankEntity.getLocationY())
            .build();
    }

    // 시군구 지역 추천 조회
    @Override
    public ResultDTO<List<RecommendDTO>> getRecommendLocation() {
        LocalDate searchDate = LocalDate.now();

        // 10개 시군구 추천
        Pageable pageable = PageRequest.of(0, 5);
        List<RankEntity> locations = rankRepository.findByIdBaseDateAndIdLevel2IsNotNullOrderByTCIDesc(searchDate, pageable);
        List<RecommendDTO> result = new ArrayList<>();
        int len = locations.size();
        for (int i = 0; i < len; i++) {
            RankEntity rankEntity = locations.get(i);
            if (rankEntity.getId().getLevel2().length() == 0) continue;
            List<RecommendTourDTO> tours = findTouristSpots(rankEntity.getId().getLevel1(), rankEntity.getId().getLevel2());
            RecommendRankDTO rankDTO = makeRecommendRankDTO(rankEntity);
            RecommendDTO recommendDTO = RecommendDTO.builder()
                .rankValue(rankDTO)
                .spots(tours)
                .build();
            result.add(recommendDTO);
        }

        return ResultDTO.of(HttpStatus.OK.value(), "10개의 시군구 지역을 추천하는데 성공하였습니다.", result);
    }
}
