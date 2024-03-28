package uz.bookshop.domain.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("name")
    private String name;

    @NotNull
    @NotBlank
    @JsonProperty("permissions")
    private Set<PermissionRequestDTO> permissions;
}
