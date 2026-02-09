package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        if (updatedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student deletedStudent = studentService.deleteStudent(id);
        if (deletedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<Student>> filterStudentsByAge(@RequestParam int age) {
        Collection<Student> filteredStudents = studentService.findStudentsByAge(age);
        return ResponseEntity.ok(filteredStudents);
    }

    @GetMapping("/filter/age-between")
    public ResponseEntity<Collection<Student>> filterStudentsByAgeBetween(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        Collection<Student> filteredStudents = studentService.findStudentsByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(filteredStudents);
    }

    @GetMapping("/filter/faculty/{facultyId}")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        Collection<Student> students = studentService.findStudentsByFaculty(facultyId);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{studentId}/assign-faculty/{facultyId}")
    public ResponseEntity<Student> assignFacultyToStudent(
            @PathVariable Long studentId,
            @PathVariable Long facultyId) {
        Student student = studentService.assignFacultyToStudent(studentId, facultyId);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/filter/name")
    public ResponseEntity<Collection<Student>> findStudentsByName(@RequestParam String name) {
        Collection<Student> students = studentService.findStudentsByName(name);
        return ResponseEntity.ok(students);
    }

    // Требуемые эндпоинты с использованием @Query

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> students = studentService.getLastFiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/last-five-native")
    public ResponseEntity<List<Student>> getLastFiveStudentsNative() {
        List<Student> students = studentService.getLastFiveStudentsNative();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllStudents() {
        Long count = studentService.countAllStudents();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        Double averageAge = studentService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/faculty/{facultyId}/count")
    public ResponseEntity<Long> countStudentsByFaculty(@PathVariable Long facultyId) {
        Long count = studentService.countStudentsByFaculty(facultyId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/older-than/{age}")
    public ResponseEntity<List<Student>> getStudentsOlderThan(@PathVariable int age) {
        List<Student> students = studentService.getStudentsOlderThan(age);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/without-faculty")
    public ResponseEntity<Collection<Student>> getStudentsWithoutFaculty() {
        Collection<Student> students = studentService.getStudentsWithoutFaculty();
        return ResponseEntity.ok(students);
    }
}