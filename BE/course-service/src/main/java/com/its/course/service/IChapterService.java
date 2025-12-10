package com.its.course.service;

import com.its.course.dto.response.ChapterDto;

import java.util.List;

public interface IChapterService {
    ChapterDto getChapterById(Long id);
    List<ChapterDto> getChaptersByCourseId(Long courseId);
    boolean existsById(Long id);
}