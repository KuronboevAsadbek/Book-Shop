package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.request_dto.LoginRequestDto;
import uz.bookshop.domain.dto.request_dto.UserRequestDto;
import uz.bookshop.domain.dto.response_dto.LoginResponseDTO;
import uz.bookshop.domain.dto.response_dto.UserResponseDTO;
import uz.bookshop.domain.model.Role;
import uz.bookshop.domain.model.User;
import uz.bookshop.exception.UserException;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.mapping.RoleMapper;
import uz.bookshop.mapping.UserMapper;
import uz.bookshop.repository.RoleRepository;
import uz.bookshop.repository.UserRepository;
import uz.bookshop.service.UserService;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUserName().trim().toLowerCase());
        if (user == null) {
            throw new UserException("Bu foydalanuvch mavjud emas");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUserName()
                .trim().toLowerCase(), requestDto.getPassword()));
        return jwtTokenProvider.createToken(user.getUsername().trim().toLowerCase(), requestDto.isRememberMe());
    }


    @Override
    public UserResponseDTO createUser(UserRequestDto userRequestDto, String roleName) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName(roleName).orElseThrow(
                () -> new UserException("Role is not found")
        ));
        User user = userMapper.toEntity(userRequestDto);
        assert role.isPresent();
        user.setRoles(Set.of(role.get()));
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
    public UserResponseDTO updateUser(UserRequestDto requestDto) {
        if (requestDto.getId() == null) {
            throw new UserException("User id is not found");
        }
        if (Boolean.FALSE.equals(checkPassword(requestDto.getPassword()))) {
            throw new UserException(("Password is too short!"));
        }
        User oldUser = userRepository.findById(requestDto.getId()).orElseThrow(() -> new UserException("User is not found"));
        User newUser = userMapper.toEntity(requestDto);

        userMapper.updateFromDto(requestDto, oldUser);
        newUser.setPassword(
                checkPassword(
                        newUser.getPassword()) ? passwordEncoder.encode(newUser.getPassword()) : oldUser.getPassword());
        newUser.setUsername(requestDto.getUsername().trim().toLowerCase());
        userRepository.save(oldUser);
        return UserResponseDTO
                .builder()
                .id(oldUser.getId())
                .firstName(oldUser.getFirstName())
                .lastName(oldUser.getLastName())
                .username(oldUser.getUsername())
                .roles(roleMapper.toDto(oldUser.getRoles()))
                .build();

    }

    @Override
    public UserResponseDTO updateUser(UserRequestDto userRequestDto, String roleName) {
        userRequestDto.setRoles(
                Set.of(
                        roleMapper.toDto(
                                roleRepository.findByName(roleName).orElseThrow(() -> new UserException("Role is not found"))
                        )
                )
        );
        return updateUser(userRequestDto);
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
    public void logout() {
        String username = JwtTokenProvider.getCurrentUser();
        if (username == null) {
            throw new UserException("User is not found");
        }
        try {
            User user = userRepository.findByUsername(username);
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
