package com.fvsigeapi.fvsige_api.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fvsigeapi.fvsige_api.dto.CourseDTO;
import com.fvsigeapi.fvsige_api.dto.LessonDTO;
import com.fvsigeapi.fvsige_api.enums.Category;
import com.fvsigeapi.fvsige_api.model.Course;
import com.fvsigeapi.fvsige_api.model.Lesson;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        List<LessonDTO> lista = course.getLessons().stream()
                .map(item -> new LessonDTO(item.getId(), item.getNome(), item.getYoutubeUrl()))
                .collect(Collectors.toList());
        return new CourseDTO(course.getId(), course.getName(), course.getCategory().getValue(), lista);
    }

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course curso = new Course();

        if (courseDTO.id() != null) {
            curso.setId(courseDTO.id());
        }

        curso.setName(courseDTO.name());
        curso.setCategory(convertCategoryValue(courseDTO.category()));

        curso.setLessons(courseDTO.lessons().stream().map(lessonDTO -> {
            var lesson = new Lesson();
            lesson.setId(lessonDTO.id());
            lesson.setNome(lessonDTO.nome());
            lesson.setYoutubeUrl(lessonDTO.youtubeUrl());
            lesson.setCurso(curso);
            return lesson;
        }).collect(Collectors.toList()));

        return curso;
    }

    public Category convertCategoryValue(String value) {
        if (value == null) {
            return null;
        }

        return switch (value) {
            case "Front-end" -> Category.FRONT_END;
            case "Back-end" -> Category.BACK_END;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };

    }

}
