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

    /**
     * 지역 명 region과 시군구 명 sigungu에 해당하는 관광지 리스트를 DB에서 조회하여 List<RecommendTourDTO> 리턴합니다.
     *
     * @param region 지역 명
     * @param sigungu 시군구 명
     * @return 해당 지역과 시군구에 해당하는 관광지 리스트를 DB에서 조회하여 List<RecommendTourDTO> 리턴
     */
    private List<RecommendTourDTO> findTouristSpots(String region, String sigungu) {
        log.info("region : {}", region);
        log.info("sigungu: {}", sigungu);
        List<Integer[]> codes = regionCodeRepository.findBySigunguAndRegionCode(sigungu, region);

        Integer sigunguCode = codes.get(0)[0];
        Integer regionCode = codes.get(0)[1];

        // 관광지만 조회
        Long contentTypeId = 12L;
        List<TourEntity> spots = tourRepository.findByAreaCodeAndSigunguCodeAndContentTypeId(regionCode, sigunguCode, contentTypeId);
        List<RecommendTourDTO> tourDTOS = new ArrayList<>();
        int len = spots.size();
        for (int i = 0; i < len; i++) {
            TourEntity tourEntity = spots.get(i);
            RecommendTourDTO recommendTourDTO = RecommendTourDTO.builder()
                .title(tourEntity.getTitle())
                .addr(tourEntity.getAddr())
                .contentId(tourEntity.getContentId())
                .firstImage(tourEntity.getFirstImage())
                .contentTypeId(tourEntity.getContentTypeId())
                .tel(tourEntity.getTel())
                .zipcode(tourEntity.getZipcode())
                .latitude(tourEntity.getMapy())
                .longitude(tourEntity.getMapx())
                .build();

            tourDTOS.add(recommendTourDTO);
        }

        return tourDTOS;
    }


    /**
     * RankEntity를 RecommendRankDTO로 변환하여 RecommendRankDTO 리턴합니다.
     *
     * @param rankEntity 관광 순위 데이터 Entity(날씨 + 대기)
     * @return  RankEntity -> RecommendRankDTO로 변환하여 RecommendRankDTO 리턴
     */
    private RecommendRankDTO makeRecommendRankDTO(RankEntity rankEntity) {
        return RecommendRankDTO.builder()
            .HDGrade(rankEntity.getHDGrade())
            .pm10Grade(rankEntity.getPm10Grade())
            .HNGrade(rankEntity.getHNGrade())
            .pm25Grade(rankEntity.getPm25Grade())
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

    /**
     * 5개의 RankEntity DB에서 날씨 관광 지수와 대기 값을 바탕으로 5개의 관광 지역을 조회하여 ResultDTO<List<RecommendDTO>>를 리턴합니다.
     *
     * @return DB에서 관광지수와 대기로 조회한 5개의 추천 지역을 ResultDTO<List<RecommendDTO>> 리턴
     */
    @Override
    public ResultDTO<List<RecommendDTO>> getRecommendLocation() {
        LocalDate searchDate = LocalDate.now();

        // 5개 시군구 추천
        Pageable pageable = PageRequest.of(0, 5);
        List<RankEntity> locations = rankRepository.findByIdBaseDateOrderByTCIDesc(searchDate, pageable);
        log.info("추천 지역 : {}", locations);
        List<RecommendDTO> result = new ArrayList<>();
        int len = locations.size();
        for (int i = 0; i < len; i++) {
            RankEntity rankEntity = locations.get(i);
            List<RecommendTourDTO> tours = findTouristSpots(rankEntity.getId().getLevel1(), rankEntity.getId().getLevel2());
            RecommendRankDTO rankDTO = makeRecommendRankDTO(rankEntity);
            RecommendDTO recommendDTO = RecommendDTO.builder()
                .rankValue(rankDTO)
                .spots(tours)
                .build();
            result.add(recommendDTO);
        }

        return ResultDTO.of(HttpStatus.OK.value(), "5개의 시군구 지역을 추천하는데 성공하였습니다.", result);
    }
}
