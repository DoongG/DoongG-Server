package com.merge.doongG.service;

import com.merge.doongG.dto.*;
import java.util.List;

public interface ShopService {

    // 최근 상품 10개 배열
    List<NewProductDTO> getNew();

    // Best 상품 목록 반환
    List<BestProductDTO> getBest();

    // 해당 카테고리의 모든 상품 반환
    List<GetAllProductDTO> getAll(String category, int page, int size);

    // 상품 하나 조회
    GetOneDTO getOne(Long productId);

    // 추천 상품 조회
    List<RecommendedProductDTO> getRecommendedProducts(String category);

}
