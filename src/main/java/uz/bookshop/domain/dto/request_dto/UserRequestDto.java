package uz.bookshop.domain.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;

import java.util.Set;

@Data
@Builder
public class UserRequestDto {

    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 3)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4)
    private String password;

    private Set<RoleResponseDTO> roles;
}
