package com.merge.doongG.service;

import com.merge.doongG.domain.Product;
import com.merge.doongG.dto.*;
import com.merge.doongG.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public List<NewProductDTO> getNew() { // 최근 상품 10개 배열
        List<Product> newProducts = shopRepository.findTop10ByOrderByCreatedAtDesc();
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

    public List<BestProductDTO> getBest() {
        List<Product> bestProducts = shopRepository.findTop10ByOrderByViewCountDesc();
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

    public List<GetAllProductDTO> getAll(String category) {
        List<Product> allProducts = shopRepository.findAllByCategory(category);
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

    // 상품 하나 조회
    public GetOneDTO getOne(Long productId) {
        shopRepository.updateViewCount(productId);
        Optional<Product> product = shopRepository.findByProductID(productId);

        // ReviewDTO 배열 만들기
        List<ReviewDTO> reviews = new ArrayList<>();
        for (int i = 0; i < product.get().getReviews().size(); i++) {
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .nickname(product.get().getReviews().get(i).getUser().getNickname()) // 작성자
                    .content(product.get().getReviews().get(i).getContent()) // 내용
                    .createdAt(product.get().getReviews().get(i).getCreatedAt().toString()) // 작성일자
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
}
