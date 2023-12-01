package com.merge.doongG.service;

import com.merge.doongG.dto.FindFoodDTO;
import com.merge.doongG.dto.GetOneFoodDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FoodService {
    @Value("${food.api.key}")
    private String apiKey;

    // 재료기반 레시피 검색
    public List<FindFoodDTO> findFood(String[] ingredients) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/COOKRCP01/json/1/1000/RCP_PARTS_DTLS=";

        // url에 재료 추가
        for (String ingredient : ingredients) {
            url += ingredient + ",";
        }

        // 마지막 , 제거
        url = url.substring(0, url.length() - 1);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
        Object result = responseEntity.getBody();

        Map<String, Object> resultMap = (Map<String, Object>) result;
        Map<String, Object> cookRcp01Map = (Map<String, Object>) resultMap.get("COOKRCP01");
        Object totalCount = cookRcp01Map.get("total_count");

        List<FindFoodDTO> list = new ArrayList<>();

        // totalCount가 0일 경우 빈배열 반환
        if (totalCount.equals("0")) {
            return list;
        } else {
            // row에 해당하는 값 추출
            List<Map<String, Object>> rowList = (List<Map<String, Object>>) cookRcp01Map.get("row");

            // 각 객체에서 필요한 값 추출하여 DTO에 설정 후 List에 추가
            for (Map<String, Object> row : rowList) {
                String rcpSeq = (String) row.get("RCP_SEQ");
                String attFileNoMain = (String) row.get("ATT_FILE_NO_MAIN");
                String rcpNm = (String) row.get("RCP_NM");

                // FindFoodDTO 객체 생성
                FindFoodDTO findFoodDTO = FindFoodDTO.builder()
                        .RCP_SEQ(rcpSeq)
                        .ATT_FILE_NO_MAIN(attFileNoMain)
                        .RCP_NM(rcpNm)
                        .build();

                // List에 추가
                list.add(findFoodDTO);
            }
        }

        return list;
    }

    // 레시피 상세 조회
    public GetOneFoodDTO findFoodDetail(String name) {
        String url = "http://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/COOKRCP01/json/1/100/RCP_NM=" + name;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
        Object result = responseEntity.getBody();

        Map<String, Object> resultMap = (Map<String, Object>) result;
        Map<String, Object> cookRcp01Map = (Map<String, Object>) resultMap.get("COOKRCP01");
        List<Map<String, Object>> rowList = (List<Map<String, Object>>) cookRcp01Map.get("row");
        Map<String, Object> row = rowList.get(0);

        // RCP_PARTS_DTLS 추출
        String rcpPartsDtls = ((String) row.get("RCP_PARTS_DTLS")).replaceAll("(\r\n|\r|\n|\n\r)", " ");

        // RCP_NM 추출
        String rcpNm = (String) row.get("RCP_NM");

        // ATT_FILE_NO_MAIN 추출
        String attFileNoMain = (String) row.get("ATT_FILE_NO_MAIN");

        // MANUAL01~20 추출
        String[] manual = new String[20];

        for (int i = 1; i <= 20; i++) {
            if (i < 10) {
                manual[i - 1] = ((String) row.get("MANUAL0" + i)).replaceAll("(\r\n|\r|\n|\n\r)", " ");
            } else {
                manual[i - 1] = ((String) row.get("MANUAL" + i)).replaceAll("(\r\n|\r|\n|\n\r)", " ");
            }
        }

        // MANUAL_IMG01~20 추출
        String[] manual_img = new String[20];

        for (int j = 1; j <= 20; j++) {
            if (j < 10) {
                // 개행문자 제거 후 저장
                manual_img[j - 1] = (String) row.get("MANUAL_IMG0" + j);
            } else {
                manual_img[j - 1] = (String) row.get("MANUAL_IMG" + j);
            }
        }

        // DTO로 반환
        GetOneFoodDTO dto = GetOneFoodDTO.builder()
                .RCP_NM(rcpNm)
                .RCP_PARTS_DTLS(rcpPartsDtls)
                .ATT_FILE_NO_MAIN(attFileNoMain)
                .manual(manual)
                .manual_img(manual_img)
                .build();

        return dto;
    }
}
