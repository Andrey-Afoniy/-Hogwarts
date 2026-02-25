package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.ok(createdStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student deletedStudent = studentService.deleteStudent(id);
        if (deletedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
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

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> students = studentService.getLastFiveStudents();
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

    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getStudentNamesStartingWithA() {
        List<String> names = studentService.getStudentNamesStartingWithA();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/average-age-all")
    public ResponseEntity<Double> getAverageAgeOfAllStudents() {
        Double averageAge = studentService.getAverageAgeOfAllStudents();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<String> printStudentsParallel() {
        logger.info("Вызов метода параллельного вывода студентов");

        List<String> studentNames = studentService.getFirstSixStudentNames();

        if (studentNames.size() < 6) {
            return ResponseEntity.badRequest().body("Недостаточно студентов в базе данных (нужно минимум 6)");
        }

        System.out.println("=== Параллельный вывод студентов (без синхронизации) ===");
        System.out.println("Основной поток: " + Thread.currentThread().getName());

        System.out.println("Студент 1 (основной поток): " + studentNames.get(0));
        System.out.println("Студент 2 (основной поток): " + studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            System.out.println("Поток 1: " + Thread.currentThread().getName());
            System.out.println("Студент 3 (поток 1): " + studentNames.get(2));
            System.out.println("Студент 4 (поток 1): " + studentNames.get(3));
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Поток 2: " + Thread.currentThread().getName());
            System.out.println("Студент 5 (поток 2): " + studentNames.get(4));
            System.out.println("Студент 6 (поток 2): " + studentNames.get(5));
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании завершения потоков: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Завершение параллельного вывода ===\n");

        return ResponseEntity.ok("Параллельный вывод студентов выполнен. Проверьте консоль.");
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<String> printStudentsSynchronized() {
        logger.info("Вызов метода синхронизированного вывода студентов");

        List<String> studentNames = studentService.getFirstSixStudentNames();

        if (studentNames.size() < 6) {
            return ResponseEntity.badRequest().body("Недостаточно студентов в базе данных (нужно минимум 6)");
        }

        System.out.println("=== Синхронизированный вывод студентов ===");
        System.out.println("Основной поток: " + Thread.currentThread().getName());

        printNameSynchronized("Студент 1 (основной поток)", studentNames.get(0));
        printNameSynchronized("Студент 2 (основной поток)", studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            printNameSynchronized("Студент 3 (поток 1)", studentNames.get(2));
            printNameSynchronized("Студент 4 (поток 1)", studentNames.get(3));
        });

        Thread thread2 = new Thread(() -> {
            printNameSynchronized("Студент 5 (поток 2)", studentNames.get(4));
            printNameSynchronized("Студент 6 (поток 2)", studentNames.get(5));
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании завершения потоков: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Завершение синхронизированного вывода ===\n");

        return ResponseEntity.ok("Синхронизированный вывод студентов выполнен. Проверьте консоль.");
    }

    private synchronized void printNameSynchronized(String prefix, String name) {
        System.out.println(prefix + " (поток: " + Thread.currentThread().getName() + "): " + name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}