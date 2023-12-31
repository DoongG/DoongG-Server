package com.merge.doongG.repository;

import com.merge.doongG.domain.Post;
import com.merge.doongG.domain.Reaction;
import com.merge.doongG.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByPostAndUser(Post post, Optional<User> user);

    int countByPostAndLikesIsTrue(Post post);

    int countByPostAndDislikesIsTrue(Post post);
}
