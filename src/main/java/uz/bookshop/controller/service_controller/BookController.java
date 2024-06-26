package uz.bookshop.controller.service_controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.service.BookService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(BOOK)
public class BookController {

    private final BookService bookService;


    @PreAuthorize("hasAuthority('AUTHOR_ACCESS')")
    @PostMapping(CREATE)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO bookRequestDTO,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(bookService.createBook(bookRequestDTO, request));
    }

    @PreAuthorize("hasAuthority('AUTHOR_ACCESS')")
    @PutMapping(UPDATE)
    public ResponseEntity<BookResponseDTO> updateBook(@RequestParam Long id, @RequestBody BookRequestDTO bookRequestDTO,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(bookService.updateBook(bookRequestDTO, id, request));
    }

    @PreAuthorize("hasAuthority('AUTHOR_ACCESS')")
    @DeleteMapping(DELETE)
    public ResponseEntity<ResponseDTO> deleteBook(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(bookService.deleteBook(id, request));
    }

    @PreAuthorize("hasAnyAuthority('AUTHOR_ACCESS', 'USER_ACCESS')")
    @GetMapping(GET_ONE)
    public ResponseEntity<BookResponseDTO> getOneBook(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(bookService.getBook(id, httpServletRequest));
    }

    @PreAuthorize("hasAnyAuthority('AUTHOR_ACCESS', 'USER_ACCESS')")
    @GetMapping(GET_ALL)
    public ResponseEntity<?> getAllBooks(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(bookService.getAllBooks(httpServletRequest));
    }

    @PreAuthorize("hasAuthority('AUTHOR_ACCESS')")
    @GetMapping(GET_USER_BOOKS)
    public ResponseEntity<?> getUserBooks(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(bookService.getUserBooks(httpServletRequest));
    }
}
