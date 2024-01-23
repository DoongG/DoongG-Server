package com.merge.doongG.controller;

import com.merge.doongG.dto.GetRoomRivewDTO;
import com.merge.doongG.dto.MyRoomRivewDTO;
import com.merge.doongG.dto.WriteRoomRivewDTO;
import com.merge.doongG.service.RoomRivewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "자취방 리뷰 API", description = "자취방 리뷰를 관리하기 위한 API")
@RestController
@RequiredArgsConstructor
public class RoomRivewController {
    private final RoomRivewService roomRivewService;

    @Operation(
            summary = "모든 자취방 리뷰 조회",
            description = "모든 자취방 리뷰를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "자취방 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/roomRivew/getAll")
    public ResponseEntity<List<GetRoomRivewDTO>> getAll() {
        List<GetRoomRivewDTO> result = roomRivewService.getAll();
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "자취방 리뷰 작성",
            description = "자취방 리뷰를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "자취방 리뷰 작성 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/roomRivewWrite")
    public ResponseEntity<String> roomRivewWrite(@RequestBody WriteRoomRivewDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        roomRivewService.roomRivewWrite(uuid, dto);
        return ResponseEntity.ok().body("true");
    }

    @Operation(
            summary = "내가 쓴 자취방 리뷰 조회",
            description = "현재 로그인한 사용자가 작성한 자취방 리뷰를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내가 쓴 자취방 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/MyRoomRivew")
    public ResponseEntity<List<MyRoomRivewDTO>> getMyRoomRivew() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<MyRoomRivewDTO> result = roomRivewService.getMyRoomRivew(uuid);
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "내가 쓴 자취방 리뷰 삭제",
            description = "현재 로그인한 사용자가 작성한 자취방 리뷰를 삭제합니다.",
            parameters = {
                    @Parameter(name = "reviewId", description = "리뷰 ID", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "내가 쓴 자취방 리뷰 삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "해당 리뷰를 찾을 수 없음", content = @Content)
            }
    )
    @PostMapping("/roomRivewDelete/{reviewId}")
    public ResponseEntity<String> deleteRoomRivew(@PathVariable Long reviewId) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        try {
            roomRivewService.deleteRoomRivew(uuid, reviewId);
            return ResponseEntity.ok().body("true");
        } catch (RuntimeException e) {
            // 해당 리뷰를 찾을 수 없을 때 핸들링
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("false");
        }
    }
}
