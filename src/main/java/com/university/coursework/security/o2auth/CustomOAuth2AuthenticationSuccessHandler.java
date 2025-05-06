package com.university.coursework.security.o2auth;

import com.university.coursework.domain.enums.Role;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            var attributes = oauthToken.getPrincipal().getAttributes();

            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");

            if (email != null && !userRepository.existsByEmail(email)) {
                UserEntity user = UserEntity.builder()
                        .username(name != null ? name : email.split("@")[0])
                        .email(email)
                        .password(UUID.randomUUID().toString())
                        .createdAt(LocalDateTime.now())
                        .role(Role.USER)
                        .build();

                userRepository.save(user);
            }
        }

        super.clearAuthenticationAttributes(request);
        super.getRedirectStrategy().sendRedirect(request, response, "/api/v1/auth/me");
    }
}