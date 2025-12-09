package com.its.course.controller;

import com.its.course.dto.ApiResponse;
import com.its.course.dto.response.ChapterDto;
import com.its.course.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/chapters")
    public ApiResponse<List<ChapterDto>> getChapters(@PathVariable Long courseId) {
        return ApiResponse.success(contentService.getChaptersByCourseId(courseId), "Chapters retrieved");
    }

    @GetMapping("/chapters/{chapterId}")
    public ApiResponse<ChapterDto> getChapter(@PathVariable Long courseId, @PathVariable Long chapterId) {
        // courseId parameter is for route consistency; validation could be added
        return ApiResponse.success(contentService.getChapter(chapterId), "Chapter retrieved");
    }
}

