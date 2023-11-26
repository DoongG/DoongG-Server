package com.merge.doongG.service;

import com.merge.doongG.domain.Board;
import com.merge.doongG.domain.Comment;
import com.merge.doongG.domain.Post;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.*;
import com.merge.doongG.repository.BoardRepository;
import com.merge.doongG.repository.PostRepository;
import com.merge.doongG.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static final List<String> DEFAULT_BOARD_NAMES = Arrays.asList("DefaultBoard1", "DefaultBoard2", "DefaultBoard3", "DefaultBoard4", "DefaultBoard5", "DefaultBoard6");

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, PostRepository postRepository,
                            UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    // 게시판에서 특정 키워드로 검색
    @Override
    public Page<PostDTO> searchBoard(Long boardId, String keyword, String order, String category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Post> posts;

        if (StringUtils.hasText(keyword)) {
            if ("latest".equalsIgnoreCase(order)) {
                posts = switch (category.toLowerCase()) {
                    case "title" ->
                            postRepository.findByBoardIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(boardId, keyword, pageable);
                    case "author" ->
                            postRepository.findByBoardIdAndUserNicknameContainingIgnoreCaseOrderByCreatedAtDesc(boardId, keyword, pageable);
                    case "hashtag" ->
                            postRepository.findByBoardIdAndHashtagsContainingIgnoreCaseOrderByCreatedAtDesc(boardId, keyword, pageable);
                    case "content" ->
                            postRepository.findByBoardIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(boardId, keyword, pageable);
                    default -> Page.empty();
                };
            } else if ("views".equalsIgnoreCase(order)) {
                posts = switch (category.toLowerCase()) {
                    case "title" ->
                            postRepository.findByBoardIdAndTitleContainingIgnoreCaseOrderByViewsDesc(boardId, keyword, pageable);
                    case "author" ->
                            postRepository.findByBoardIdAndUserNicknameContainingIgnoreCaseOrderByViewsDesc(boardId, keyword, pageable);
                    case "hashtag" ->
                            postRepository.findByBoardIdAndHashtagsContainingIgnoreCaseOrderByViewsDesc(boardId, keyword, pageable);
                    case "content" ->
                            postRepository.findByBoardIdAndContentContainingIgnoreCaseOrderByViewsDesc(boardId, keyword, pageable);
                    default -> Page.empty();
                };
            } else {
                posts = Page.empty();
            }} else {
            posts = Page.empty();
        }

        return posts.map(this::convertToDTO);
    }

    // 특정 게시물을 가져오기 (게시물 상세 페이지)
    @Override
    public PostDTO getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.map(this::convertToDTO).orElse(null);
    }

    // 게시물 생성
    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Long userId = postDTO.getUser().getId();

        UserSummaryDTO userSummary = findUserSummaryById(userId);

        if (userSummary != null) {
            Post post = convertToEntity(postDTO);
            Post savedPost = postRepository.save(post);

            return convertToDTO(savedPost);
        } else {
            return null;
        }
    }

    // 게시물 수정
    @Override
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            Post existingPost = post.get();

            UserSummaryDTO userDTO = new UserSummaryDTO(
                    existingPost.getUser().getId(),
                    existingPost.getUser().getNickname(),
                    existingPost.getUser().getProfileImg()
            );

            PostDTO updatedPostDTO = PostDTO.builder()
                    .postId(existingPost.getPostId())
                    .title(postDTO.getTitle())
                    .content(postDTO.getContent())
                    .views(existingPost.getViews())
                    .commentCount(existingPost.getCommentCount())
                    .user(userDTO)
                    .board(existingPost.getBoard())
                    .comments(postDTO.getComments())
                    .postImages(postDTO.getPostImages())
                    .hashtags(postDTO.getHashtags())
                    .commentAllowed(postDTO.getCommentAllowed())
                    .createdAt(existingPost.getCreatedAt())
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            Post updatedPost = postRepository.save(convertToEntity(updatedPostDTO));
            return convertToDTO(updatedPost);
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
    public long getTotalPosts() {
        return postRepository.count();
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

        List<CommentResponseDTO> commentDTOs = convertToDTO(post.getComments());

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
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Comment 리스트를 DTO 리스트로 변환
    private List<CommentResponseDTO> convertToDTO(List<Comment> comments) {
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

    // User를 UserSummaryDTO로 변혼
    private UserSummaryDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserSummaryDTO.builder()
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
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
}
