package com.example.demo1.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {


    private static final List<Student> STUDENTS = List.of(new Student(1, "yashas"),
            new Student(2, "malathi"), new Student(3, "Krishna"));

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable(name = "id") int id) {
        return STUDENTS.stream().filter(s -> s.getStudentId() == id).findFirst().
                orElseThrow(() -> new IllegalStateException("student with id" + id + "does not exists"));
    }
}
