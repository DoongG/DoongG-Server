package com.merge.doongG.service;


import com.merge.doongG.domain.RoomRivew;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.GetRoomRivewDTO;
import com.merge.doongG.dto.WriteRoomRivewDTO;
import com.merge.doongG.repository.RoomRivewRepository;
import com.merge.doongG.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomRivewService {
    private final UserRepository userRepository;
    private final RoomRivewRepository roomRivewRepository;

    // 모든 자취방 리뷰 조회
    public List<GetRoomRivewDTO> getAll() {
        List<RoomRivew> roomRivews = roomRivewRepository.findAll();
        List<GetRoomRivewDTO> list = new ArrayList<>();

        for (RoomRivew roomRivew : roomRivews) {
            GetRoomRivewDTO dto = GetRoomRivewDTO.builder()
                    .reviewId(roomRivew.getReviewId())
                    .address(roomRivew.getAddress())
                    .latitude(roomRivew.getLatitude())
                    .longitude(roomRivew.getLongitude())
                    .content(roomRivew.getContent())
                    .build();
            list.add(dto);
        }

        return list;
    }

    // 자취방 리뷰 작성
    public boolean roomRivewWrite(UUID uuid, WriteRoomRivewDTO dto) {
        // uuid로 유저 찾기
        Optional<User> user = userRepository.findByUuid(uuid);

        RoomRivew roomRivew = RoomRivew.builder()
                .user(user.get())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .content(dto.getContent())
                .build();

        roomRivewRepository.save(roomRivew); // 저장

        return true;
    }
}
