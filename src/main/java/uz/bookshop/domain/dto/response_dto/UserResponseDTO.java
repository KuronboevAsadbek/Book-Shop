package uz.bookshop.domain.dto.response_dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<RoleResponseDTO> roles;
    private Set<PermissionResponseDTO> permissions;
}
