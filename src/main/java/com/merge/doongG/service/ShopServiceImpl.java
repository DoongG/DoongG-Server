package com.merge.doongG.service;

import com.merge.doongG.domain.Product;
import com.merge.doongG.domain.Review;
import com.merge.doongG.dto.*;
import com.merge.doongG.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    @Override
    public List<NewProductDTO> getNew() {
        // 최근 상품 9개 배열
        List<Product> newProducts = shopRepository.findTop9ByOrderByCreatedAtDesc();
        List<NewProductDTO> newProductDTOS = new ArrayList<>();

        for (Product product : newProducts) {
            NewProductDTO newProductDTO = NewProductDTO.builder()
                    .productID(product.getProductID())
                    .productName(product.getProductName())
                    .productImage(product.getProductImage())
                    .stock(product.getStock())
                    .price(product.getPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .viewCount(product.getViewCount())
                    .build();
            newProductDTOS.add(newProductDTO);
        }

        return newProductDTOS;
    }

    @Override
    public List<BestProductDTO> getBest() {
        // Best 상품 목록 반환
        List<Product> bestProducts = shopRepository.findTop9ByOrderByViewCountDesc();
        List<BestProductDTO> bestProductDTOS = new ArrayList<>();

        for (Product product : bestProducts) {
            BestProductDTO bestProductDTO = BestProductDTO.builder()
                    .productID(product.getProductID())
                    .productName(product.getProductName())
                    .productImage(product.getProductImage())
                    .stock(product.getStock())
                    .price(product.getPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .viewCount(product.getViewCount())
                    .build();
            bestProductDTOS.add(bestProductDTO);
        }

        return bestProductDTOS;
    }

    @Override
    public List<GetAllProductDTO> getAll(String category, int page, int size) {
        // 페이지네이션을 고려한 상품 목록 반환
        List<Product> allProducts = shopRepository.findAllByCategory(category, PageRequest.of(page, size));
        List<GetAllProductDTO> getAllProductDTOS = new ArrayList<>();

        for (Product product : allProducts) {
            GetAllProductDTO getAllProductDTO = GetAllProductDTO.builder()
                    .productID(product.getProductID())
                    .productName(product.getProductName())
                    .productImage(product.getProductImage())
                    .category(product.getCategory())
                    .stock(product.getStock())
                    .price(product.getPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .viewCount(product.getViewCount())
                    .build();
            getAllProductDTOS.add(getAllProductDTO);
        }

        return getAllProductDTOS;
    }

    @Override
    public GetOneDTO getOne(Long productId) {
        // 상품 하나 조회
        shopRepository.updateViewCount(productId);
        Optional<Product> product = shopRepository.findByProductID(productId);

        // ReviewDTO 배열 만들기
        List<ReviewDTO> reviews = new ArrayList<>();
        for (int i = 0; i < product.get().getReviews().size(); i++) {
            Review review = product.get().getReviews().get(i);
            Long reviewId = review.getReviewId(); // 리뷰의 식별자 가져오기
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .reviewId(reviewId)
                    .nickname(review.getUser().getNickname()) // 작성자
                    .content(review.getContent()) // 내용
                    .createdAt(review.getCreatedAt().toString()) // 작성일자
                    .build();
            reviews.add(reviewDTO);
        }

        // GetOneDTO 만들기
        GetOneDTO getOneDTO = GetOneDTO.builder()
                .productID(product.get().getProductID())
                .productName(product.get().getProductName())
                .productImage(product.get().getProductImage())
                .productDescription(product.get().getProductDescription())
                .category(product.get().getCategory())
                .stock(product.get().getStock())
                .price(product.get().getPrice())
                .discountedPrice(product.get().getDiscountedPrice())
                .viewCount(product.get().getViewCount())
                .createdAt(product.get().getCreatedAt().toString())
                .reviews(reviews)
                .build();

        return getOneDTO;
    }

    @Override
    public List<RecommendedProductDTO> getRecommendedProducts(String category) {
        // 리뷰 수를 기준으로 상위 8개 상품 가져오기
        List<Product> recommendedProducts = shopRepository.findTop8ByMostReviewed();

        // RecommendedProductDTO 리스트 생성
        List<RecommendedProductDTO> recommendedProductDTOs = new ArrayList<>();

        for (Product product : recommendedProducts) {
            RecommendedProductDTO recommendedProductDTO = RecommendedProductDTO.builder()
                    .productID(product.getProductID())
                    .productName(product.getProductName())
                    .productImage(product.getProductImage())
                    .category(product.getCategory())
                    .stock(product.getStock())
                    .price(product.getPrice())
                    .discountedPrice(product.getDiscountedPrice())
                    .viewCount(product.getViewCount())
                    .build();
            recommendedProductDTOs.add(recommendedProductDTO);
        }

        return recommendedProductDTOs;
    }

}
