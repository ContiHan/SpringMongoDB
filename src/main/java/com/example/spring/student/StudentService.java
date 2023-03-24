package com.example.spring.student;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        studentRepository.findByEmail(student.getEmail())
                .ifPresentOrElse(s -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Email '" + s.getEmail() + "' already taken");
                }, () -> {
                    student.setCreated(LocalDateTime.now());
                    studentRepository.insert(student);
                });
    }

    public void deleteStudent(String studentId) {
        studentRepository.findById(studentId)
                .ifPresentOrElse(student -> {
                    studentRepository.deleteById(studentId);
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Student does not exist");
                });
    }
}
