package uz.bookshop.service;


import uz.bookshop.domain.dto.request_dto.LoginRequestDto;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.LoginResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;

public interface UserService {
    LoginResponseDTO login(LoginRequestDto requestDto);

    UserResponseDTO createUser(UserRequestDto userRequestDto, String roleName);

    UserResponseDTO createUser(UserRequestDto userRequestDto);

    UserResponseDTO updateUser(UserRequestDto userRequestDto);

    UserResponseDTO updateUser(UserRequestDto userRequestDto, String roleName);

    UserResponseDTO updateUserByAdmin(UserRequestDto userRequestDto, Long userId);

    Boolean checkUserName(String userName);

    Boolean checkPassword(String password);


    void logout();

}
