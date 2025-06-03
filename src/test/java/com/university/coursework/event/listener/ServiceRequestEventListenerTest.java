package com.university.coursework.event.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.domain.enums.ServiceRequestStatus;
import com.university.coursework.entity.NotificationEntity;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.event.ServiceRequestEvent;
import com.university.coursework.exception.UserNotFoundException;
import com.university.coursework.repository.NotificationRepository;
import com.university.coursework.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ServiceRequestEventListenerTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private ServiceRequestEventListener eventListener;

    private ServiceRequestDTO requestDTOCreated;
    private ServiceRequestDTO requestDTOStatusChanged;
    private ServiceRequestEvent eventCreated;
    private ServiceRequestEvent eventStatusChanged;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        eventListener = new ServiceRequestEventListener(mailSender, userRepository, notificationRepository);
        ReflectionTestUtils.setField(eventListener, "fromEmail", "stud49507@gmail.com");

        UUID userId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        UUID centerId = UUID.randomUUID();
        UUID slotId = UUID.randomUUID();

        requestDTOCreated = ServiceRequestDTO.builder()
                .requestId(requestId)
                .userId(userId)
                .centerId(centerId)
                .serviceType("Oil Change")
                .requestDate(LocalDateTime.now())
                .status(ServiceRequestStatus.PENDING)
                .appointmentSlotId(slotId)
                .build();
        eventCreated = new ServiceRequestEvent(this, requestDTOCreated, "CREATED");

        requestDTOStatusChanged = requestDTOCreated.toBuilder()
                .status(ServiceRequestStatus.CONFIRMED)
                .build();
        eventStatusChanged = new ServiceRequestEvent(this, requestDTOStatusChanged, "STATUS_CHANGED");

        userEntity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    }

    @Test
    public void testHandleServiceRequestCreatedEvent() {
        eventListener.handleServiceRequestEvent(eventCreated);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("Ваш запит на обслуговування створено", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Ваш запит на послугу"));
        assertTrue(sentMessage.getText().contains("Статус: " + requestDTOCreated.getStatus()));
        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }

    @Test
    public void testHandleServiceRequestStatusChangedEvent() {
        eventListener.handleServiceRequestEvent(eventStatusChanged);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("Статус Вашого запиту змінено", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("було змінено на " + requestDTOStatusChanged.getStatus()));
        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
    }

    @Test
    public void testHandleServiceRequestEvent_UserNotFound() {
        when(userRepository.findById(requestDTOCreated.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            eventListener.handleServiceRequestEvent(eventCreated);
        });
    }
}
