package com.example.ustudy_first_project.repository;

import com.example.ustudy_first_project.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
