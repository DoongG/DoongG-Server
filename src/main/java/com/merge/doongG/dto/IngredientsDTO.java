package com.merge.doongG.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsDTO {
    private String[] ingredients; // 재료 배열
}
