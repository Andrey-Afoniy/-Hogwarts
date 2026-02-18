package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findByFacultyId(Long facultyId);

    Student findByEmail(String email);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();

    @Query("SELECT s FROM Student s WHERE s.age > :age")
    List<Student> findStudentsOlderThan(@Param("age") int age);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.faculty.id = :facultyId")
    Long countStudentsByFacultyId(@Param("facultyId") Long facultyId);
}