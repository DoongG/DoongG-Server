package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.CartService;
import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userAuth")
public class UserAuthController {
    private final UserService userService;
    private final CartService cartSerivce;

    @GetMapping
    public ResponseEntity<MyPageDTO> myPage() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        MyPageDTO result = userService.myPage(uuid);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/chNick") // 닉네임 변경 (/user/updateNickname)
    public ResponseEntity<String> chNick(@RequestBody ChNicknameDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String nickname = dto.getNickname();
        userService.chNickname(uuid, nickname);

        return ResponseEntity.ok().body("true");
    }

    @PostMapping("/chProImg") // 프로필 이미지 변경 (/user/updateProfileImg)
    public ResponseEntity<String> chProImg(@RequestBody ChProImgDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String profileImg = dto.getProfileImg();
        userService.chProImg(uuid, profileImg);

        return ResponseEntity.ok().body("true");
    }

    @PostMapping("/chPw") // 비밀번호 변경 (/userAuth/chPw)
    public ResponseEntity<String> chPw(@RequestBody ChPwDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String password = dto.getPassword();
        userService.chPw(uuid, password);

        return ResponseEntity.ok().body("true");
    }

    // 장바구니 조회 (/userAuth/getCart)
    @PostMapping("/getCart")
    public ResponseEntity<List<GetCartDTO>> getCart() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<GetCartDTO> result = cartSerivce.getCart(uuid);

        return ResponseEntity.ok().body(result);
    }

    // 장바구니 추가 (/userAuth/addCart)
    @PostMapping("/addCart")
    public ResponseEntity<String> addCart(@RequestBody AddCartDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        cartSerivce.addCart(uuid, dto.getProductID(), dto.getQuantity());

        return ResponseEntity.ok().body("true");
    }
    // 상품 주문 (/userAuth/order)
    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestBody OrderDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        userService.order(uuid, dto);

        return ResponseEntity.ok().body("true");
    }
}
