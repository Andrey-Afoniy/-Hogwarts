package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/student";

        testStudent = new Student();
        testStudent.setName("Гарри Поттер");
        testStudent.setAge(17);
    }

    @Test
    void testCreateStudent() {
        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl, testStudent, Student.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Гарри Поттер", response.getBody().getName());
        assertEquals(17, response.getBody().getAge());
    }

    @Test
    void testGetStudentById() {
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                baseUrl, testStudent, Student.class);
        Student createdStudent = createResponse.getBody();
        Long studentId = createdStudent.getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/" + studentId, Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Гарри Поттер", response.getBody().getName());
    }

    @Test
    void testGetStudentByIdNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/999", Student.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateStudent() {
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                baseUrl, testStudent, Student.class);
        Student createdStudent = createResponse.getBody();
        Long studentId = createdStudent.getId();

        Student updatedStudent = new Student();
        updatedStudent.setName("Гарри Джеймс Поттер");
        updatedStudent.setAge(18);

        HttpEntity<Student> requestEntity = new HttpEntity<>(updatedStudent);

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl + "/" + studentId,
                HttpMethod.PUT,
                requestEntity,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Гарри Джеймс Поттер", response.getBody().getName());
        assertEquals(18, response.getBody().getAge());
    }

    @Test
    void testDeleteStudent() {
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                baseUrl, testStudent, Student.class);
        Student createdStudent = createResponse.getBody();
        Long studentId = createdStudent.getId();

        ResponseEntity<Student> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + studentId,
                HttpMethod.DELETE,
                null,
                Student.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + studentId, Student.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testGetAllStudents() {
        restTemplate.postForEntity(baseUrl, testStudent, Student.class);

        Student student2 = new Student();
        student2.setName("Гермиона Грейнджер");
        student2.setAge(17);
        restTemplate.postForEntity(baseUrl, student2, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
    }

    @Test
    void testGetStudentsByAge() {
        restTemplate.postForEntity(baseUrl, testStudent, Student.class);

        Student student2 = new Student();
        student2.setName("Рон Уизли");
        student2.setAge(17);
        restTemplate.postForEntity(baseUrl, student2, Student.class);

        Student student3 = new Student();
        student3.setName("Драко Малфой");
        student3.setAge(18);
        restTemplate.postForEntity(baseUrl, student3, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/filter/17",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
        response.getBody().forEach(student ->
                assertEquals(17, student.getAge()));
    }

    @Test
    void testGetStudentsByAgeBetween() {
        restTemplate.postForEntity(baseUrl, testStudent, Student.class);

        Student student2 = new Student();
        student2.setName("Невилл Долгопупс");
        student2.setAge(16);
        restTemplate.postForEntity(baseUrl, student2, Student.class);

        Student student3 = new Student();
        student3.setName("Полумна Лавгуд");
        student3.setAge(15);
        restTemplate.postForEntity(baseUrl, student3, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/age-between?min=16&max=18",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        response.getBody().forEach(student -> {
            assertTrue(student.getAge() >= 16);
            assertTrue(student.getAge() <= 18);
        });
    }

    @Test
    void testGetStudentFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class);
        Faculty createdFaculty = facultyResponse.getBody();

        testStudent.setFaculty(createdFaculty);
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity(
                baseUrl, testStudent, Student.class);
        Student createdStudent = studentResponse.getBody();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + createdStudent.getId() + "/faculty",
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Гриффиндор", response.getBody().getName());
    }
}