package com.university.coursework.controller;

import com.university.coursework.domain.UserDTO;
import com.university.coursework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable UUID userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
