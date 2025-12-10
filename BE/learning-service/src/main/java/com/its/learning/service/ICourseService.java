package com.its.learning.service;

import com.its.learning.dto.ApiResponse;
import com.its.learning.dto.request.BatchCourseRequest;
import com.its.learning.dto.response.ChapterDto;
import com.its.learning.dto.response.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "COURSE-SERVICE",
        configuration = com.its.learning.configuration.FeignClientConfig.class
)
public interface ICourseService {

    @GetMapping("/courses/{id}")
    ApiResponse<CourseDto> getCourseById(@PathVariable("id") Long id);

    @PostMapping("/courses/batch")
    ApiResponse<List<CourseDto>> getBatchCourses(@RequestBody BatchCourseRequest request);

    @GetMapping("/chapters/{id}")
    ApiResponse<ChapterDto> getChapterById(@PathVariable("id") Long id);

    @GetMapping("/chapters/{id}/exists")
    ApiResponse<Boolean> checkChapterExists(@PathVariable("id") Long id);

    @GetMapping("/chapters/course/{courseId}")
    ApiResponse<List<ChapterDto>> getChaptersByCourse(@PathVariable("courseId") Long courseId);
}