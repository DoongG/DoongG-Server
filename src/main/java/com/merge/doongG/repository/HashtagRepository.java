package com.merge.doongG.repository;

import com.merge.doongG.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByHashtagName(String hashtagName);

    List<Hashtag> findByHashtagNameIn(List<String> hashtags);
}
