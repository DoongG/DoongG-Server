package com.merge.doongG.controller;

import com.merge.doongG.dto.BestProductDTO;
import com.merge.doongG.dto.GetAllProductDTO;
import com.merge.doongG.dto.NewProductDTO;
import com.merge.doongG.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/getAll")
    public ResponseEntity<List<GetAllProductDTO>> getAll() {
        List<GetAllProductDTO> products = shopService.getAll();

        return ResponseEntity.ok().body(products);
    }
}
