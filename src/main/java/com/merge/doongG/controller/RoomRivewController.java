package com.merge.doongG.controller;

import com.merge.doongG.dto.GetRoomRivewDTO;
import com.merge.doongG.dto.MyRoomRivewDTO;
import com.merge.doongG.dto.WriteRoomRivewDTO;
import com.merge.doongG.service.RoomRivewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
