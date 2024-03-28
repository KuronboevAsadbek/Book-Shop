package uz.bookshop.domain.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionRequestDTO {

    @NotNull
    @NotBlank
    @JsonProperty("id")
    private Long id;
}
