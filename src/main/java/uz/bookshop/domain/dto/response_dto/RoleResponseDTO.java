package uz.bookshop.domain.dto.response_dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleResponseDTO {

    private Long id;
    private String name;
    private Set<PermissionResponseDTO> permissions;
}
