package uz.bookshop.service;


import jakarta.servlet.http.HttpServletRequest;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;

import java.util.List;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO bookRequestDTO, HttpServletRequest servletRequest);

    BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id, HttpServletRequest httpServletRequest);

    BookResponseDTO getBook(Long id, HttpServletRequest httpServletRequest);

    List<BookResponseDTO> getAllBooks(HttpServletRequest httpServletRequest);

    ResponseDTO deleteBook(Long id, HttpServletRequest httpServletRequest);
}
