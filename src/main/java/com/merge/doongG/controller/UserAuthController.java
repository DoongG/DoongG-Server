package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.BoardService;
import com.merge.doongG.service.CartService;
import com.merge.doongG.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "인증 필요 사용자 API", description = "로그인 및 토큰이 필요한 사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/userAuth")
public class UserAuthController {
    private final UserService userService;
    private final CartService cartSerivce;
    private final BoardService boardService;

    @Operation(
            summary = "마이페이지 조회",
            description = "현재 로그인한 사용자의 마이페이지를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공", content = @Content(schema = @Schema(implementation = MyPageDTO.class)))
            }
    )
    @GetMapping
    public ResponseEntity<MyPageDTO> myPage() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        MyPageDTO result = userService.myPage(uuid);

        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "닉네임 변경",
            description = "현재 로그인한 사용자의 닉네임을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/chNick")
    public ResponseEntity<String> chNick(@RequestBody ChNicknameDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String nickname = dto.getNickname();
        userService.chNickname(uuid, nickname);

        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "프로필 이미지 변경",
            description = "현재 로그인한 사용자의 프로필 이미지를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/chProImg")
    public ResponseEntity<String> chProImg(@RequestBody ChProImgDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String profileImg = dto.getProfileImg();
        userService.chProImg(uuid, profileImg);

        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인한 사용자의 비밀번호를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/chPw")
    public ResponseEntity<String> chPw(@RequestBody ChPwDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String password = dto.getPassword();
        userService.chPw(uuid, password);

        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "장바구니 조회",
            description = "현재 로그인한 사용자의 장바구니를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "장바구니 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/getCart")
    public ResponseEntity<List<GetCartDTO>> getCart() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<GetCartDTO> result = cartSerivce.getCart(uuid);

        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "장바구니 추가",
            description = "현재 로그인한 사용자의 장바구니에 상품을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "장바구니 추가 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/addCart")
    public ResponseEntity<String> addCart(@RequestBody AddCartDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        cartSerivce.addCart(uuid, dto.getProductID(), dto.getQuantity());

        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "상품 주문",
            description = "현재 로그인한 사용자가 장바구니에 담긴 상품을 주문합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 주문 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestBody OrderDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        userService.order(uuid, dto);

        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "내가 작성한 글 조회",
            description = "현재 로그인한 사용자가 작성한 글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내가 작성한 글 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/myPosts")
    public ResponseEntity<List<PostDTO>> getMyPosts() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<PostDTO> result = boardService.getMyPosts(uuid);
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "내가 좋아요 표시한 글 조회",
            description = "현재 로그인한 사용자가 좋아요 표시한 글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내가 좋아요 표시한 글 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/myLikedPosts")
    public ResponseEntity<List<PostDTO>> getMyLikedPosts() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<PostDTO> result = boardService.getMyLikedPosts(uuid);
        return ResponseEntity.ok().body(result);
    }
}
