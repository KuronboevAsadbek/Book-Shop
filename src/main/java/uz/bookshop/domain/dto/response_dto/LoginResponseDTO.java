package uz.bookshop.domain.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private Set<RoleResponseDTO> roles;

    private String token;

    private String refreshToken;
}
