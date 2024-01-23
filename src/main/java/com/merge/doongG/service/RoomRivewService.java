package com.merge.doongG.service;


import com.merge.doongG.domain.RoomRivew;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.GetRoomRivewDTO;
import com.merge.doongG.dto.MyRoomRivewDTO;
import com.merge.doongG.dto.WriteRoomRivewDTO;
import com.merge.doongG.repository.RoomRivewRepository;
import com.merge.doongG.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
                    .createdAt(roomRivew.getCreatedAt().toString())
                    .build();
            list.add(dto);
        }

        // createAt 기준으로 최신순으로 정렬
        list.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return list;
    }

    // 자취방 리뷰 작성
    public boolean roomRivewWrite(UUID uuid, WriteRoomRivewDTO dto) {
        boolean flag = false; // 같은 주소로 작성된 리뷰가 있는지 확인

        // uuid로 유저 찾기
        Optional<User> user = userRepository.findByUuid(uuid);

        // 주소로 이미 작성한 리뷰가 있는지 확인
        List<RoomRivew> list = roomRivewRepository.findByAddress(dto.getAddress());

        if (list.size() > 0) {
            flag = true; // 같은 주소로 작성된 리뷰가 있으면 true로 변경
        }

        // 요청 들어온 주소로 엔티티 생성
        // 같은 주소로 작성된 리뷰가 존재한다면, 먼저 작성된 리뷰의 위도와 경도로 설정
        RoomRivew roomRivew = RoomRivew.builder()
                .user(user.get())
                .address(dto.getAddress())
                .latitude(flag ? list.get(0).getLatitude() : dto.getLatitude())
                .longitude(flag ? list.get(0).getLongitude() : dto.getLongitude())
                .content(dto.getContent())
                .build();

        roomRivewRepository.save(roomRivew); // 저장

        return true;
    }

    public List<MyRoomRivewDTO> getMyRoomRivew(UUID uuid) {
        // uuid로 유저 찾기
        Optional<User> user = userRepository.findByUuid(uuid);
        List<RoomRivew> roomRivews = roomRivewRepository.findByUser(user.get());
        List<MyRoomRivewDTO> list = new ArrayList<>();

        for (RoomRivew roomRivew : roomRivews) {
            MyRoomRivewDTO dto = MyRoomRivewDTO.builder()
                    .reviewId(roomRivew.getReviewId())
                    .address(roomRivew.getAddress())
                    .content(roomRivew.getContent())
                    .createdAt(roomRivew.getCreatedAt().toString())
                    .build();
            list.add(dto);
        }

        // createAt 기준으로 최신순으로 정렬
        list.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return list;
    }

    // 자취방 리뷰 삭제
    public void deleteRoomRivew(UUID uuid, Long reviewId) {
        Optional<RoomRivew> optionalRoomRivew = roomRivewRepository.findById(reviewId);

        if (optionalRoomRivew.isPresent()) {
            RoomRivew roomRivew = optionalRoomRivew.get();

            // 현재 사용자가 리뷰 작성자인지 확인
            if (!roomRivew.getUser().getUuid().equals(uuid)) {
                throw new AccessDeniedException("이 리뷰를 삭제할 권한이 없습니다");
            }

            roomRivewRepository.deleteById(reviewId);
        } else {
            // 리뷰가 존재하지 않을 때 핸들링
            throw new RuntimeException("Review with id " + reviewId + " not found");
        }
    }
}
