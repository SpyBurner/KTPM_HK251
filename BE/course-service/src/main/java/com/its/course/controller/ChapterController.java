package com.its.course.controller;

import com.its.course.dto.ApiResponse;
import com.its.course.dto.response.ChapterDto;
import com.its.course.service.IChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final IChapterService chapterService;

    @GetMapping("/{id}")
    public ApiResponse<ChapterDto> getChapterById(@PathVariable Long id) {
        return ApiResponse.success(
                chapterService.getChapterById(id),
                "Chapter retrieved successfully"
        );
    }

    @GetMapping("/{id}/exists")
    public ApiResponse<Boolean> checkChapterExists(@PathVariable Long id) {
        return ApiResponse.success(
                chapterService.existsById(id),
                "Chapter existence checked"
        );
    }

    @GetMapping("/course/{courseId}")
    public ApiResponse<List<ChapterDto>> getChaptersByCourse(@PathVariable Long courseId) {
        return ApiResponse.success(
                chapterService.getChaptersByCourseId(courseId),
                "Chapters retrieved successfully"
        );
    }
}
