package com.example.travelapp.service;

import com.example.travelapp.cache.UserCache;
import com.example.travelapp.exception.ErrorMessages;
import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.UserRequestDto;
import com.example.travelapp.model.dto.response.UserResponseDto;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserCache userCache;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user = new User(1L, "Test User", "test@example.com", List.of());
        UserResponseDto dto = new UserResponseDto(1L, "Test User", "test@example.com", List.of());
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(dto);

        // Act
        List<UserResponseDto> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_WhenCached_ShouldReturnFromCache() {
        // Arrange
        Long id = 1L;
        User cachedUser = new User(id, "Cached User", "cached@example.com", List.of());
        UserResponseDto dto = new UserResponseDto(id, "Cached User", "cached@example.com", List.of());

        when(userCache.get(id)).thenReturn(cachedUser);
        when(userMapper.toResponseDto(cachedUser)).thenReturn(dto);

        // Act
        UserResponseDto result = userService.getUserById(id);

        // Assert
        assertEquals(dto, result);
        verify(userCache).get(id);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUserById_WhenNotCached_ShouldFetchFromDbAndCache() {
        // Arrange
        Long id = 1L;
        User dbUser = new User(id, "DB User", "db@example.com", List.of());
        UserResponseDto dto = new UserResponseDto(id, "DB User", "db@example.com", List.of());

        when(userCache.get(id)).thenReturn(null);
        when(userRepository.findById(id)).thenReturn(Optional.of(dbUser));
        when(userMapper.toResponseDto(dbUser)).thenReturn(dto);

        // Act
        UserResponseDto result = userService.getUserById(id);

        // Assert
        assertEquals(dto, result);
        verify(userCache).put(id, dbUser);
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(userCache.get(id)).thenReturn(null);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(id));

        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void createUser_ShouldSaveAndCacheUser() {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto("New User", "new@example.com");
        User user = new User(null, "New User", "new@example.com", List.of());
        User savedUser = new User(1L, "New User", "new@example.com", List.of());
        UserResponseDto responseDto = new UserResponseDto(1L, "New User", "new@example.com", List.of());

        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.createUser(requestDto);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).save(user);
        verify(userCache).put(savedUser.getId(), savedUser);
    }

    @Test
    void updateUser_ShouldUpdateAndCacheUser() {
        // Arrange
        Long id = 1L;
        UserRequestDto requestDto = new UserRequestDto("Updated User", "updated@example.com");
        User existingUser = new User(id, "Old User", "old@example.com", List.of());
        UserResponseDto responseDto = new UserResponseDto(id, "Updated User", "updated@example.com", List.of());

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toResponseDto(existingUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.updateUser(id, requestDto);

        // Assert
        assertEquals("Updated User", existingUser.getName());
        assertEquals("updated@example.com", existingUser.getEmail());
        assertEquals(responseDto, result);
        verify(userCache).put(id, existingUser);
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        UserRequestDto requestDto = new UserRequestDto("Test User", "test@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(id, requestDto));

        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteAndRemoveFromCache() {
        // Arrange
        Long id = 1L;

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository).deleteById(id);
        verify(userCache).remove(id);
    }
}