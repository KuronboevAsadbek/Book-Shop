package uz.bookshop.service;


import uz.bookshop.controller.admin_controller.VM.LoginVM;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.LoginResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;

public interface UserService {
    LoginResponseDTO login(LoginVM loginVM);

    UserResponseDTO createUser(UserRequestDto userRequestDto);

    UserResponseDTO updateUser(UserRequestDto userRequestDto, Long userId);

    Boolean checkUserName(String userName);

    Boolean checkPassword(String password);


    void logout(String userName);

}
