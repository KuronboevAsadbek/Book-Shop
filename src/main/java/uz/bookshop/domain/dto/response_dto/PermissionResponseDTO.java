package uz.bookshop.domain.dto.response_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponseDTO {

    @NotNull
    @NotBlank
    @JsonProperty("id")
    private Long id;

    @NotNull
    @NotBlank
    @JsonProperty("name")
    private String name;
}
