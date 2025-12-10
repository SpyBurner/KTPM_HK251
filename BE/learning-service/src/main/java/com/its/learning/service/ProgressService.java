package com.its.learning.service;

import com.its.learning.dto.response.ChapterDto;
import com.its.learning.dto.response.CourseDto;
import com.its.learning.entity.StudentFinishChapter;
import com.its.learning.exception.AppException;
import com.its.learning.exception.ErrorCode;
import com.its.learning.repository.StudentFinishChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final StudentFinishChapterRepository finishRepo;
    private final ICourseService courseClient;

    public void markChapterFinished(Long studentId, Long chapterId) {
        Boolean exists = courseClient.checkChapterExists(chapterId).getResult();

        if (!exists) {
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
        CourseDto course = courseClient.getCourseById(courseId).getResult();

        if (course == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Course not found");
        }

        List<ChapterDto> chapters = courseClient.getChaptersByCourse(courseId).getResult();

        if (chapters.isEmpty()) {
            return 0.0;
        }

        long finished = chapters.stream()
                .filter(ch -> finishRepo.existsByStudentIdAndChapterId(studentId, ch.getId()))
                .count();

        return (double) finished / (double) chapters.size();
    }
}
