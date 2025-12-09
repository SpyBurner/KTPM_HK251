package com.its.iam.service;

import com.its.iam.dto.request.UpdateUserRequest;
import com.its.iam.dto.response.UserDto;
import com.its.iam.entity.User;
import com.its.iam.exception.AppException;
import com.its.iam.exception.ErrorCode;
import com.its.iam.mapper.UserMapper;
import com.its.iam.repository.UserRepository;
import com.its.iam.security.IJwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IJwtService jwtService;

    public UserDto getUser(Long id) {
        log.info("Getting user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.mapToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        log.info("Getting all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if username is being changed and if it already exists
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            user.setUsername(request.getUsername());
        }

        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            user.setEmail(request.getEmail());
        }

        // Update other fields if provided
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.mapToUserDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
        log.info("User with id {} deleted successfully", id);
    }

    public UserDto getUserByUsername(String username) {
        log.info("Getting user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto updateUserProfile(String username, UpdateUserRequest request) {
        log.info("User {} updating their profile", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if username is being changed and if it already exists
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            user.setUsername(request.getUsername());
        }

        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            user.setEmail(request.getEmail());
        }

        // Update other fields if provided
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.mapToUserDto(updatedUser);
    }

    public UserDto getUserProfileFromToken(HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        log.info("Getting profile for user: {}", username);
        return getUserByUsername(username);
    }

    @Transactional
    public UserDto updateUserProfileFromToken(HttpServletRequest request, UpdateUserRequest updateRequest) {
        String username = extractUsernameFromToken(request);
        log.info("User {} updating their profile", username);
        return updateUserProfile(username, updateRequest);
    }

    /**
     * Helper method to extract username from JWT token in Authorization header
     */
    private String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.MISSING_TOKEN);
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return jwtService.extractUsername(token);
    }
}