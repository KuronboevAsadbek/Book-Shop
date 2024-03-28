package uz.bookshop.controller.service_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.service.BookService;

import java.security.Principal;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(BOOK)
public class BookController {

    private final BookService bookService;


    @PreAuthorize("hasAuthority('CAN_MANAGE_BOOK')")
    @PostMapping(CREATE)
    public ResponseEntity<BookResponseDTO> createBook(Principal principal, BookRequestDTO bookRequestDTO) {
        return ResponseEntity.ok(bookService.createBook(bookRequestDTO, principal));
    }

    @PreAuthorize("hasAuthority('CAN_MANAGE_BOOK')")
    @PutMapping(UPDATE)
    public ResponseEntity<BookResponseDTO> updateBook(@RequestParam Long id, BookRequestDTO bookRequestDTO) {
        return ResponseEntity.ok(bookService.updateBook(bookRequestDTO, id));
    }

    @PreAuthorize("hasAuthority('CAN_MANAGE_BOOK')")
    @DeleteMapping(DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted");
    }

    @PreAuthorize("hasAuthority('CAN_MANAGE_BOOK')")
    @GetMapping(GET_ONE)
    public ResponseEntity<BookResponseDTO> getOneBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }
}
