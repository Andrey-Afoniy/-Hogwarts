package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            return null;
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
            return student.get();
        }
        return null;
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> findStudentsByFaculty(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }

    public Student assignFacultyToStudent(Long studentId, Long facultyId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Faculty> facultyOpt = facultyRepository.findById(facultyId);

        if (studentOpt.isPresent() && facultyOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setFaculty(facultyOpt.get());
            return studentRepository.save(student);
        }
        return null;
    }

    public Student removeFacultyFromStudent(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setFaculty(null);
            return studentRepository.save(student);
        }
        return null;
    }

    public Collection<Student> findStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    // Методы с использованием @Query

    public List<Student> getLastFiveStudents() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }

    public List<Student> getLastFiveStudentsNative() {
        return studentRepository.findLastFiveStudents();
    }

    public Long countAllStudents() {
        Long count = studentRepository.countAllStudents();
        return count != null ? count : 0L;
    }

    public Double getAverageAge() {
        Double averageAge = studentRepository.findAverageAge();
        return averageAge != null ? averageAge : 0.0;
    }

    public Long countStudentsByFaculty(Long facultyId) {
        Long count = studentRepository.countStudentsByFacultyId(facultyId);
        return count != null ? count : 0L;
    }

    public List<Student> getStudentsOlderThan(int age) {
        return studentRepository.findStudentsOlderThan(age);
    }

    public Collection<Student> getStudentsWithoutFaculty() {
        return studentRepository.findByFacultyIsNull();
    }
}