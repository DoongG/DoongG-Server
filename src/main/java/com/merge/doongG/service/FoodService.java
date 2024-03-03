package com.merge.doongG.service;

import com.merge.doongG.dto.FindFoodDTO;
import com.merge.doongG.dto.GetOneFoodDTO;

import java.util.List;

public interface FoodService {

    // 재료기반 레시피 검색
    List<FindFoodDTO> findFood(String[] ingredients);

    // 레시피 상세 조회
    GetOneFoodDTO findFoodDetail(String name);
}
