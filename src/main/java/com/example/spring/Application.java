package com.example.spring;

import com.example.spring.student.Address;
import com.example.spring.student.Gender;
import com.example.spring.student.Student;
import com.example.spring.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, MongoTemplate mongoTemplate) {
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

            studentRepository.findStudentByEmail(alex.getEmail())
                    .ifPresentOrElse(student -> {
                        System.out.println(student + " already exists");
                    }, () -> {
                        studentRepository.insert(alex);
                        System.out.println(alex + " was created");
                    });

//            usingMongoRepositoryAndBoolean(studentRepository, alex);
//            usingMongoTemplateAndQuery(studentRepository, mongoTemplate, alex);
        };
    }

    private void usingMongoRepositoryAndBoolean(StudentRepository studentRepository, Student alex) {
        if (studentRepository.existsByEmail(alex.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already taken");
        }

        studentRepository.insert(alex);
    }

    private void usingMongoTemplateAndQuery(StudentRepository studentRepository, MongoTemplate mongoTemplate, Student alex) {
        Query query = new Query()
                .addCriteria(Criteria.where("email").is(alex.getEmail()));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "More than one student with email: " + alex.getEmail());
        }

        if (students.isEmpty()) {
            studentRepository.insert(alex);
            System.out.println("Added new student: " + alex);
        } else {
            System.out.println(alex + " already exists");
        }
    }
}
