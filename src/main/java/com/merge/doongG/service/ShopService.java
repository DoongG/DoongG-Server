package com.merge.doongG.service;

import com.merge.doongG.domain.Product;
import com.merge.doongG.dto.BestProductDTO;
import com.merge.doongG.dto.GetAllProductDTO;
import com.merge.doongG.dto.NewProductDTO;
import com.merge.doongG.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<GetAllProductDTO> getAll() {
        List<Product> allProducts = shopRepository.findAll();
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
}
