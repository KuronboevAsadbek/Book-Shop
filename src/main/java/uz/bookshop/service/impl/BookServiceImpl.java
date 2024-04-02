package uz.bookshop.service.impl;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.BookException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.BookMapper;
import uz.bookshop.repository.BookRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.BookService;

import java.util.ArrayList;
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
            User user = userRepository.findByUsername(jwtTokenProvider.getCurrentUser());
            Book book = bookMapper.toEntity(bookRequestDTO);
//            book.setPrice(book.getPrice().multiply(BigInteger.valueOf(1000)));
            Book book2 = bookRepository.findByName(book.getName());
            book.setUserId(user.getId());
            if (book2 != null) {
                if (book.getName().equals(book2.getName()) && book2.getUserId().equals(
                        jwtTokenProvider.getCurrentUserId())) {
                    LOG.error("Book already exists update it \t\t {}", gson.toJson(bookRequestDTO));
                    throw new BookException("Book already exists update it");
                }
            }
            book = bookRepository.save(book);
            bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setAuthorName(user.getFirstName());
            bookResponseDTO.setAuthorSurname(user.getLastName());
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
            User user = userRepository.findById(book.getUserId()).orElseThrow(() -> new BookException("User not found"));
            BookResponseDTO bookResponseDTO = bookMapper.toDto(book);
            bookResponseDTO.setAuthorName(user.getFirstName());
            bookResponseDTO.setAuthorSurname(user.getLastName());
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

            String sql = ("""
                    SELECT b.id                 AS id,
                           b.name               AS name,
                           b.price              AS price,
                           b.quantity           AS quantity,
                           u.first_name         AS authorName,
                           u.last_name          AS authorSurname
                    FROM book b
                    JOIN users u ON b.user_id = u.id
                    """);

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            List<BookResponseDTO> bookResponseDTOs = new ArrayList<>();

            for (Object[] result : results) {
                BookResponseDTO bookResponseDTO = new BookResponseDTO();
                bookResponseDTO.setId((Long) result[0]);
                bookResponseDTO.setName((String) result[1]);
                bookResponseDTO.setPrice((Integer) result[2]);
                bookResponseDTO.setQuantity((Integer) result[3]);
                bookResponseDTO.setAuthorName((String) result[4]);
                bookResponseDTO.setAuthorSurname((String) result[5]);

                bookResponseDTOs.add(bookResponseDTO);
            }

            LOG.info("Book updated \t\t {}", gson.toJson(bookResponseDTOs));
            return bookResponseDTOs;
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
}
