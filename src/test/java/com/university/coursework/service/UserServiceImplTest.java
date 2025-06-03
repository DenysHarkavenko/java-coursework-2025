package com.university.coursework.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.university.coursework.domain.UserDTO;
import com.university.coursework.entity.UserEntity;
import com.university.coursework.exception.UserNotFoundException;
import com.university.coursework.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;

import com.university.coursework.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;

    private UUID userId;
    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userId = UUID.randomUUID();

        userEntity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .phone("123456789")
                .role("USER")
                .createdAt(System.currentTimeMillis())
                .build();

        userDTO = UserDTO.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .phone("123456789")
                .role("USER")
                .build();
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPhone(), result.getPhone());
        assertEquals(userDTO.getRole(), result.getRole());

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        UserEntity savedEntity = captor.getValue();
        assertEquals(userDTO.getName(), savedEntity.getName());
        assertEquals(userDTO.getEmail(), savedEntity.getEmail());
        assertEquals(userDTO.getPhone(), savedEntity.getPhone());
        assertEquals(userDTO.getRole(), savedEntity.getRole());
    }

    @Test
    public void testGetUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getName(), result.getName());
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getPhone(), result.getPhone());
        assertEquals(userEntity.getRole(), result.getRole());

        verify(userRepository).findById(userId);
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(userId));
        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    public void testUpdateUser_Success() {
        // Создаем DTO с измененными значениями
        UserDTO updateDTO = UserDTO.builder()
                .id(userId)
                .name("Jane Doe")
                .email("john@example.com") // Email не меняется
                .phone("987654321")
                .role("USER")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = userService.updateUser(userId, updateDTO);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("987654321", result.getPhone());
        // Email и роль остаются прежними
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testUpdateUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(userId, userDTO));
        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    public void testDeleteUser_Success() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(userId));
        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    public void testExistsByEmail() {
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userService.existsByEmail(email);
        assertTrue(exists);

        verify(userRepository).existsByEmail(email);
    }

    @Test
    public void testFindByEmail_Success() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        UserDTO result = userService.findByEmail(email);

        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getName(), result.getName());
        assertEquals(userEntity.getEmail(), result.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testFindByEmail_NotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.findByEmail(email));
        assertEquals("User not found with email: " + email, exception.getMessage());
    }
}
