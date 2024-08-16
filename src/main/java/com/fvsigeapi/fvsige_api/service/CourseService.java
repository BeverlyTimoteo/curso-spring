package com.fvsigeapi.fvsige_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fvsigeapi.fvsige_api.dto.CourseDTO;
import com.fvsigeapi.fvsige_api.dto.CoursePageDTO;
import com.fvsigeapi.fvsige_api.dto.mapper.CourseMapper;
import com.fvsigeapi.fvsige_api.exception.RecordNotFoundException;
import com.fvsigeapi.fvsige_api.model.Course;
import com.fvsigeapi.fvsige_api.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper cursoMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper cursoMapper) {
        this.courseRepository = courseRepository;
        this.cursoMapper = cursoMapper;
    }

    /*public List<CourseDTO> list() {
        return courseRepository.findAll()
                .stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }*/

    public CoursePageDTO list(@PositiveOrZero int pageNumber, @Positive @Max(100) int pageSize) {
        Page<Course> page = courseRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<CourseDTO> courses = page.get().map(cursoMapper::toDTO).collect(Collectors.toList());
        return new CoursePageDTO(courses, page.getTotalElements(), page.getTotalPages());
    }

    public CourseDTO findById(@NotNull @Positive Long id) {
        return courseRepository.findById(id).map(cursoMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public CourseDTO create(@Valid @NotNull CourseDTO curso) {
        return cursoMapper.toDTO(courseRepository.save(cursoMapper.toEntity(curso)));
    }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO cursoDTO) {
        return courseRepository.findById(id)
                .map(recordFound -> {
                    Course course = cursoMapper.toEntity(cursoDTO);
                    recordFound.setName(cursoDTO.name());
                    recordFound.setCategory(cursoMapper.convertCategoryValue(cursoDTO.category()));
                    
                    recordFound.getLessons().clear();

                    course.getLessons().forEach(recordFound.getLessons()::add);
                    
                    return cursoMapper.toDTO(courseRepository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        courseRepository.delete(courseRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id)));
    }

}
