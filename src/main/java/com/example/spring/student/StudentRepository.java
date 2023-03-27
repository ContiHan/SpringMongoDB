package com.example.spring.student;

import com.example.spring.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
}
