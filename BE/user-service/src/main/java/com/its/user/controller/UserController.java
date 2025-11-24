package com.its.user.controller;

import com.its.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User management REST controller
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public ApiResponse<List<String>> getAllUsers() {
        System.out.println("Getting all users");
        List<String> users = new ArrayList<>();
        users.add("User 1");
        users.add("User 2");
        return ApiResponse.success(users, "Users retrieved successfully");
    }
    
    @GetMapping("/{id}")
    public ApiResponse<String> getUserById(@PathVariable Long id) {
        System.out.println("Getting user with id: " + id);
        return ApiResponse.success("User " + id, "User retrieved successfully");
    }
    
    @PostMapping
    public ApiResponse<String> createUser(@RequestBody String userData) {
        System.out.println("Creating new user");
        return ApiResponse.success("User created successfully", "User created");
    }
    
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("User service is running", "Health check passed");
    }
}