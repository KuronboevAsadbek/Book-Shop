package uz.bookshop.service;

import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

import java.security.Principal;
import java.util.List;

public interface CommentService {

    CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO, Principal principal);

    CommentResponseDTO editComment(Long id, CommentRequestDTO commentRequestDTO, Principal principal);

    List<CommentResponseDTO> getAllComments(Long id);

    ResponseDTO deleteComment(Long id, Principal principal);

}
