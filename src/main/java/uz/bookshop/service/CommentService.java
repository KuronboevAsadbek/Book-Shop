package uz.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

import java.util.List;

public interface CommentService {

    CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO,
                                  HttpServletRequest httpServletRequest);

    CommentResponseDTO editComment(Long id, CommentRequestDTO commentRequestDTO,
                                   HttpServletRequest httpServletRequest);

    List<CommentResponseDTO> getAllComments(Long id,
                                            HttpServletRequest httpServletRequest);

    ResponseDTO deleteComment(Long id,
                              HttpServletRequest httpServletRequest);

}
