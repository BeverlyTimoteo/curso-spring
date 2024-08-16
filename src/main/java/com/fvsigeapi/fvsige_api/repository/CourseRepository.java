package com.fvsigeapi.fvsige_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fvsigeapi.fvsige_api.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
