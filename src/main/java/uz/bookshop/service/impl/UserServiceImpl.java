package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bookshop.controller.admin_controller.VM.LoginVM;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.LoginResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.UserException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.UserMapper;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDTO login(LoginVM loginVM) {
        User user = userRepository.findByUsername(loginVM.getUserName().trim().toLowerCase());
        if (user == null) {
            throw new UserException("Bu foydalanuvch mavjud emas");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVM.getUserName().trim().toLowerCase(), loginVM.getPassword()));
        return jwtTokenProvider.createToken(user.getUsername().trim().toLowerCase(), loginVM.isRememberMe());
    }


    @Override
    public UserResponseDTO createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        if (Boolean.TRUE.equals(checkPassword(userRequestDto.getPassword()))) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        } else {
            throw new UserException("Password is too short");
        }
        user.setUsername(userRequestDto.getUsername().trim().toLowerCase());
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }


    @Override
    public UserResponseDTO updateUser(UserRequestDto userRequestDto, Long userId) {
        UserResponseDTO userResponseDto;
        User user;
        user = userRepository.findById(userId).orElseThrow(() -> new UserException("User is not found"));
        if (Boolean.TRUE.equals(checkPassword(userRequestDto.getPassword()))) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        } else {
            throw new UserException(("Password is too short!"));
        }

        userMapper.updateFromDto(userRequestDto, user);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setUsername(userRequestDto.getUsername().trim().toLowerCase());
        userResponseDto = userMapper.toDto(user);
        userRepository.save(user);
        return userResponseDto;
    }

    @Override
    public UserResponseDTO updateUserByAdmin(UserRequestDto userRequestDto, Long userId) {
        return null;
    }

    @Override
    public Boolean checkUserName(String userName) {
        try {
            return userRepository.existsByUsername(userName);
        } catch (Exception e) {
            throw new UserException("User is not found!");
        }
    }

    @Override
    public Boolean checkPassword(String password) {
        return password.length() >= 4;
    }


    @Override
    public void logout(String userName) {
        try {
            User user = userRepository.findByUsername(userName);
            if (user == null) {
                throw new UserException(("User is not found"));
            }
            user.setRefreshToken(null);
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserException(("User is not found"));
        }

    }
}
