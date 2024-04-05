package uz.bookshop.domain.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private Set<PermissionRequestDTO> permissions;
}
