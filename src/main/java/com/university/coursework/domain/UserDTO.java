package com.university.coursework.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class UserDTO {
    UUID id;
    String name;
    String email;
    String phone;
    String role;
}