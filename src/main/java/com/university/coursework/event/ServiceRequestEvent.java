package com.university.coursework.event;

import com.university.coursework.domain.ServiceRequestDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ServiceRequestEvent extends ApplicationEvent {
    private final ServiceRequestDTO serviceRequestDTO;
    private final String eventType;

    public ServiceRequestEvent(Object source, ServiceRequestDTO serviceRequestDTO, String eventType) {
        super(source);
        this.serviceRequestDTO = serviceRequestDTO;
        this.eventType = eventType;
    }
}
