package com.university.coursework.event.listener;

import com.university.coursework.domain.ServiceRequestDTO;
import com.university.coursework.event.ServiceRequestEvent;
import com.university.coursework.entity.NotificationEntity;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.exception.UserNotFoundException;
import com.university.coursework.repository.NotificationRepository;
import com.university.coursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceRequestEventListener {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Async
    @EventListener
    public void handleServiceRequestEvent(ServiceRequestEvent event) {
        ServiceRequestDTO requestDTO = event.getServiceRequestDTO();
        String eventType = event.getEventType();

        UserEntity user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDTO.getUserId()));
        String toEmail = user.getEmail();
        String subject;
        String content;

        if ("CREATED".equalsIgnoreCase(eventType)) {
            subject = "Ваш запит на обслуговування створено";
            content = "Доброго дня, " + user.getName() + "!\n\n" +
                    "Ваш запит на послугу \"" + requestDTO.getServiceType() + "\" успішно створено.\n" +
                    "ID запиту: " + requestDTO.getRequestId() + "\n" +
                    "Статус: " + requestDTO.getStatus() + "\n" +
                    "Обраний слот: " + requestDTO.getAppointmentSlotId() + "\n\n" +
                    "Дякуємо за використання нашої системи!";
        } else if ("STATUS_CHANGED".equalsIgnoreCase(eventType)) {
            subject = "Статус Вашого запиту змінено";
            content = "Доброго дня, " + user.getName() + "!\n\n" +
                    "Статус запиту на послугу \"" + requestDTO.getServiceType() + "\" з ID " +
                    requestDTO.getRequestId() + " було змінено на " + requestDTO.getStatus() + ".\n\n" +
                    "Дякуємо за використання нашої системи!";
        } else {
            subject = "Повідомлення щодо запиту";
            content = "Доброго дня, " + user.getName() + "!\n\n" +
                    "Ваш запит має оновлення. Перевірте деталі в системі.";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", toEmail, e.getMessage());
        }

        NotificationEntity notification = NotificationEntity.builder()
                .userId(user.getId())
                .message(content)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }
}
