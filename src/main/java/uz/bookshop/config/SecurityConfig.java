package uz.bookshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import uz.bookshop.jwt_utils.JwtTokenFilter;
import uz.bookshop.jwt_utils.JwtTokenProvider;
import uz.bookshop.service.impl.CustomUserDetailServiceImpl;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailServiceImpl customUserDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailService);
        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/docs/**",
                                        "/docs",
                                        "/error",
                                        "/auth/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS
                        )
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter1() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
