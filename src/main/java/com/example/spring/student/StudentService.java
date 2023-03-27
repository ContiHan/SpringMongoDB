package com.example.spring.student;

import com.example.spring.common.Helper;
import com.example.spring.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    public void updateEmailOfStudent(String studentId, String email) {
        Student studentFromDb = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Student does not exist");
                });

        //TODO create another way how to check if email is taken because second if never run
        if (canBeUpdated(studentFromDb.getEmail(), email)) {
            if (studentRepository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email '" + email + "' already taken");
            }
            studentFromDb.setEmail(email);
        }

        studentRepository.save(studentFromDb);
    }

    @Transactional
    public void updateStudent(Student student) {
        Student studentFromDb = studentRepository.findById(student.getId())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Student does not exist");
                });

        if (canBeUpdated(studentFromDb.getFirstName(), student.getFirstName())) {
            studentFromDb.setFirstName(student.getFirstName());
        }

        if (canBeUpdated(studentFromDb.getLastName(), student.getLastName())) {
            studentFromDb.setLastName(student.getLastName());
        }

        if (canBeUpdated(studentFromDb.getEmail(), student.getEmail())) {
            studentFromDb.setEmail(student.getEmail());
        }

        if (canBeUpdated(studentFromDb.getGender(), student.getGender())) {
            studentFromDb.setGender(student.getGender());
        }

        if (canBeUpdated(studentFromDb.getAddress(), student.getAddress())) {
            studentFromDb.setAddress(student.getAddress());
        }

        if (canBeUpdated(studentFromDb.getFavouriteSubjects(), student.getFavouriteSubjects())) {
            studentFromDb.setFavouriteSubjects(student.getFavouriteSubjects());
        }

        if (canBeUpdated(studentFromDb.getTotalSpentInBooks(), student.getTotalSpentInBooks())) {
            studentFromDb.setTotalSpentInBooks(student.getTotalSpentInBooks());
        }

        studentRepository.save(studentFromDb);
    }

    private boolean canBeUpdated(String originalValue, String newValue) {
        return newValue != null &&
                newValue.length() > 0 &&
                !Objects.equals(newValue, originalValue);
    }

    private boolean canBeUpdated(Gender originalValue, Gender newValue) {
        return newValue != null &&
                Helper.isValueInEnum(newValue.toString(), Gender.class) &&
                !Objects.equals(newValue, originalValue);
    }

    private boolean canBeUpdated(Address originalValue, Address newValue) {
        return newValue != null &&
                canBeUpdated(originalValue.getCity(), newValue.getCity()) &&
                canBeUpdated(originalValue.getCountry(), newValue.getCountry()) &&
                canBeUpdated(originalValue.getPostCode(), newValue.getPostCode());
    }

    private boolean canBeUpdated(List<String> originalValue, List<String> newValue) {
        return newValue != null &&
                !newValue.isEmpty() &&
                !Objects.equals(newValue, originalValue);
    }

    private boolean canBeUpdated(BigDecimal originalValue, BigDecimal newValue) {
        return newValue != null &&
                !Objects.equals(newValue, originalValue);
    }
}
