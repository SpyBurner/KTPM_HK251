package com.its.course.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.course.entity.Chapter;
import com.its.course.entity.Course;
import com.its.course.entity.FileResource;
import com.its.course.entity.Section;
import com.its.course.entity.StudentEnrollCourse;
import com.its.course.entity.StudentFinishChapter;
import com.its.course.entity.Topic;
import com.its.course.repository.ChapterRepository;
import com.its.course.repository.CourseRepository;
import com.its.course.repository.FileResourceRepository;
import com.its.course.repository.SectionRepository;
import com.its.course.repository.StudentEnrollCourseRepository;
import com.its.course.repository.StudentFinishChapterRepository;
import com.its.course.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final SectionRepository sectionRepository;
    private final FileResourceRepository fileResourceRepository;
    private final StudentEnrollCourseRepository enrollRepo;
    private final StudentFinishChapterRepository finishRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() > 0) return; // don't re-seed

        // Use try-with-resources for safety
        try (InputStream is = new ClassPathResource("mock-data/course-mock.json").getInputStream()) {
            JsonNode root = objectMapper.readTree(is);

            Map<String, Topic> topicsByCode = new HashMap<>();
            for (JsonNode t : root.path("topics")) {
                String code = t.path("code").asText(null);
                String name = t.path("name").asText(null);
                if (code == null || name == null) continue;
                Topic topic = new Topic();
                topic.setCode(code);
                topic.setName(name);
                Topic savedTopic = topicRepository.save(topic);
                topicsByCode.put(savedTopic.getCode(), savedTopic);
            }

            Map<String, Course> coursesByCode = new HashMap<>();
            Map<String, Chapter> chaptersByCode = new HashMap<>(); // build while creating chapters

            for (JsonNode c : root.path("courses")) {
                String courseCode = c.path("code").asText(null);
                String title = c.path("title").asText(null);
                if (title == null) continue;

                Course course = new Course();
                course.setTitle(title);
                course.setDescription(c.path("description").asText(null));
                course.setChangeUserId(c.path("changeUserId").isMissingNode() ? null : c.path("changeUserId").asLong());
                course.setInstructorId(c.path("instructorId").isMissingNode() ? null : c.path("instructorId").asLong());
                course.setActive(true);
                course.setCreatedAt(LocalDateTime.now());

                // thumbnail
                JsonNode thumb = c.path("thumbnailFile");
                if (thumb != null && !thumb.isMissingNode() && !thumb.isNull()) {
                    FileResource fr = new FileResource();
                    fr.setFileName(thumb.path("fileName").asText(null));
                    fr.setFilePath(thumb.path("filePath").asText(null));
                    fr.setContentType(thumb.path("contentType").asText(null));
                    fr.setSizeBytes(thumb.path("sizeBytes").asLong(0L));
                    FileResource savedFr = fileResourceRepository.save(fr);
                    course.setThumbnailFileId(savedFr.getId());
                }

                Course savedCourse = courseRepository.save(course);
                if (courseCode != null && !courseCode.isBlank()) coursesByCode.put(courseCode, savedCourse);

                // chapters
                for (JsonNode ch : c.path("chapters")) {
                    String chapterCode = ch.path("code").asText(null);
                    String chTitle = ch.path("title").asText(null);
                    if (chTitle == null) continue;

                    Chapter chapter = new Chapter();
                    chapter.setTitle(chTitle);
                    chapter.setSummary(ch.path("summary").asText(null));
                    chapter.setOrderIndex(ch.path("orderIndex").asInt(0));
                    chapter.setCourse(savedCourse);

                    Chapter savedChapter = chapterRepository.save(chapter);

                    // link topics
                    for (JsonNode codeNode : ch.path("topicCodes")) {
                        String tcode = codeNode.asText(null);
                        if (tcode == null) continue;
                        Topic topic = topicsByCode.get(tcode);
                        if (topic != null) {
                            savedChapter.getTopics().add(topic);
                        }
                    }
                    savedChapter = chapterRepository.save(savedChapter);

                    // sections
                    for (JsonNode s : ch.path("sections")) {
                        String sName = s.path("name").asText(null);
                        if (sName == null) continue;
                        Section section = new Section();
                        section.setName(sName);
                        section.setText(s.path("text").isNull() ? null : s.path("text").asText(null));
                        section.setOrderIndex(s.path("orderIndex").asInt(0));
                        section.setChapter(savedChapter);
                        sectionRepository.save(section);
                    }

                    if (chapterCode != null && !chapterCode.isBlank()) {
                        chaptersByCode.put(chapterCode, savedChapter);
                    }
                }
            }

            // enrollments
            for (JsonNode en : root.path("enrollments")) {
                long studentId = en.path("studentId").asLong();
                String courseCode = en.path("courseCode").asText(null);
                if (courseCode == null) continue;
                Course course = coursesByCode.get(courseCode);
                if (course == null) continue;
                StudentEnrollCourse rec = new StudentEnrollCourse();
                rec.setStudentId(studentId);
                rec.setCourseId(course.getId());
                rec.setIsStarred(en.path("isStarred").asBoolean(false));
                rec.setIsSaved(en.path("isSaved").asBoolean(false));
                rec.setEnrolledAt(LocalDateTime.now());
                enrollRepo.save(rec);
            }

            // finished chapters
            for (JsonNode f : root.path("finishedChapters")) {
                long studentId = f.path("studentId").asLong();
                String chapterCode = f.path("chapterCode").asText(null);
                if (chapterCode == null) continue;
                Chapter ch = chaptersByCode.get(chapterCode);
                if (ch != null) {
                    StudentFinishChapter rec = new StudentFinishChapter();
                    rec.setStudentId(studentId);
                    rec.setChapterId(ch.getId());
                    rec.setFinishedAt(LocalDateTime.now());
                    finishRepo.save(rec);
                }
            }

            System.out.println("CourseService: loaded mock data from classpath/mock-data/course-mock.json");
        }
    }
}
