package uz.bookshop.service;


import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;

import java.security.Principal;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO bookRequestDTO, Principal principal);

    BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id);

    BookResponseDTO getBook(Long id);

    void deleteBook(Long id);
}
