package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOneFoodDTO {
    private String RCP_NM; // 메뉴명
    private String RCP_PARTS_DTLS; // 재료
    private String ATT_FILE_NO_MAIN; // 요리사진
    private String[] manual; // 조리방법
    private String[] manual_img; // 조리방법 이미지
}
