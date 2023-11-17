package com.merge.doongG.repository;

import com.merge.doongG.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<PostImage, Long> {
}
