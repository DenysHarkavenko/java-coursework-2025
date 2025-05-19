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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceSearchAndBookingIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ServiceCenterRepository centerRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ServiceCenterServiceTypeRepository centerServiceRepository;

    @Autowired
    private AppointmentSlotRepository slotRepository;

    @Autowired
    private ServiceRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String userToken;

    private UUID centerId;
    private UUID userId;
    private UUID oilChangeServiceId;
    private UUID slotId;

    @BeforeEach
    void setup() {
        requestRepository.deleteAll();
        slotRepository.deleteAll();
        centerServiceRepository.deleteAll();
        serviceTypeRepository.deleteAll();
        centerRepository.deleteAll();
        userRepository.deleteAll();


        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .username("testuser")
                .role(Role.ADMIN)
                .phone("123456789")
                .createdAt(LocalDateTime.now())
                .build());
        userId = user.getUserId();

        ServiceCenterEntity center = centerRepository.save(
                ServiceCenterEntity.builder()
                        .name("Best Auto Service")
                        .street("123 Main St")
                        .city("Kyiv")
                        .region("Kyiv")
                        .country("Ukraine")
                        .rating(4.8)
                        .build()
        );
        centerId = center.getCenterId();

        ServiceTypeEntity oilChange = serviceTypeRepository.save(
                ServiceTypeEntity.builder()
                        .name("Oil Change")
                        .description("Engine oil replacement")
                        .build()
        );
        oilChangeServiceId = oilChange.getServiceId();

        serviceTypeRepository.save(
                ServiceTypeEntity.builder()
                        .name("Tire Rotation")
                        .description("Tire replacement and balancing")
                        .build()
        );

        centerServiceRepository.save(
                ServiceCenterServiceTypeEntity.builder()
                        .centerId(centerId)
                        .serviceId(oilChangeServiceId)
                        .build()
        );

        AppointmentSlotEntity slot = slotRepository.save(
                AppointmentSlotEntity.builder()
                        .centerId(centerId)
                        .dateTime(LocalDateTime.now().plusDays(1))
                        .isAvailable(true)
                        .build()
        );
        slotId = slot.getSlotId();

        userToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Test
    void shouldFindCentersById() {
        webTestClient.get()
                .uri("/api/v1/center-services/center/{centerId}", centerId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceCenterServiceTypeDTO.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<ServiceCenterServiceTypeDTO> services = response.getResponseBody();
                    assertThat(services).isNotNull();
                    assertThat(services.get(0).getServiceId()).isEqualTo(oilChangeServiceId);
                });
    }

    @Test
    void shouldNotFindCentersForNonExistingService() {
        UUID invalidId = UUID.randomUUID();
        webTestClient.get()
                .uri("/api/v1/center-services/center/{invalidId}", invalidId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ServiceCenterServiceTypeDTO.class)
                .hasSize(0);
    }

    @Test
    void shouldBookAvailableSlot() {
        webTestClient.get()
                .uri("/api/v1/slots/{slotId}", slotId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.available").isEqualTo(true);

        webTestClient.post()
                .uri("/api/v1/requests")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ServiceRequestDTO.builder()
                        .userId(userId)
                        .centerId(centerId)
                        .serviceType("Oil Change")
                        .requestDate(LocalDateTime.now())
                        .appointmentSlotId(slotId)
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ServiceRequestDTO.class)
                .consumeWith(response -> {
                    ServiceRequestDTO request = response.getResponseBody();
                    assertThat(request).isNotNull();
                    assertThat(request.getStatus()).isEqualTo(ServiceRequestStatus.PENDING);
                    assertThat(request.getAppointmentSlotId()).isEqualTo(slotId);
                });

        webTestClient.get()
                .uri("/api/v1/slots/{slotId}", slotId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.available").isEqualTo(false);
    }
}