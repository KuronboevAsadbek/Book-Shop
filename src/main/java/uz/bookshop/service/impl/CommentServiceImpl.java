package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.model.Comment;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.CommentException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.mapping.CommentMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.CommentRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.CommentService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final static Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityManager entityManager;
    private final Gson gson;
    private final NetworkDataService networkDataService;

    private static CommentResponseDTO getCommentResponseDTO(Object[] row) {
        Long commentId = (Long) row[0];
        String commentText = (String) row[1];
        Instant instant = (Instant) row[2];
        String createdBy = (String) row[3];
        Long bookId = (Long) row[4];
        String bookName = (String) row[5];
        String authorName = (String) row[6];
        String authorSurname = (String) row[7];


        String createdAt = instant.toString();

        BookResponseDTO bookResponseDTO = new BookResponseDTO(bookId, bookName, null, null, authorName, authorSurname);
        return new CommentResponseDTO(commentId, commentText, bookResponseDTO, createdAt, createdBy);
    }

    @Override
    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO,
                                         HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            BookResponseDTO bookResponseDTO = bookMapper.toDto(bookRepository.findById(commentRequestDTO.getBookId())
                    .orElseThrow(() -> new CommentException("Book not found")));
            CommentResponseDTO commentResponseDTO;
            User user = userRepository.findByUsername(jwtTokenProvider.getCurrentUser());
            Comment comment = commentMapper.toEntity(commentRequestDTO);
            comment.setUserId(user.getId());
            comment = commentRepository.save(comment);
            commentResponseDTO = commentMapper.toDto(comment);
            commentResponseDTO.setBook(bookResponseDTO);
            log.info("Comment added successfully");
            evictCacheForComments(commentRequestDTO.getBookId());
            LOG.info("Comment Added \t\t {}", gson.toJson(commentRequestDTO));
            return commentResponseDTO;

        } catch (Exception e) {
            LOG.error("Error in adding comment {}", e.getMessage());
            throw new CommentException("Error in adding comment");
        }
    }

    @Override
    public CommentResponseDTO editComment(Long id, CommentRequestDTO commentRequestDTO,
                                          HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            CommentResponseDTO commentResponseDTO;
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException("Comment not found"));
            User user = userRepository.findByUsername(jwtTokenProvider.getCurrentUser());
            if (comment.getUserId().equals(user.getId())) {
                commentMapper.updateFromDto(commentRequestDTO, comment);
                commentRepository.save(comment);
                comment = commentRepository.findById(id).orElseThrow(() -> new CommentException("Comment not found"));
                commentResponseDTO = commentMapper.toDto(comment);
                LOG.info("Comment Edited \t\t {}", gson.toJson(commentResponseDTO));
                return commentResponseDTO;
            } else {
                LOG.error("You can't edit this comment");
                throw new CommentException("You can't edit this comment");
            }
        } catch (Exception e) {
            LOG.error("Error editing comment {}", e.getMessage());
            throw new CommentException("Error editing comment");
        }
    }

    @Override
    @Cacheable(value = "comments", key = "#id")
    public List<CommentResponseDTO> getAllComments(Long id, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            String sql = ("""
                    SELECT c.id                 AS id,
                           c.text               AS text,
                           c.created_at         AS created_at,
                           c.created_by         AS created_by,
                           b.id                 AS book_id,
                           b.name               AS book_name,
                           u.first_name         AS author_name,
                           u.last_name          AS author_surname
                    FROM comment c
                        JOIN book b ON c.book_id = b.id
                        JOIN users u ON b.user_id = u.id WHERE b.id=:id
                    """);
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("id", id);
            @SuppressWarnings("unchecked") List<Object[]> resultList = query.getResultList();

            List<CommentResponseDTO> commentResponseDTOS = new ArrayList<>();
            for (Object[] row : resultList) {
                CommentResponseDTO commentResponseDTO = getCommentResponseDTO(row);
                commentResponseDTOS.add(commentResponseDTO);
            }
            LOG.info("Comments Getting \t\t {}", gson.toJson(commentResponseDTOS));
            return commentResponseDTOS;
        } catch (Exception e) {
            LOG.error("Error getting comments {}", e.getMessage());
            throw new CommentException("Error getting comments");
        }
    }

    @Override
    public ResponseDTO deleteComment(Long id, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            ResponseDTO responseDTO = new ResponseDTO();
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException("Comment not found"));
            User user = userRepository.findByUsername(jwtTokenProvider.getCurrentUser());
            if (comment.getUserId().equals(user.getId())) {
                commentRepository.delete(comment);
                responseDTO.setMessage("Comment deleted successfully");
                LOG.info("Comment Deleted \t\t {}", gson.toJson(comment));
                return responseDTO;
            } else {
                LOG.error("You can't delete this comment");
                throw new CommentException("You can't delete this comment");
            }
        } catch (Exception e) {
            LOG.error("Error deleting comment {}", e.getMessage());
            throw new CommentException("Error deleting comment");
        }

    }

    @CacheEvict(value = "comments", key = "#bookId")
    public void evictCacheForComments(Long bookId) {
        LOG.info("Cache evicted for book id {}", bookId);
    }
}
