package uz.bookshop.controller.admin_controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.exception.UserException;
import uz.bookshop.service.UserService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(ADMIN)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Controller")
public class AdminController {

    private final UserService userService;

    @PostMapping(REGISTER)
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDto userRequestDto) {
        if (isSuperAdminOrAdmin(userRequestDto)) {
            throw new UserException("You can't create user with this role");
        }
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PutMapping(UPDATE)
    @PreAuthorize("hasAnyAuthority('ADMIN_ACCESS', 'FULL_ACCESS')")

    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDto userRequestDto) {
        if (isSuperAdminOrAdmin(userRequestDto)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.updateUser(userRequestDto));
    }

    private boolean isSuperAdminOrAdmin(UserRequestDto userRequestDto) {
        //if name no t null
        if (userRequestDto.getRoles() != null) {
            if (userRequestDto.getRoles().stream().map(
                    role -> role.getName() != null && (role.getName().equals("SUPER_ADMIN") || role.getName().equals("ADMIN"))
            ).findFirst().orElse(false)) {
                return true;
            }
            else return userRequestDto.getRoles().stream().map(
                    role -> role.getId() != null && (role.getId() == 1 || role.getId() == 2)
            ).findFirst().orElse(false);
        }
        return false;
    }
}