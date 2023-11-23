package com.merge.doongG.service;

import com.merge.doongG.domain.Board;
import com.merge.doongG.domain.Post;
import com.merge.doongG.dto.BoardDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.PostSummaryDTO;
import com.merge.doongG.dto.UnifiedBoardDTO;
import com.merge.doongG.repository.BoardRepository;
import com.merge.doongG.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private static final List<String> DEFAULT_BOARD_NAMES = Arrays.asList("DefaultBoard1", "DefaultBoard2", "DefaultBoard3", "DefaultBoard4", "DefaultBoard5", "DefaultBoard6");

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, PostRepository postRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
    }

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
                        .posts(postSummaries)
                        .build();

                unifiedBoards.add(unifiedBoardDTO);
            }
        }

        return unifiedBoards;
    }

    @Override
    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public BoardDTO createBoard(String boardName) {
        Board newBoard = Board.builder().boardName(boardName).build();
        Board savedBoard = boardRepository.save(newBoard);

        return convertToDTO(savedBoard);
    }

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

    @Override
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public Page<PostDTO> getBoard(Long boardId, String order, int pageSize, int page) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Post> posts;
        if ("latest".equalsIgnoreCase(order)) {
            posts = postRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable);
        } else {
            posts = postRepository.findByBoardIdOrderByViewsDesc(boardId, pageable);
        }
        return posts.map(this::convertToDTO);
    }

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

    @Override
    public PostDTO getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.map(this::convertToDTO).orElse(null);
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post(
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getViews(),
                postDTO.getBoard(),
                postDTO.getUser(),
                postDTO.getCommentAllowed()
        );

        Post savedPost = postRepository.save(post);

        return convertToDTO(savedPost);
    }

    @Override
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            Post existingPost = post.get();

            PostDTO updatedPostDTO = PostDTO.builder()
                    .postId(existingPost.getPostId())
                    .title(postDTO.getTitle())
                    .content(postDTO.getContent())
                    .views(existingPost.getViews())
                    .commentCount(existingPost.getCommentCount())
                    .user(existingPost.getUser())
                    .board(existingPost.getBoard())
                    .comments(postDTO.getComments())
                    .postImages(postDTO.getPostImages())
                    .hashtags(postDTO.getHashtags())
                    .commentAllowed(postDTO.getCommentAllowed())
                    .createdAt(existingPost.getCreatedAt())
                    .updatedAt(existingPost.getUpdatedAt())
                    .build();

            Post updatedPost = postRepository.save(convertToEntity(updatedPostDTO));
            return convertToDTO(updatedPost);
        }

        return null;
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public long getTotalPosts() {
        return postRepository.count();
    }

    private BoardDTO convertToDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
                .build();
    }

    private PostDTO convertToDTO(Post post) {
        return PostDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .user(post.getUser())
                .board(post.getBoard())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Post convertToEntity(PostDTO postDTO) {
        return Post.builder()
                .postId(postDTO.getPostId())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .views(postDTO.getViews())
                .commentCount(postDTO.getCommentCount())
                .user(postDTO.getUser())
                .board(postDTO.getBoard())
                .createdAt(postDTO.getCreatedAt())
                .updatedAt(postDTO.getUpdatedAt())
                .build();
    }
}
