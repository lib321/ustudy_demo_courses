package com.example.ustudy_first_project.controller;

import com.example.ustudy_first_project.entity.Course;
import com.example.ustudy_first_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/favorites/add/{courseId}")
    public ResponseEntity<String> addToFavorites(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.addToFavorites(userId, courseId);
        return ResponseEntity.ok("Курс добавлен в избранное");
    }

    @DeleteMapping("/{userId}/favorites/delete/{courseId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.removeFromFavorites(userId, courseId);
        return ResponseEntity.ok("Курс удален из избранного");
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<Set<Course>> getFavoriteCourses(@PathVariable Long userId) {
        Set<Course> favoriteCourses = userService.getFavoriteCourses(userId);
        return ResponseEntity.ok(favoriteCourses);
    }
}
