package com.its.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chapter")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    // Topics linked to this chapter (for filtering/tags)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chapter_topic",
            joinColumns = @JoinColumn(name = "chapter_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();

}
