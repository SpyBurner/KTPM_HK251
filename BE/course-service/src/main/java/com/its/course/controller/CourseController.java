package com.its.course.controller;

import com.its.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Course management REST controller
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @GetMapping
    public ApiResponse<List<String>> getAllCourses() {
        System.out.println("Getting all courses");
        List<String> courses = new ArrayList<>();
        courses.add("Java Programming");
        courses.add("Spring Boot Microservices");
        return ApiResponse.success(courses, "Courses retrieved successfully");
    }
    
    @GetMapping("/{id}")
    public ApiResponse<String> getCourseById(@PathVariable Long id) {
        System.out.println("Getting course with id: " + id);
        return ApiResponse.success("Course " + id, "Course retrieved successfully");
    }
    
    @PostMapping
    public ApiResponse<String> createCourse(@RequestBody String courseData) {
        System.out.println("Creating new course");
        return ApiResponse.success("Course created successfully", "Course created");
    }
    
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("Course service is running", "Health check passed");
    }
}