package com.merge.doongG.controller;

import com.merge.doongG.domain.Product;
import com.merge.doongG.dto.BestProductDTO;
import com.merge.doongG.dto.GetAllProductDTO;
import com.merge.doongG.dto.GetOneDTO;
import com.merge.doongG.dto.NewProductDTO;
import com.merge.doongG.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "핫딜 API", description = "핫딜 관리를 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    @Operation(
            summary = "최근 상품 조회",
            description = "최근에 추가된 상품 중에서 최근 10개를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "최근 상품 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/new")
    public ResponseEntity<List<NewProductDTO>> getNew() {
        List<NewProductDTO> products = shopService.getNew();

        return ResponseEntity.ok().body(products);
    }

    @Operation(
            summary = "인기 상품 조회",
            description = "인기 상품 중에서 최근 10개를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 상품 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/best")
    public ResponseEntity<List<BestProductDTO>> getBest() {
        List<BestProductDTO> products = shopService.getBest();

        return ResponseEntity.ok().body(products);
    }

    @Operation(
            summary = "카테고리 별 상품 조회",
            description = "지정된 카테고리에 속한 상품들을 조회합니다.",
            parameters = {
                    @Parameter(name = "category", description = "상품 카테고리", example = "뷰티", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 별 상품 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/getAll/{category}")
    public ResponseEntity<List<GetAllProductDTO>> getAll(@PathVariable String category) {
        List<GetAllProductDTO> products = shopService.getAll(category);

        return ResponseEntity.ok().body(products);
    }

    @Operation(
            summary = "상품 하나 조회",
            description = "지정된 상품 ID에 해당하는 상품을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 하나 조회 성공", content = @Content(schema = @Schema(implementation = GetOneDTO.class)))
            }
    )
    @GetMapping("/get/{productId}")
    public ResponseEntity<GetOneDTO> getOne(@PathVariable Long productId) {
        GetOneDTO result = shopService.getOne(productId);

        return ResponseEntity.ok().body(result);
    }
}
