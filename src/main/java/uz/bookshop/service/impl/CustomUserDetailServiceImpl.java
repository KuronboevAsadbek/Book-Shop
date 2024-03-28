package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.bookshop.domain.model.User;
import uz.bookshop.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles().stream().map(
                        role -> role.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                                .collect(Collectors.toList())
                ).flatMap(Collection::stream).collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return !user.getIsDeleted();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.getIsActive();
            }
        };
    }
}
