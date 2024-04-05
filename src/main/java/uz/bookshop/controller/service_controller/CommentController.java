package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.service.CommentService;

import java.util.List;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENT)
public class CommentController {

    private final CommentService commentService;


    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @PostMapping(CREATE)
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO commentRequestDTO,
                                                         HttpServletRequest servletRequest) {
        return ResponseEntity.ok(commentService.addComment(commentRequestDTO, servletRequest));
    }

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @GetMapping(GET_ALL)
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentByBookId(@RequestParam Long bookId,
                                                                          HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(commentService.getAllComments(bookId, httpServletRequest));
    }


    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @PutMapping(UPDATE)
    public ResponseEntity<CommentResponseDTO> editComment(@RequestParam Long id,
                                                          @RequestBody CommentRequestDTO commentRequestDTO,
                                                          HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(commentService.editComment(id, commentRequestDTO, httpServletRequest));
    }

    @PreAuthorize("hasAuthority('USER_ACCESS')")
    @DeleteMapping(DELETE)
    public ResponseEntity<ResponseDTO> deleteComment(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(commentService.deleteComment(id, httpServletRequest));
    }
}
