// java
package com.its.iam.controller;

import com.its.iam.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ApiResponse<List<String>> getAllCourses() {
        System.out.println("Getting all courses");
        List<String> courses = new ArrayList<>();
        courses.add("Java Programming");
        courses.add("Spring Boot Microservices");
        return ApiResponse.<List<String>>builder().result(courses).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<String> getCourseById(@PathVariable Long id) {
        System.out.println("Getting course with id: " + id);
        return ApiResponse.<String>builder().result("Course " + id).build();
    }

    @PostMapping
    public ApiResponse<String> createCourse(@RequestBody String courseData) {
        System.out.println("Creating new course");
        return ApiResponse.<String>builder().result("Course created successfully").build();
    }

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.<String>builder().result("Course service is running").build();
    }
}