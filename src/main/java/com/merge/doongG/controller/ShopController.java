package com.merge.doongG.controller;

import com.merge.doongG.domain.Product;
import com.merge.doongG.dto.BestProductDTO;
import com.merge.doongG.dto.GetAllProductDTO;
import com.merge.doongG.dto.GetOneDTO;
import com.merge.doongG.dto.NewProductDTO;
import com.merge.doongG.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/new") // 최근 상품 10개 /shop/new
    public ResponseEntity<List<NewProductDTO>> getNew() {
        List<NewProductDTO> products = shopService.getNew();

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/best") // 인기 상품 10개 /shop/best
    public ResponseEntity<List<BestProductDTO>> getBest() {
        List<BestProductDTO> products = shopService.getBest();

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/getAll/{category}") // 카테고리 별 상품 조회 /shop/getAll/뷰티
    public ResponseEntity<List<GetAllProductDTO>> getAll(@PathVariable String category) {
        List<GetAllProductDTO> products = shopService.getAll(category);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/get/{productId}") // 상품 하나 조회 /shop/get/1
    public ResponseEntity<GetOneDTO> getOne(@PathVariable Long productId) {
        GetOneDTO result = shopService.getOne(productId);

        return ResponseEntity.ok().body(result);
    }
}
