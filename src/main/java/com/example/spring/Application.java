package com.example.spring;

import com.example.spring.student.Address;
import com.example.spring.student.Gender;
import com.example.spring.student.Student;
import com.example.spring.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student alex = new Student(
                    "Alex",
                    "Lockwood",
                    "a.lockwood@gmail.com",
                    Gender.MALE,
                    new Address(
                        "USA",
                            "NY10",
                            "New York"
                    ),
                    List.of("Maths", "Physics", "Robotics"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            System.out.println(studentRepository.existsByEmail(alex.getEmail()));

            if (studentRepository.existsByEmail(alex.getEmail())) {
                throw new IllegalStateException("Email already taken");
            }

            studentRepository.insert(alex);
        };
    }
}
