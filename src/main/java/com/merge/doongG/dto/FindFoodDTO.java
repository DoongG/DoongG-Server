package com.merge.doongG.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindFoodDTO {
    private String RCP_SEQ; // 레시피 일련번호
    private String RCP_NM; // 메뉴명
    private String ATT_FILE_NO_MAIN; // 대표 이미지
}
