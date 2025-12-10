package com.its.course.service;

import com.its.course.dto.response.ChapterDto;
import com.its.course.dto.response.SectionDto;
import com.its.course.entity.Chapter;
import com.its.course.exception.AppException;
import com.its.course.exception.ErrorCode;
import com.its.course.mapper.SectionMapper;
import com.its.course.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterService implements IChapterService {

    private final ChapterRepository chapterRepository;
    private final SectionMapper sectionMapper;

    @Override
    public ChapterDto getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND, "Chapter not found"));

        return ChapterDto.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .summary(chapter.getSummary())
                .orderIndex(chapter.getOrderIndex())
                .sections(
                        chapter.getSections()
                                .stream()
                                .map(sectionMapper::sectionDto)
                                .toList()
                )
                .build();
    }

    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByOrderIndex(courseId);

        return chapters.stream()
                .map(chapter -> ChapterDto.builder()
                        .id(chapter.getId())
                        .title(chapter.getTitle())
                        .summary(chapter.getSummary())
                        .orderIndex(chapter.getOrderIndex())
                        .sections(
                                chapter.getSections()
                                        .stream()
                                        .map(sectionMapper::sectionDto)
                                        .toList()
                        )
                        .build()
                )
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return chapterRepository.existsById(id);
    }
}
