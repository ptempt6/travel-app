package com.example.travelapp.service;


import com.example.travelapp.cache.UserCache;
import com.example.travelapp.exception.ErrorMessages;
import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.UserRequestDto;
import com.example.travelapp.model.dto.response.UserResponseDto;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.service.mapper.UserMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCache userCache;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserCache userCache) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCache = userCache;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUserById(Long id) {
        User cachedUser = userCache.get(id);

        if (cachedUser != null) {
            return userMapper.toResponseDto(cachedUser);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND));

        userCache.put(id, user);
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        userCache.put(savedUser.getId(), savedUser);
        return userMapper.toResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new NotFoundException(ErrorMessages.USER_NOT_FOUND));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        User savedUser = userRepository.save(user);
        userCache.put(savedUser.getId(), savedUser);

        return userMapper.toResponseDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        userCache.remove(id);
    }
}
