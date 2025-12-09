// java
package com.its.iam.controller;

import com.its.iam.dto.request.UpdateUserRequest;
import com.its.iam.dto.response.ApiResponse;
import com.its.iam.dto.response.UserDto;
import com.its.iam.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<UserDto> getUser(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);
        UserDto user = userService.getUser(id);
        return ApiResponse.<UserDto>builder()
                .result(user)
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<UserDto> getUserProfile(HttpServletRequest request) {
        log.info("Getting user profile from token");
        UserDto user = userService.getUserProfileFromToken(request);
        return ApiResponse.<UserDto>builder()
                .result(user)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<UserDto>> getAllUsers() {
        log.info("Getting all users");
        List<UserDto> users = userService.getAllUsers();
        return ApiResponse.<List<UserDto>>builder()
                .result(users)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);
        UserDto updatedUser = userService.updateUser(id, request);
        return ApiResponse.<UserDto>builder()
                .result(updatedUser)
                .build();
    }

    @PutMapping("/profile")
    public ApiResponse<UserDto> updateUserProfile(
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        log.info("User updating their profile from token");
        UserDto updatedUser = userService.updateUserProfileFromToken(httpRequest, request);
        return ApiResponse.<UserDto>builder()
                .result(updatedUser)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.<String>builder()
                .result("IAM service is running")
                .build();
    }
}