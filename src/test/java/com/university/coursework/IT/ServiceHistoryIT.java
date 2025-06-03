package com.university.coursework.IT;

import com.university.coursework.domain.*;
import com.university.coursework.domain.enums.Role;
import com.university.coursework.domain.enums.ServiceRequestStatus;
import com.university.coursework.entity.*;
import com.university.coursework.repository.*;
import com.university.coursework.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
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

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceHistoryIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ServiceRequestRepository requestRepository;

    @Autowired
    private AppointmentSlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCenterRepository centerRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private UUID userId;
    private UUID centerId;
    private UUID slotId;
    private String userToken;
    private UUID requestId;

    @BeforeEach
    void setup() {
        requestRepository.deleteAll();
        slotRepository.deleteAll();
        userRepository.deleteAll();
        centerRepository.deleteAll();
        serviceTypeRepository.deleteAll();


        UserEntity user = userRepository.save(UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .username("testuser")
                .role(Role.ADMIN)
                .phone("+380501234567")
                .createdAt(LocalDateTime.now())
                .build());
        userId = user.getUserId();
        userToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        ServiceCenterEntity center = centerRepository.save(
                ServiceCenterEntity.builder()
                        .name("AutoService Center")
                        .street("Main St")
                        .city("Kyiv")
                        .region("Kyiv")
                        .country("Ukraine")
                        .description("Best auto service in town")
                        .rating(4.5)
                        .build());
        centerId = center.getCenterId();

        AppointmentSlotEntity slot = slotRepository.save(
                AppointmentSlotEntity.builder()
                        .centerId(centerId)
                        .dateTime(LocalDateTime.now().plusDays(1))
                        .isAvailable(true)
                        .build()
        );
        slotId = slot.getSlotId();

        ServiceTypeEntity service = serviceTypeRepository.save(
                ServiceTypeEntity.builder()
                        .name("Oil Change")
                        .build());

        ServiceRequestEntity request = requestRepository.save(
                ServiceRequestEntity.builder()
                        .userId(userId)
                        .centerId(center.getCenterId())
                        .serviceType(service.getName())
                        .requestDate(LocalDateTime.now().minusDays(1))
                        .status(ServiceRequestStatus.COMPLETED)
                        .appointmentSlotId(slotId)
                        .build());
        requestId = request.getRequestId();
    }

    @Test
    void shouldGetUserServiceHistory() {
        webTestClient.get()
                .uri("/api/v1/requests/users/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceRequestDTO.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<ServiceRequestDTO> history = response.getResponseBody();
                    assertThat(history.get(0).getStatus()).isEqualTo(ServiceRequestStatus.COMPLETED);
                });
    }

    @Test
    void shouldFilterHistoryByStatus() {
        requestRepository.save(
                ServiceRequestEntity.builder()
                        .userId(userId)
                        .centerId(centerId)
                        .serviceType("Brake Inspection")
                        .requestDate(LocalDateTime.now())
                        .status(ServiceRequestStatus.PENDING)
                        .appointmentSlotId(slotId)
                        .build());

        webTestClient.get()
                .uri("/api/v1/requests/users/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceRequestDTO.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<ServiceRequestDTO> history = response.getResponseBody();
                    assertThat(history.get(0).getStatus()).isEqualTo(ServiceRequestStatus.COMPLETED);
                });
    }

    @Test
    void shouldGetRequestDetails() {
        log.info("* Отправляемый токен: " + userToken);

        webTestClient.get()
                .uri("/api/v1/requests/{requestId}", requestId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ServiceRequestDTO.class)
                .consumeWith(response -> {
                    ServiceRequestDTO details = response.getResponseBody();
                    assertThat(details.getRequestId()).isEqualTo(requestId);
                });
    }

    @Test
    void shouldPreventAccessToOtherUsersHistory() {
        UserEntity otherUser = userRepository.save(UserEntity.builder()
                .email("other@example.com")
                .username("otheruser")
                .password("password")
                .role(Role.USER)
                .password("password")
                .createdAt(LocalDateTime.now())
                .build());
        String otherUserToken = jwtTokenProvider.createToken(otherUser.getEmail(), otherUser.getRole());

        webTestClient.get()
                .uri("/api/v1/requests/{requestId}", userId)
                .header("Authorization", "Bearer " + otherUserToken)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}