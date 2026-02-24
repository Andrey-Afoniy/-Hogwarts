package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        logger.info("StudentService initialized");
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student with name: {}", student.getName());
        logger.debug("Student details: {}", student);

        // Проверка на уникальность email
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            Student existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent != null) {
                logger.error("Student with email {} already exists", student.getEmail());
                throw new IllegalArgumentException("Студент с email " + student.getEmail() + " уже существует");
            }
        }

        try {
            Student savedStudent = studentRepository.save(student);
            logger.info("Student created successfully with id: {}", savedStudent.getId());
            return savedStudent;
        } catch (Exception e) {
            logger.error("Error creating student: {}", e.getMessage());
            throw e;
        }
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student with id: {}", id);

        return studentRepository.findById(id)
                .map(student -> {
                    logger.debug("Found student: {}", student);
                    return student;
                })
                .orElseGet(() -> {
                    logger.warn("Student with id {} not found", id);
                    return null;
                });
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method for update student with id: {}", id);

        if (!studentRepository.existsById(id)) {
            logger.error("Cannot update: student with id {} not found", id);
            return null;
        }

        try {
            student.setId(id);
            Student updatedStudent = studentRepository.save(student);
            logger.info("Student with id {} updated successfully", id);
            logger.debug("Updated student: {}", updatedStudent);
            return updatedStudent;
        } catch (Exception e) {
            logger.error("Error updating student with id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public Student deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);
        logger.warn("Delete operation for student with id: {}", id);

        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    logger.info("Student with id {} deleted successfully", id);
                    return student;
                })
                .orElseGet(() -> {
                    logger.error("Cannot delete: student with id {} not found", id);
                    return null;
                });
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");

        Collection<Student> students = studentRepository.findAll();
        logger.debug("Found {} students", students.size());
        return students;
    }

    public Collection<Student> findStudentsByAge(int age) {
        logger.info("Was invoked method for find students by age: {}", age);

        Collection<Student> students = studentRepository.findByAge(age);
        logger.debug("Found {} students with age {}", students.size(), age);
        return students;
    }

    public Collection<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find students by age between {} and {}", minAge, maxAge);

        Collection<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        logger.debug("Found {} students in age range {}-{}", students.size(), minAge, maxAge);
        return students;
    }

    public Collection<Student> findStudentsByFaculty(Long facultyId) {
        logger.info("Was invoked method for find students by faculty id: {}", facultyId);

        Collection<Student> students = studentRepository.findByFacultyId(facultyId);
        logger.debug("Found {} students for faculty {}", students.size(), facultyId);
        return students;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");

        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Found {} students in last five", students.size());
        return students;
    }

    public Long countAllStudents() {
        logger.info("Was invoked method for count all students");

        Long count = studentRepository.countAllStudents();
        logger.info("Total students count: {}", count);
        return count != null ? count : 0L;
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");

        Double averageAge = studentRepository.findAverageAge();
        logger.info("Average student age: {}", averageAge);
        return averageAge != null ? averageAge : 0.0;
    }

    public Long countStudentsByFaculty(Long facultyId) {
        logger.info("Was invoked method for count students by faculty id: {}", facultyId);

        Long count = studentRepository.countStudentsByFacultyId(facultyId);
        logger.debug("Faculty {} has {} students", facultyId, count);
        return count != null ? count : 0L;
    }

    public List<Student> getStudentsOlderThan(int age) {
        logger.info("Was invoked method for get students older than {}", age);

        List<Student> students = studentRepository.findStudentsOlderThan(age);
        logger.debug("Found {} students older than {}", students.size(), age);
        return students;
    }
}