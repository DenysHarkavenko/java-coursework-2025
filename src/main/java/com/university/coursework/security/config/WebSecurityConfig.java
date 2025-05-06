package com.university.coursework.security.config;

import com.university.coursework.repository.UserRepository;
import com.university.coursework.security.o2auth.CustomOAuth2AuthenticationSuccessHandler;
import com.university.coursework.security.o2auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final CustomOAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html","/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        .requestMatchers("/", "/login", "/register", "/css/**", "/images/**").permitAll()
                        .requestMatchers("/api/v1/centers/top-rated", "/api/v1/centers/{centerId}", "/api/v1/centers").permitAll()
                        .requestMatchers("/api/v1/service-types", "/api/v1/service-types/{serviceId}").permitAll()
                        .requestMatchers("/api/v1/center-services/center/{centerId}").permitAll()
                        .requestMatchers("/api/v1/slots/center/{centerId}").permitAll()
                        .requestMatchers("/api/v1/feedbacks/center/{centerId}").permitAll()

                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/centers/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/service-types/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/center-services/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/slots/**").hasRole("ADMIN")

                        .requestMatchers("/api/v1/requests/**").authenticated()
                        .requestMatchers("/api/v1/feedbacks/**").authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .permitAll())
                .formLogin(form -> form
                        .loginPage("/api/v1/auth/login")
                        .failureHandler((request, response, exception) -> response.sendRedirect("/login?error=true")))
                .logout(logoutSettings -> logoutSettings
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .logoutUrl("/api/v1/auth/logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }
}