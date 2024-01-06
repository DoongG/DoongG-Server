package com.merge.doongG.controller;

import com.merge.doongG.dto.FindFoodDTO;
import com.merge.doongG.dto.GetOneFoodDTO;
import com.merge.doongG.dto.IngredientsDTO;
import com.merge.doongG.service.FoodService;
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

@Tag(name = "음식 API", description = "음식 레시피를 관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {
    private final FoodService foodService;

    @Operation(
            summary = "전체 레시피 조회",
            description = "입력된 재료를 기반으로 전체 레시피를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @PostMapping
    public ResponseEntity<List<FindFoodDTO>> findFood(@RequestBody IngredientsDTO dto) {
        List<FindFoodDTO> result = foodService.findFood(dto.getIngredients());
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "레시피 상세 조회",
            description = "지정된 음식의 상세 레시피를 조회합니다.",
            parameters = {
                    @Parameter(name = "name", description = "음식 이름", example = "해물아란치니", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 상세 조회 성공", content = @Content(schema = @Schema(implementation = GetOneFoodDTO.class)))
            }
    )
    @GetMapping("/{name}")
    public ResponseEntity<GetOneFoodDTO> findFoodDetail(@PathVariable String name) {
        GetOneFoodDTO result = foodService.findFoodDetail(name);
        return ResponseEntity.ok().body(result);
    }
}