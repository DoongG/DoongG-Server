package com.merge.doongG.service;

import com.merge.doongG.domain.Post;
import com.merge.doongG.domain.Reaction;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.ReactionDTO;
import com.merge.doongG.exception.PostNotFoundException;
import com.merge.doongG.exception.UserNotFoundException;
import com.merge.doongG.repository.PostRepository;
import com.merge.doongG.repository.ReactionRepository;
import com.merge.doongG.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository, PostRepository postRepository, UserRepository userRepository) {
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReactionDTO likePost(Long postId, UUID uuid) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (userOptional.isPresent()) {
            Reaction existingReaction = reactionRepository.findByPostAndUser(post, userOptional).orElse(null);

            User user = userOptional.get();

            if (existingReaction == null) {
                Reaction newReaction = Reaction.builder()
                        .post(post)
                        .user(user)
                        .liked(true)
                        .build();

                newReaction.like();
                post.incrementLikeCount();
                reactionRepository.save(newReaction);
            } else {
                if (!existingReaction.isLiked()) {
                    existingReaction.like();
                    post.incrementLikeCount();
                    reactionRepository.save(existingReaction);
                } else {
                    existingReaction.undoLike();
                    post.decrementLikeCount();
                    reactionRepository.save(existingReaction);
                }
            }

            return getReactionsByPostId(postId, uuid);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    @Transactional
    public ReactionDTO dislikePost(Long postId, UUID uuid) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (userOptional.isPresent()) {
            Reaction existingReaction = reactionRepository.findByPostAndUser(post, userOptional).orElse(null);

            User user = userOptional.get();

            if (existingReaction == null) {
                Reaction newReaction = Reaction.builder()
                        .post(post)
                        .user(user)
                        .disliked(true)
                        .build();

                newReaction.dislike();
                reactionRepository.save(newReaction);
            } else {
                if (!existingReaction.isDisliked()) {
                    existingReaction.dislike();
                    reactionRepository.save(existingReaction);
                } else {
                    existingReaction.undoDislike();
                    reactionRepository.save(existingReaction);
                }
            }

            return getReactionsByPostId(postId, uuid);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public ReactionDTO getReactionsByPostId(Long postId, UUID uuid) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Optional<User> user = userRepository.findByUuid(uuid);

        Reaction userReaction = reactionRepository.findByPostAndUser(post, user).orElse(null);

        int totalLikes = reactionRepository.countByPostAndLikesIsTrue(post);
        int totalDislikes = reactionRepository.countByPostAndDislikesIsTrue(post);

        return ReactionDTO.builder()
                .postId(postId)
                .liked(userReaction != null && userReaction.isLiked())
                .disliked(userReaction != null && userReaction.isDisliked())
                .likes(totalLikes)
                .dislikes(totalDislikes)
                .build();
    }
}
