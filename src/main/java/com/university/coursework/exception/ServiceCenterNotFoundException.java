package com.university.coursework.exception;

public class ServiceCenterNotFoundException extends RuntimeException {
    public ServiceCenterNotFoundException(String message) {
        super(message);
    }
}
