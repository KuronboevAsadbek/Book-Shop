package uz.bookshop.controller.user_controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.LoginRequestDto;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.service.UserService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@Slf4j
public class UserJwtController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(REGISTER)
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto, "ROLE_USER"));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        return new ResponseEntity<>(userService.login(requestDto), HttpStatus.OK);
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping(REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(jwtTokenProvider.refreshToken(refreshToken));
    }


    @PutMapping(UPDATE)
    @PreAuthorize("hasAnyAuthority('USER_ACCESS')")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.updateUser(userRequestDto, "ROLE_USER"));
    }
}
