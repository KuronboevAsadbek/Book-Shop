package uz.bookshop.jwt_utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import uz.bookshop.domain.dto.response_dto.LoginResponseDTO;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.UserException;
import uz.bookshop.mapping.RoleMapper;
import uz.bookshop.repository.UserRepository;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.validity}")
    private long validityMilliSecond;


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public LoginResponseDTO createToken(String userName, Boolean rememberMe) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UserException("User is not exist");
        }
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("roles", user.getRoles());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(user.getId());
        loginResponseDTO.setUsername(user.getUsername());
        loginResponseDTO.setFirstName(user.getFirstName());
        loginResponseDTO.setLastName(user.getLastName());
        loginResponseDTO.setRoles(roleMapper.toDto(user.getRoles()));
        loginResponseDTO.setToken(generateToken(user));
        if (rememberMe) {
            user.setRefreshToken(generateRefreshToken(user));
            loginResponseDTO.setRefreshToken(user.getRefreshToken());
        } else {
            user.setRefreshToken(null);
        }
        userRepository.save(user);
        return loginResponseDTO;
    }

    private String generateRefreshToken(User user) {
        return Base64.getEncoder().encodeToString(generateToken(user).getBytes());
    }

    private String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliSecond);
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getRoles());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        User user = userRepository.findByToken(refreshToken);
        if (user == null) {
            throw new UserException("This user has not used the recall feature");
        }
        return createToken(user.getUsername(), true);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUser(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUser(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Long getCurrentUserId() {
        return userRepository.findByUsername(getCurrentUser()).getId();
    }


}
