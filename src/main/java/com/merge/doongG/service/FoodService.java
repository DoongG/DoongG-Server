package com.merge.doongG.service;

import com.merge.doongG.dto.FindFoodDTO;
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
}
