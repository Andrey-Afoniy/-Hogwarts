package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            Student existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent != null) {
                throw new IllegalArgumentException("Студент с email " + student.getEmail() + " уже существует");
            }
        }
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        if (studentRepository.existsById(id)) {
            student.setId(id);
            return studentRepository.save(student);
        }
        return null;
    }

    public Student deleteStudent(Long id) {
        Student student = findStudent(id);
        if (student != null) {
            studentRepository.deleteById(id);
        }
        return student;
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

    public List<Student> getLastFiveStudents() {
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
}