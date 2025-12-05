package com.its.course.service;

import com.its.course.dto.response.ChapterDto;
import com.its.course.entity.Chapter;
import com.its.course.exception.AppException;
import com.its.course.exception.ErrorCode;
import com.its.course.mapper.ContentMapper;
import com.its.course.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ChapterRepository chapterRepository;
    private final ContentMapper contentMapper;

    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByOrderIndex(courseId);
        return chapters.stream().map(contentMapper::toChapterDto).collect(Collectors.toList());
    }

    public ChapterDto getChapter(Long id) {
        Chapter ch = chapterRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Chapter not found"));
        return contentMapper.toChapterDto(ch);
    }
}

