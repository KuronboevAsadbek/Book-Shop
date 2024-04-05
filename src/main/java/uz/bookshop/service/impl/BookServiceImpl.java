package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.bookshop.config.network.NetworkDataService;
import uz.bookshop.domain.dto.request_dto.BookRequestDTO;
import uz.bookshop.domain.dto.response_dto.BookResponseDTO;
import uz.bookshop.domain.dto.response_dto.ResponseDTO;
import uz.bookshop.domain.model.Book;
import uz.bookshop.exception.BookException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.BookService;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final static Logger LOG = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityManager entityManager;
    private final Gson gson;
    private final NetworkDataService networkDataService;


    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO,
                                      HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Book Created \t\t {}", gson.toJson(bookRequestDTO));
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            BookResponseDTO bookResponseDTO;
//            User user = userRepository.findByUsername(JwtTokenProvider.getCurrentUser());
            Book book = bookMapper.toEntity(bookRequestDTO);
//            book.setPrice(book.getPrice().multiply(BigInteger.valueOf(1000)));
            Book book2 = bookRepository.findByName(book.getName());
//            book.setUserId(user.getId());
            if (book2 != null) {
                if (book.getName().equals(book2.getName())) {
                    LOG.error("Book already exists update it \t\t {}", gson.toJson(bookRequestDTO));
                    throw new BookException("Book already exists update it");
                }
            }
            book = bookRepository.save(book);
            bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setWriter(book.getWriter());
//            bookResponseDTO.setAuthorSurname(user.getLastName());
            return bookResponseDTO;
        } catch (Exception e) {
            LOG.error("Error while creating book", e);
            throw new BookException("Error while creating book");
        }
    }

    @Override
    public BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Book updated \t\t {}", gson.toJson(bookRequestDTO));
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookException("Book not found"));
            bookMapper.updateFromDto(bookRequestDTO, book);
            book = bookRepository.save(book);
            return bookMapper.toDto(book);
        } catch (Exception e) {
            LOG.error("Error while updating book", e);
            throw new BookException("Error while updating book");
        }
    }

    //    @Cacheable(value = "book")
    @Override
    public BookResponseDTO getBook(Long id, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookException("Book not found"));
//            User user = userRepository.findById(book.getUserId()).orElseThrow(() -> new BookException("User not found"));
            BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setWriter(book.getName());
//            bookResponseDTO.setAuthorSurname(user.getLastName());
            LOG.info("Getting Book \t\t {}", gson.toJson(bookResponseDTO));
            return bookResponseDTO;
        } catch (Exception e) {
            LOG.error("Error while getting book", e);
            throw new BookException("Error while getting book");

        }
    }

    @Override
    public List<BookResponseDTO> getAllBooks(HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            List<Book> books = bookRepository.findAll();
            List<BookResponseDTO> bookResponseDTOS;
            bookResponseDTOS = bookMapper.toDto(books);
            return bookResponseDTOS;
        } catch (Exception e) {
            LOG.error("Error while getting all books", e);
            throw new BookException("Error while getting all books");

        }
    }

    @Override
    public ResponseDTO deleteBook(Long id, HttpServletRequest httpServletRequest) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            Book book = bookRepository.findById(id).orElseThrow(() -> new BookException("Book not found"));
            bookRepository.deleteById(id);
            responseDTO.setMessage("Book deleted");
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            LOG.info("Book updated \t\t {}", gson.toJson(book));
            return responseDTO;
        } catch (Exception e) {
            log.error("Error while deleting book", e);
            throw new BookException("Error while deleting book");
        }

    }

    @Override
    public List<BookResponseDTO> getUserBooks(HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            LOG.info("Client host : \t\t {}", gson.toJson(ClientInfo));
            LOG.info("Client IP :  \t\t {}", gson.toJson(ClientIP));
            List<Book> books = bookRepository.findByCreatedBy(JwtTokenProvider.getCurrentUser());
            List<BookResponseDTO> bookResponseDTOS;
            bookResponseDTOS = bookMapper.toDto(books);
            return bookResponseDTOS;
        } catch (Exception e) {
            LOG.error("Error while getting user books", e);
            throw new BookException("Error while getting user books");
        }
    }
}
