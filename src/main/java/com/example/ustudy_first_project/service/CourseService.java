package com.example.ustudy_first_project.service;

import com.example.ustudy_first_project.entity.Course;
import com.example.ustudy_first_project.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course addCourse(String name, String description, String image) {
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setImage(image);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
