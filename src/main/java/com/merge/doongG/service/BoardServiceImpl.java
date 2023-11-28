package com.merge.doongG.service;

import com.merge.doongG.domain.*;
import com.merge.doongG.dto.*;
import com.merge.doongG.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private static final List<String> DEFAULT_BOARD_NAMES = Arrays.asList("DefaultBoard1", "DefaultBoard2", "DefaultBoard3", "DefaultBoard4", "DefaultBoard5", "DefaultBoard6");

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, PostRepository postRepository,
                            UserRepository userRepository, HashtagRepository hashtagRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
    }

    // 통합 게시판 정보 가져오기
    @Override
    public List<UnifiedBoardDTO> getUnifiedBoards() {
        List<UnifiedBoardDTO> unifiedBoards = new ArrayList<>();

        for (String boardName : DEFAULT_BOARD_NAMES) {
            Optional<Board> optionalBoard = boardRepository.findByBoardName(boardName);

            if (optionalBoard.isPresent()) {
                Board board = optionalBoard.get();
                Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<Post> posts = postRepository.findByBoardId(board.getBoardId(), pageable);

                List<PostSummaryDTO> postSummaries = posts.getContent().stream()
                        .map(post -> new PostSummaryDTO(post.getPostId(), post.getTitle(), post.getCreatedAt().toLocalDateTime())).toList();

                UnifiedBoardDTO unifiedBoardDTO = UnifiedBoardDTO.builder()
                        .boardId(board.getBoardId())
                        .boardName(board.getBoardName())
                        .boardDefaultType(board.getBoardDefaultType())
                        .posts(postSummaries)
                        .build();

                unifiedBoards.add(unifiedBoardDTO);
            }
        }

        return unifiedBoards;
    }

    // 게시판 디폴트 타입 가져오기
    @Override
    public String getBoardDefaultType(String boardName) {
        Optional<Board> board = boardRepository.findByBoardName(boardName);
        return board.map(Board::getBoardDefaultType)
                .orElse("default");
    }

    // 모든 게시판 가져오기
    @Override
    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 게시판 생성
    @Override
    public BoardDTO createBoard(String boardName) {
        Board newBoard = Board.builder().boardName(boardName).build();
        Board savedBoard = boardRepository.save(newBoard);

        return convertToDTO(savedBoard);
    }

    // 게시판 수정
    @Override
    public BoardDTO updateBoard(Long boardId, String newBoardName) {
        Optional<Board> board = boardRepository.findById(boardId);

        if (board.isPresent()) {
            Board presentBoard = board.get();
            presentBoard.changeBoardName(newBoardName);
            Board updatedBoard = boardRepository.save(presentBoard);
            return convertToDTO(updatedBoard);
        }

        return null;
    }

    // 게시판 삭제
    @Override
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    // 게시판의 게시물 리스트 가져오기
    @Override
    public Page<PostDTO> getBoard(String boardName, String order, int pageSize, int page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Post> posts;
        if ("latest".equalsIgnoreCase(order)) {
            posts = postRepository.findByBoardBoardNameOrderByCreatedAtDesc(boardName, pageable);
        } else {
            posts = postRepository.findByBoardBoardNameOrderByViewsDesc(boardName, pageable);
        }
        return posts.map(this::convertToDTO);
    }

    // 게시물 검색
    @Override
    public Page<PostDTO> searchPosts(String boardName, String keyword, String order, int pageSize, int page, String searchType) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Post> posts;

        if (StringUtils.hasText(keyword)) {
            posts = switch (order.toLowerCase()) {
                case "latest" -> findByKeywordAndOrderLatest(boardName, keyword, searchType, pageable);
                case "views" -> findByKeywordAndOrderViews(boardName, keyword, searchType, pageable);
                default -> Page.empty();
            };
        } else {
            posts = Page.empty();
        }

        return posts.map(this::convertToDTO);
    }

    private Page<Post> findByKeywordAndOrderLatest(String boardName, String keyword, String searchType, Pageable pageable) {
        return switch (searchType.toLowerCase()) {
            case "full" -> postRepository.findByBoardBoardNameAndFullTextSearchContainingIgnoreCaseOrderByCreatedAtDesc(boardName, keyword, pageable);
            case "title" -> postRepository.findByBoardBoardNameAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(boardName, keyword, pageable);
            case "author" -> postRepository.findByBoardBoardNameAndUserNicknameContainingIgnoreCaseOrderByCreatedAtDesc(boardName, keyword, pageable);
            case "content" -> postRepository.findByBoardBoardNameAndContentContainingIgnoreCaseOrderByCreatedAtDesc(boardName, keyword, pageable);
            default -> Page.empty();
        };
    }

    private Page<Post> findByKeywordAndOrderViews(String boardName, String keyword, String searchType, Pageable pageable) {
        return switch (searchType.toLowerCase()) {
            case "full" -> postRepository.findByBoardBoardNameAndFullTextSearchContainingIgnoreCaseOrderByViewsDesc(boardName, keyword, pageable);
            case "title" -> postRepository.findByBoardBoardNameAndTitleContainingIgnoreCaseOrderByViewsDesc(boardName, keyword, pageable);
            case "author" -> postRepository.findByBoardBoardNameAndUserNicknameContainingIgnoreCaseOrderByViewsDesc(boardName, keyword, pageable);
            case "content" -> postRepository.findByBoardBoardNameAndContentContainingIgnoreCaseOrderByViewsDesc(boardName, keyword, pageable);
            default -> Page.empty();
        };
    }

    // 특정 게시물을 가져오기 (게시물 상세 페이지)
    @Override
    public PostDTO getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.map(this::convertToDTO).orElse(null);
    }

    // 게시물 생성
    @Override
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        System.out.println("createPost method is called.");
        Long userId = postDTO.getUser().getId();
        UserSummaryDTO userSummary = findUserSummaryById(userId);

        if (userSummary == null) {
            return null;
        }

        Post post = convertToEntity(postDTO);
        post.updateFullTextSearch();

        List<Hashtag> hashtags = postDTO.getHashtags().stream()
                .map(this::convertToEntity).toList();

        List<PostImage> postImages = postDTO.getPostImages().stream()
                .map(postImageDTO -> convertToEntity(postImageDTO, post)).toList();

        post.setPostId(postDTO.getPostId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setViews(postDTO.getViews());
        post.setCommentAllowed(postDTO.getCommentAllowed());
        post.setBoard(postDTO.getBoard());
        post.setHashtags(hashtags);
        post.setPostImages(postImages);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // 저장된 엔터티를 반환
        Post savedPost = postRepository.save(post);
        System.out.println("createPost method execution completed.");

        // 저장된 엔터티를 다시 DTO로 변환
        return convertToDTO(savedPost);
    }

    // 게시물 수정
    @Override
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();
            existingPost.updateFullTextSearch();

            UserSummaryDTO userDTO = postDTO.getUser();
            if (userDTO == null) {
                userDTO = new UserSummaryDTO();
            }

            List<Hashtag> updatedHashtags = postDTO.getHashtags().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            List<PostImage> updatedPostImages = postDTO.getPostImages().stream()
                    .map(postImageDTO -> convertToEntity(postImageDTO, existingPost))
                    .collect(Collectors.toList());

            Post updatedPost = Post.builder()
                    .postId(existingPost.getPostId())
                    .title(postDTO.getTitle())
                    .content(postDTO.getContent())
                    .views(existingPost.getViews())
                    .commentCount(existingPost.getCommentCount())
                    .user(existingPost.getUser())
                    .board(existingPost.getBoard())
                    .comments(existingPost.getComments())
                    .postImages(updatedPostImages)
                    .hashtags(updatedHashtags)
                    .commentAllowed(postDTO.getCommentAllowed())
                    .createdAt(existingPost.getCreatedAt())
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            Post savedPost = postRepository.save(updatedPost);

            return convertToDTO(savedPost, userDTO);
        }

        return null;
    }

    // 게시물 삭제
    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    // 전체 게시물 수 가져오기
    @Override
    public long getTotalPosts(String boardname) {
        return postRepository.countByBoardBoardName(boardname);
    }

    // 게시물 조회수 증가
    @Override
    @Transactional
    public void incrementPostViews(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.incrementViews();
            postRepository.save(post);
        }
    }

    // 지난 주 좋아요 Top 10
    @Override
    public List<PostDTO> getTopLikedPosts(String boardName) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        List<Post> topLikedPosts = postRepository.findTopLikedPosts(boardName, oneWeekAgo, PageRequest.of(0, 10));

        return topLikedPosts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 유저 아이디로 UserSummaryDTO 찾기
    private UserSummaryDTO findUserSummaryById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return new UserSummaryDTO(user.getId(), user.getNickname(), user.getProfileImg());
        } else {
            return null;
        }
    }

    // Board를 DTO로 변환
    private BoardDTO convertToDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
                .build();
    }

    // Post를 DTO로 변환하기
    private PostDTO convertToDTO(Post post) {
        UserSummaryDTO userDTO = convertToDTO(post.getUser());

        List<CommentResponseDTO> commentDTOs = convertCommentsToDTO(post.getComments());
        List<HashtagDTO> hashtagDTOs = convertHashtagsToDTO(post.getHashtags());
        List<PostImageDTO> postImageDTOs = convertImagesToDTO(post.getPostImages());

        int likeCount = (int) post.getReactions().stream().filter(Reaction::isLiked).count();
        int dislikeCount = (int) post.getReactions().stream().filter(Reaction::isDisliked).count();

        return PostDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .commentCount(post.getCommentCount())
                .user(userDTO)
                .board(post.getBoard())
                .comments(commentDTOs)
                .commentAllowed(post.getCommentAllowed())
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .hashtags(hashtagDTOs)
                .postImages(postImageDTOs)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private List<PostImageDTO> convertImagesToDTO(List<PostImage> postImages) {
        return postImages.stream()
                .map(postImage -> PostImageDTO.builder()
                        .url(postImage.getUrl())
                        .description(postImage.getDescription())
                        .imageType(postImage.getImageType())
                        .build())
                .collect(Collectors.toList());
    }

    private List<HashtagDTO> convertHashtagsToDTO(List<Hashtag> hashtags) {
        return hashtags.stream()
                .map(hashtag -> HashtagDTO.builder()
                        .hashtagName(hashtag.getHashtagName())
                        .build())
                .collect(Collectors.toList());
    }

    // Comment 리스트를 DTO 리스트로 변환
    private List<CommentResponseDTO> convertCommentsToDTO(List<Comment> comments) {
        List<CommentResponseDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDTO commentDTO = convertToDTO(comment);
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }

    // Comment를 DTO로 변환
    private CommentResponseDTO convertToDTO(Comment comment) {
        Long parentCommentId = (comment.getParentComment() != null) ? comment.getParentComment().getCommentId() : null;

        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .commenter(convertToDTO(comment.getCommenter()))
                .content(comment.getContent())
                .parentCommentId(parentCommentId)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // User를 UserSummaryDTO로 변환
    private UserSummaryDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserSummaryDTO.builder()
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

    // PostImage를 PostImageDTO로 변환
    private PostImageDTO convertToDTO(PostImage postImage) {
        return PostImageDTO.builder()
                .url(postImage.getUrl())
                .description(postImage.getDescription())
                .imageType(postImage.getImageType())
                .build();
    }

    // Hashtag를 HashtagDTO로 변환
    private HashtagDTO convertToDTO(Hashtag hashtag) {
        if (hashtag == null) {
            return null;
        }
        return HashtagDTO.builder()
                .hashtagName(hashtag.getHashtagName())
                .build();
    }

    private PostDTO convertToDTO(Post post, UserSummaryDTO userDTO) {
        List<CommentResponseDTO> commentDTOs = post.getComments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<PostImageDTO> postImageDTOs = post.getPostImages().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<HashtagDTO> hashtagDTOs = post.getHashtags().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PostDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .commentCount(post.getCommentCount())
                .user(userDTO)
                .board(post.getBoard())
                .comments(commentDTOs)
                .postImages(postImageDTOs)
                .hashtags(hashtagDTOs)
                .commentAllowed(post.getCommentAllowed())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // PostDTO를 엔티티로 변환
    private Post convertToEntity(PostDTO postDTO) {
        User user = convertToEntity(postDTO.getUser());
        List<CommentResponseDTO> commentDTOs = postDTO.getComments();
        List<Comment> comments = convertToEntity(commentDTOs);

        return Post.builder()
                .postId(postDTO.getPostId())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .views(postDTO.getViews())
                .comments(comments)
                .commentCount(postDTO.getCommentCount())
                .user(user)
                .commentAllowed(postDTO.getCommentAllowed())
                .board(postDTO.getBoard())
                .createdAt(postDTO.getCreatedAt())
                .updatedAt(postDTO.getUpdatedAt())
                .build();
    }

    // Comment 목록을 엔티티 목록으로 변환
    private List<Comment> convertToEntity(List<CommentResponseDTO> commentDTOs) {
        List<Comment> comments = new ArrayList<>();
        if (commentDTOs != null) {
            for (CommentResponseDTO commentDTO : commentDTOs) {
                Comment comment = convertToEntity(commentDTO);
                comments.add(comment);
            }
        }
        return comments;
    }

    // Comment를 엔티티로 변환
    private Comment convertToEntity(CommentResponseDTO commentDTO) {
        return Comment.builder()
                .commentId(commentDTO.getCommentId())
                .content(commentDTO.getContent())
                .createdAt(commentDTO.getCreatedAt())
                .commenter(convertToEntity(commentDTO.getCommenter()))
                .build();
    }

    // UserSummary를 엔티티로 변환
    private User convertToEntity(UserSummaryDTO commenter) {
        return User.builder()
                .id(commenter.getId())
                .nickname(commenter.getNickname())
                .profileImg(commenter.getProfileImg())
                .build();
    }

    // Hashtag를 엔터티로 변환
    private Hashtag convertToEntity(HashtagDTO hashtagDTO) {
        if (hashtagDTO == null || !StringUtils.hasText(hashtagDTO.getHashtagName())) {
            return null;
        }

        String hashtagName = hashtagDTO.getHashtagName();

        // 이미 존재하는 해시태그인지 확인
        Optional<Hashtag> existingHashtag = hashtagRepository.findByHashtagName(hashtagName);

        // 이미 존재하는 경우에는 기존 해시태그를, 존재하지 않는 경우에는 새로운 해시태그를 생성하여 반환
        return existingHashtag.orElseGet(() -> {
            // 저장된 Hashtag 엔터티를 반환하도록 변경
            return hashtagRepository.save(Hashtag.builder()
                    .hashtagName(hashtagName)
                    .build());
        });
    }

    private PostImage convertToEntity(PostImageDTO postImageDTO, Post post) {
        return PostImage.builder()
                .url(postImageDTO.getUrl())
                .description(postImageDTO.getDescription())
                .imageType(postImageDTO.getImageType())
                .post(post)
                .build();
    }
}
