package com.fvsigeapi.fvsige_api.dto;

import java.util.List;

public record CoursePageDTO(
        List<CourseDTO> cursos,
        long totalElements,
        int totalPages) {
}
