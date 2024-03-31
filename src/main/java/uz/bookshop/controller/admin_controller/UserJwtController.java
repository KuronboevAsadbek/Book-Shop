package uz.bookshop.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.controller.admin_controller.VM.LoginVM;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.service.UserService;

import java.security.Principal;
import java.util.Set;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@Slf4j
public class UserJwtController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(REGISTER)
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDto userRequestDto) {

        userRequestDto.setRoles(
                Set.of(
                        RoleResponseDTO.builder()
                                .id(9L)
                                .build()
                )
        );
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginVM loginVM) {
        return new ResponseEntity<>(userService.login(loginVM), HttpStatus.OK);
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<?> logout(Principal principal) {
        userService.logout(principal.getName());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping(REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(jwtTokenProvider.refreshToken(refreshToken));
    }


    @PutMapping(UPDATE)
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDto userRequestDto, Long id) {
        return ResponseEntity.ok(userService.updateUser(userRequestDto, id));
    }
}
