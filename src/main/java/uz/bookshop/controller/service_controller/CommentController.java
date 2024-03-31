package uz.bookshop.controller.service_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.CommentRequestDTO;
import uz.bookshop.domain.dto.response_dto.CommentResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.service.CommentService;

import java.security.Principal;
import java.util.List;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENT)
public class CommentController {

    private final CommentService commentService;


    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @PostMapping(CREATE)
    public ResponseEntity<CommentResponseDTO> addComment(CommentRequestDTO commentRequestDTO, Principal principal) {
        return ResponseEntity.ok(commentService.addComment(commentRequestDTO, principal));
    }

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @GetMapping(GET_ALL)
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentByBookId(@RequestParam Long bookId) {
        return ResponseEntity.ok(commentService.getAllComments(bookId));
    }

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @PutMapping(UPDATE)
    public ResponseEntity<CommentResponseDTO> editComment(@RequestParam Long id,
                                                          @RequestBody CommentRequestDTO commentRequestDTO,
                                                          Principal principal) {
        return ResponseEntity.ok(commentService.editComment(id, commentRequestDTO, principal));
    }

    @PreAuthorize("hasAuthority('CAN_ADD_COMMENT')")
    @DeleteMapping(DELETE)
    public ResponseEntity<ResponseDTO> deleteComment(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(commentService.deleteComment(id, principal));
    }
}
