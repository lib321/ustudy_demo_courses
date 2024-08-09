package com.example.ustudy_first_project.service;

import com.example.ustudy_first_project.entity.Course;
import com.example.ustudy_first_project.repository.CourseRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    public ByteArrayInputStream generateCoursePdf(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Course name: " + course.getName()));
        document.add(new Paragraph("Description: " + course.getDescription()));
        document.add(new Paragraph("Image: " + course.getImage()));

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
