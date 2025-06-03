package com.university.coursework.IT;

import com.university.coursework.domain.ServiceCenterDTO;
import com.university.coursework.domain.enums.Role;
import com.university.coursework.entity.ServiceCenterEntity;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.repository.ServiceCenterRepository;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ServiceCenterControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ServiceCenterRepository centerRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private String userToken;

    @BeforeEach
    void setup() {
        centerRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .username("testuser")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build());


        userToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Test
    void shouldGetTopRatedCenters() {
        centerRepository.save(ServiceCenterEntity.builder()
                .name("Center A")
                .street("123 Main St")
                .city("Kyiv")
                .region("Kyiv")
                .country("Ukraine")
                .description("Best service center in town")
                .rating(4.8)
                .build());

        centerRepository.save(ServiceCenterEntity.builder()
                .name("Center B")
                .street("456 Second St")
                .city("Kyiv")
                .region("Kyiv")
                .country("Ukraine")
                .description("Second best service center")
                .rating(4.5)
                .build());

        webTestClient.get()
                .uri("/api/v1/centers/top-rated")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceCenterDTO.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<ServiceCenterDTO> centers = response.getResponseBody();
                    assertThat(centers).isNotNull();
                    assertThat(centers.size()).isEqualTo(2);
                    assertThat(centers.get(0).getRating()).isGreaterThanOrEqualTo(centers.get(1).getRating());
                });
    }

    @Test
    void shouldReturnEmptyListWhenNoCenters() {
        webTestClient.get()
                .uri("/api/v1/centers/top-rated")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceCenterDTO.class)
                .hasSize(0);
    }
}