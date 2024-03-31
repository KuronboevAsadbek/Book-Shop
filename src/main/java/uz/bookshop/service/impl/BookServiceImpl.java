package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.model.Book;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.BookException;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.BookService;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    private final UserRepository userRepository;

    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO, Principal principal) {
        BookResponseDTO bookResponseDTO;
        User user = userRepository.findByUsername(principal.getName());
        try {
            Book book = bookMapper.toEntity(bookRequestDTO);
//            book.setPrice(book.getPrice().multiply(BigInteger.valueOf(1000)));
            book.setUserId(user.getId());
            book = bookRepository.save(book);
            bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setAuthorName(user.getFirstName());
            bookResponseDTO.setAuthorSurname(user.getLastName());
            return bookResponseDTO;
        } catch (Exception e) {
            log.error("Error while creating book", e);
            throw new BookException("Error while creating book");
        }
    }

    @Override
    public BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookException("Book not found"));
            bookMapper.updateFromDto(bookRequestDTO, book);
            book = bookRepository.save(book);
            return bookMapper.toDto(book);
        } catch (Exception e) {
            log.error("Error while updating book", e);
            throw new BookException("Error while updating book");
        }
    }

    //    @Cacheable(value = "book")
    @Override
    public BookResponseDTO getBook(Long id) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookException("Book not found"));
            User user = userRepository.findById(book.getUserId()).orElseThrow(() -> new BookException("User not found"));
            BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setAuthorName(user.getFirstName());
            bookResponseDTO.setAuthorSurname(user.getLastName());
            return bookResponseDTO;
        } catch (Exception e) {
            log.error("Error while getting book", e);
            throw new BookException("Error while getting book");

        }
    }

    @Override
    public void deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while deleting book", e);
            throw new BookException("Error while deleting book");
        }
    }
}
