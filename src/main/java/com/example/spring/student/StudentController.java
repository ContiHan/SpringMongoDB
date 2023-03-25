package com.example.spring.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @PutMapping(path = "{studentId}")
    public void editEmailOfStudent(
            @PathVariable(value = "studentId") String studentId,
            @RequestParam String email
    ) {
        studentService.updateEmailOfStudent(studentId, email);
    }

    @PutMapping()
    public void editStudent(@RequestBody Student student) {
        studentService.updateStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void removeStudent(@PathVariable("studentId") String studentId) {
        studentService.deleteStudent(studentId);
    }
}
