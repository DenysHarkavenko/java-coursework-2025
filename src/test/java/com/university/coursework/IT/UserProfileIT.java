package com.university.coursework.IT;

import com.university.coursework.domain.*;
import com.university.coursework.domain.enums.Role;
import com.university.coursework.entity.*;
import com.university.coursework.repository.*;
import com.university.coursework.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserProfileIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private UserEntity user;
    private UUID userId;
    private String userToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = userRepository.save(UserEntity.builder()
                .email("user@example.com")
                .password("oldPassword")
                .username("oldUsername")
                .phone("+380501234567")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build());
        userId = user.getUserId();
        userToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Test
    void shouldUpdateProfile() {
        webTestClient.put()
                .uri("/api/v1/users/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserDTO.builder()
                        .email(user.getEmail())
                        .username("newUsername")
                        .role(user.getRole())
                        .phone("+380991234567")
                        .password(user.getPassword())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                    UserDTO updatedUser = response.getResponseBody();
                    assertThat(updatedUser.getUsername()).isEqualTo("newUsername");
                    assertThat(updatedUser.getPhone()).isEqualTo("+380991234567");
                });
    }


    @Test
    void shouldRejectInvalidEmail() {
        webTestClient.put()
                .uri("/api/v1/users/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserDTO.builder()
                        .email("invalid email")
                        .username("newUsername")
                        .role(user.getRole())
                        .phone("380991234567")
                        .password(user.getPassword())
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldPreventUnauthorizedAccess() {
        UserEntity otherUser = userRepository.save(UserEntity.builder()
                .email("other@example.com")
                .username("otherUser")
                .phone("+380501234568")
                .createdAt(LocalDateTime.now())
                .password("password")
                .role(Role.USER)
                .build());
        String otherUserToken = jwtTokenProvider.createToken(otherUser.getEmail(), otherUser.getRole());

        webTestClient.put()
                .uri("/api/v1/users/{userId}", userId)
                .header("Authorization", "Bearer " + otherUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void shouldGetUserProfile() {
        webTestClient.get()
                .uri("/api/v1/users/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                    UserDTO profile = response.getResponseBody();
                    assertThat(profile.getUserId()).isEqualTo(userId);
                    assertThat(profile.getUsername()).isEqualTo("oldUsername");
                });
    }
}