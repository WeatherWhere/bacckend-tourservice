package com.example.backendtourservice.service.rankdata;

import org.springframework.stereotype.Service;

import com.example.backendtourservice.dto.rankdata.RankDTO;
import com.example.backendtourservice.dto.rankdata.RankWeatherDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalculateTCIServiceImpl implements CalculateTCIService {

    // 열쾌적성 지수
    private Double calculateHI(Double T, Double RH) {
        // 섭씨 온도 -> 화씨 온도
        T = T * 1.8 + 32;

        Double HI = 0.0;
        if (T <= 80) {
            HI = 0.5 * (T + 61 + ((T - 68) * 1.2) + (RH * 0.094));
        } else if (T > 80) {
            HI = (-42.379) + (2.04901523 * T) + (10.14333127 * RH) - (0.22475541 * T * RH) - (0.00683783 * T * T)
                - (0.05481717 * RH * RH) + (0.00122874 * T * T * RH) + (0.00085282 * T * RH * RH)
                - (0.00000199 * T * T * RH * RH);

            if (RH < 13 && T < 87) {
                HI -= ((13 - RH) / 4) * Math.sqrt(17 - Math.abs(95 - T) / 17);
            } else if (RH > 85 && T < 87) {
                HI += ((RH - 85) / 10) * ((87 - T) / 5);
            }
        }
        // 화씨를 섭씨로 변환해서 return
        return (HI - 32) * 5 / 9;
    }

    private Double getHNOrHD(Double HI) {
        Double result = 0.0;

        // 20도 이하일 때 임의로 설정
        if(HI >= 5 && HI < 20) result = 3.0;
        else if(HI >= 0 && HI < 5) result = 0.3;
        else if(HI < 0) result = -3.0;

        // 날씨마루 공식 참고
        if (20.0 < HI && HI <= 27.0) result = 5.0;
        else if (27.0 < HI && HI <= 28.0) result = 4.5;
        else if (28.0 < HI && HI <= 29.0) result = 4.0;
        else if (29.0 < HI && HI <= 30.0) result = 3.5;
        else if (30.0 < HI && HI <= 31.0) result = 3.0;
        else if (31.0 < HI && HI <= 32.0) result = 2.5;
        else if (32.0 < HI && HI <= 33.0) result = 2.0;
        else if (33.0 < HI && HI <= 34.0) result = 1.5;
        else if (34.0 < HI && HI <= 35.0) result = 1.0;
        else if (35.0 < HI && HI <= 36.0) result = 0.3;
        else if (36.0 < HI && HI <= 41.0) result = 0.0;
        else if (41.0 < HI && HI <= 47.0) result = -1.0;
        else if (47.0 < HI && HI <= 54.0) result = -2.0;
        else if (54.0 < HI) result = -3.0;

        return result;
    }

    private String[] getHNAndHDGrade(Double HN, Double HD) {
        String [] result = new String[2];
        // HN
         if (4.0 <= HN && HN <= 5.0) result[0] = "좋음";
         else if (2.5 <= HN && HN <= 3.5) result[0] = "보통";
         else if (-3 <= HN && HN <= 2.0) result[0] = "나쁨";

         // HD
        if (3.5 <= HD && HD <= 5.0) result[1] = "좋음";
        else if (2.0 <= HD && HD<= 3.0) result[1] = "보통";
        else if (-3 <= HD && HD <= 1.5) result[1] = "나쁨";

        return result;
    }

    // 바람 지수 W 구하기
    private Double getW(Double WS) {
        Double result = 0.0;

        if (WS <= 0.8) result = 5.0;
        else if (0.8 < WS && WS <= 1.6) result = 4.5;
        else if (1.6 < WS && WS <= 2.51) result = 4.0;
        else if (2.51 < WS && WS <= 3.4) result = 3.5;
        else if (3.4 < WS && WS <= 5.5) result = 3.0;
        else if (5.5 < WS && WS <= 6.75) result = 2.5;
        else if (6.75 < WS && WS <= 8.0) result = 2.0;
        else if (8.0 < WS && WS <= 10.7) result = 1.0;
        else if (10.7 < WS) result = 0.0;

        return result;
    }

    // 바람지수 W 등급
    private String getWGrade(Double W) {
        String result = "";

        if (4.5 <= W && W <= 5.0) result = "좋음";
        else if (W == 4) result = "보통";
        else if (0 <= W && W <= 3.5) result = "나쁨";

        return result;
    }

    // 강수 R 구하기
    private Double getR(Double R) {
        Double result = 0.0;

        if (R == 0.0) result = 5.0;
        else if (0.0 < R && R <= 1.0) result = 4.0;
        else if (1.0 < R && R <= 5.0) result = 3.0;
        else if (5.0 < R && R <= 10.0) result = 2.5;
        else if (10.0 < R && R <= 15.0) result = 1.5;
        else if (15.0 < R && R <= 20.0) result = 1.0;
        else if (20.0 < R && R <= 25.0) result = 0.0;

        return result;
    }

    // 강수지수 R 등급
    private String getRGrade(Double R) {
        String result = "";

        if (R == 5.0) result = "좋음";
        else if (R >= 3 && R <= 4) result = "보통";
        else if (R >= 0 && R <= 2.5) result = "나쁨";

        return result;
    }

    // 일사지수 SI 구하기
    private Double getSI(Double SI) {
        Double result = 0.0;
        // 1, 3, 4 코드 값을 운량으로 변환
        if (SI == 1) SI = 0.0;
        else if(SI > 1 && SI < 3) SI = 3.0;
        else if(SI >= 3.0 && SI < 3.5) SI = 6.0;
        else if(SI >= 3.5) SI = 9.0;

        // 운량의 평균값을 점수
        if  (SI >= 0.0 && SI < 3.0) result = 5.0;
        else if (SI >= 3.0 && SI < 6.0) result = 4.1;
        else if (SI >= 6.0 && SI < 9.0) result = 2.2;
        else if (SI >= 9.0) result = 0.3;

        return result;
    }

    // 일사지수 SI 등급
    private String getSIGrade(Double SI) {
        String result = "";

        if (SI == 5.0) result = "좋음";
        else if (SI >= 2.2 && SI <=4.1) result = "보통";
        else if (SI == 0.3) result = "나쁨";

        return result;
    }

    // TCI 관광기후지수 구하기
    private Double getTCI(Double HN, Double HD, Double W, Double R, Double SI) {
        return -1.91 + (0.09 * HN) + (0.08 * HD) + (0.12 * W) + (0.13 * R) + (0.09 * SI);
    }

    // TCI 등급
    private String getTCIGrade(Double TCI) {
        if (TCI >= 0.35) return "매우좋음";
        else if (TCI >= 0.1) return "좋음";
        else if (TCI >= -0.25) return "보통";
        else return "나쁨";
        // 특보발령 주의 필요
    }


    @Override
    public RankDTO makeRankData(RankWeatherDTO rankWeatherDTO) {
        Double HN_HI = calculateHI(rankWeatherDTO.getMaxTmp(), rankWeatherDTO.getMinReh());
        Double HD_HI = calculateHI(rankWeatherDTO.getAvgTmp(), rankWeatherDTO.getAvgReh());

        Double HN = getHNOrHD(HN_HI);
        Double HD = getHNOrHD(HD_HI);

        String[] HNAndHdGrade = getHNAndHDGrade(HN, HD);
        String HNGrade = HNAndHdGrade[0];
        String HDGrade = HNAndHdGrade[1];

        Double W = getW(rankWeatherDTO.getAvgWsd());
        String WGrade = getWGrade(W);

        Double R = getR(rankWeatherDTO.getSumPcp());
        String RGrade = getRGrade(R);

        Double SI = getSI(rankWeatherDTO.getAvgSky());
        String SIGrade = getSIGrade(SI);

        Double TCI = getTCI(HN, HD, W, R, SI);
        String TCIGrade = getTCIGrade(TCI);

        return RankDTO.builder()
            .level1(rankWeatherDTO.getLevel1())
            .level2(rankWeatherDTO.getLevel2())
            .weatherX(rankWeatherDTO.getWeatherX())
            .weatherY(rankWeatherDTO.getWeatherY())
            .locationX(rankWeatherDTO.getLocationX())
            .locationY(rankWeatherDTO.getLocationY())
            .baseDate(rankWeatherDTO.getBaseDate())
            .HNGrade(HNGrade)
            .HDGrade(HDGrade)
            .SIGrade(SIGrade)
            .RGrade(RGrade)
            .WGrade(WGrade)
            .TCI(TCI)
            .TCIGrade(TCIGrade)
            .build();
    }
}
