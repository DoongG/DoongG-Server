package com.merge.doongG.controller;

import com.merge.doongG.dto.FindFoodDTO;
import com.merge.doongG.dto.GetOneFoodDTO;
import com.merge.doongG.dto.IngredientsDTO;
import com.merge.doongG.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {
    private final FoodService foodService;

    @PostMapping // 레시피 전체 조회
    public ResponseEntity<List<FindFoodDTO>> findFood(@RequestBody IngredientsDTO dto) {
        List<FindFoodDTO> result = foodService.findFood(dto.getIngredients());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{name}") // 레시피 상세 조회
    public ResponseEntity<GetOneFoodDTO> findFoodDetail(@PathVariable String name) {
        GetOneFoodDTO result = foodService.findFoodDetail(name);
        return ResponseEntity.ok().body(result);
    }
}