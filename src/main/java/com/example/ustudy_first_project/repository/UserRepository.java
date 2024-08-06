package com.example.ustudy_first_project.repository;

import com.example.ustudy_first_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

}
