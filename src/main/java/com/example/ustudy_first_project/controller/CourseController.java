package com.example.ustudy_first_project.controller;

import com.example.ustudy_first_project.entity.Course;
import com.example.ustudy_first_project.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestParam String name,
                                       @RequestParam String description,
                                       @RequestParam String image) {
        Course course = courseService.addCourse(name, description, image);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Курс удален");
    }
}
