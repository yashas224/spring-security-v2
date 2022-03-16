package com.example.demo1.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {
    private static final List<Student> STUDENTS = List.of(new Student(1, "yashas"),
            new Student(2, "malathi"), new Student(3, "Krishna"));


    @GetMapping
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @PostMapping
    public void registerStudent(@RequestBody Student student) {
        System.out.println("student added---" + student);
    }


    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable(name = "id") int id) {
        System.out.println("deleted Student with id" + id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable(name = "id") int id, @RequestBody Student student) {
        System.out.println(String.format("updated Student with %s and contents %s", id, student));

    }

}
