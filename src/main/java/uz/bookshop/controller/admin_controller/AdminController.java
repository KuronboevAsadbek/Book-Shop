package uz.bookshop.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bookshop.domain.dto.request_dto.RoleRequestDTO;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.service.RoleService;
import uz.bookshop.service.UserService;

import static uz.bookshop.utils.Endpoint.*;

@RestController
@RequestMapping(ADMIN)
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping(REGISTER)
    @PreAuthorize("hasAuthority('CAN_MANAGE_USERS')")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PostMapping(ADD_ROLE)
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<RoleResponseDTO> addRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.ok(roleService.createRole(roleRequestDTO));
    }

    @PutMapping(UPDATE_USER)
    @PreAuthorize("hasAuthority('CAN_MANAGE_USERS')")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDto userRequestDto, Long id) {
        return ResponseEntity.ok(userService.updateUser(userRequestDto, id));
    }
}
