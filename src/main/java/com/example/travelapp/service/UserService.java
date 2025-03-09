package com.example.travelapp.service;


import com.example.travelapp.exception.EntityNotFoundException;
import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.UserRequestDto;
import com.example.travelapp.model.dto.response.UserResponseDto;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.service.mapper.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
