package com.its.course.service;

import com.its.course.dto.response.ChapterDto;
import com.its.course.dto.response.CourseDto;
import com.its.course.entity.Chapter;
import com.its.course.entity.Course;
import com.its.course.entity.StudentFinishChapter;
import com.its.course.exception.AppException;
import com.its.course.exception.ErrorCode;
import com.its.course.mapper.ContentMapper;
import com.its.course.mapper.CourseMapper;
import com.its.course.repository.ChapterRepository;
import com.its.course.repository.CourseRepository;
import com.its.course.repository.StudentFinishChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final StudentFinishChapterRepository finishRepo;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ContentMapper contentMapper;
    private final CourseMapper courseMapper;

    public void markChapterFinished(Long studentId, Long chapterId) {
        if (!chapterRepository.existsById(chapterId)) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Chapter not found");
        }
        if (!finishRepo.existsByStudentIdAndChapterId(studentId, chapterId)) {
            StudentFinishChapter rec = new StudentFinishChapter();
            rec.setStudentId(studentId);
            rec.setChapterId(chapterId);
            finishRepo.save(rec);
        }
    }

    public double getCourseProgressForStudent(Long studentId, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "Course not found"));
        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByOrderIndex(courseId);
        if (chapters.isEmpty()) return 0.0;
        long finished = chapters.stream().filter(ch -> finishRepo.existsByStudentIdAndChapterId(studentId, ch.getId())).count();
        return (double) finished / (double) chapters.size();
    }

}

