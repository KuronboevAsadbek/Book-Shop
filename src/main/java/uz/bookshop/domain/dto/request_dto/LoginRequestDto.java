package uz.bookshop.domain.dto.request_dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDto {

    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private boolean rememberMe;
}
