package com.university.coursework.domain;

import com.university.coursework.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID user_id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Role role;
}