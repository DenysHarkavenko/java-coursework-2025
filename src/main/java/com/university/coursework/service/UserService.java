package com.university.coursework.service;

import com.university.coursework.domain.UserDTO;
import com.university.coursework.exception.UserNotFoundException;

import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(UUID id) throws UserNotFoundException;
    UserDTO updateUser(UUID id, UserDTO userDTO) throws UserNotFoundException;
    void deleteUser(UUID id) throws UserNotFoundException;
    boolean existsByEmail(String email);
    UserDTO findByEmail(String email) throws UserNotFoundException;
}
