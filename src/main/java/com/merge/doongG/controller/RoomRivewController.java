package com.merge.doongG.controller;

import com.merge.doongG.dto.GetRoomRivewDTO;
import com.merge.doongG.dto.MyRoomRivewDTO;
import com.merge.doongG.dto.WriteRoomRivewDTO;
import com.merge.doongG.service.RoomRivewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RoomRivewController {
    private final RoomRivewService roomRivewService;

    // 모든 자취방 리뷰 조회
    @GetMapping("/roomRivew/getAll")
    public ResponseEntity<List<GetRoomRivewDTO>> getAll() {
        List<GetRoomRivewDTO> result = roomRivewService.getAll();
        return ResponseEntity.ok().body(result);
    }

    // 자취방 리뷰 작성
    @PostMapping("/roomRivewWrite")
    public ResponseEntity<String> roomRivewWrite(@RequestBody WriteRoomRivewDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        roomRivewService.roomRivewWrite(uuid, dto);
        return ResponseEntity.ok().body("true");
    }

    // 내가 쓴 자취방 리뷰 조회
    @GetMapping("/MyRoomRivew")
    public ResponseEntity<List<MyRoomRivewDTO>> getMyRoomRivew() {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        List<MyRoomRivewDTO> result = roomRivewService.getMyRoomRivew(uuid);
        return ResponseEntity.ok().body(result);
    }

    // 내가 쓴 자취방 리뷰 삭제
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
