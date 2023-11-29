package com.merge.doongG.repository;

import com.merge.doongG.domain.RoomRivew;
import com.merge.doongG.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRivewRepository extends JpaRepository<RoomRivew, Long> {
    // user가 작성한 리뷰 조회
    List<RoomRivew> findByUser(User user);

    // 주소를 기준으로 리뷰 조회
    List<RoomRivew> findByAddress(String address);
}
