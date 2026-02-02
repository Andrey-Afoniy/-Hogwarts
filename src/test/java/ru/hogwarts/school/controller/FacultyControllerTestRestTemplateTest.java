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
class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";

        testFaculty = new Faculty();
        testFaculty.setName("Гриффиндор");
        testFaculty.setColor("красный");
    }

    @Test
    void testCreateFaculty() {
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl, testFaculty, Faculty.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Гриффиндор", response.getBody().getName());
        assertEquals("красный", response.getBody().getColor());
    }

    @Test
    void testGetFacultyById() {
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                baseUrl, testFaculty, Faculty.class);
        Faculty createdFaculty = createResponse.getBody();
        Long facultyId = createdFaculty.getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + facultyId, Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Гриффиндор", response.getBody().getName());
    }

    @Test
    void testGetFacultyByIdNotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/999", Faculty.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateFaculty() {
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                baseUrl, testFaculty, Faculty.class);
        Faculty createdFaculty = createResponse.getBody();
        Long facultyId = createdFaculty.getId();

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setName("Слизерин");
        updatedFaculty.setColor("зеленый");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(updatedFaculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/" + facultyId,
                HttpMethod.PUT,
                requestEntity,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(facultyId, response.getBody().getId());
        assertEquals("Слизерин", response.getBody().getName());
        assertEquals("зеленый", response.getBody().getColor());
    }

    @Test
    void testDeleteFaculty() {
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                baseUrl, testFaculty, Faculty.class);
        Faculty createdFaculty = createResponse.getBody();
        Long facultyId = createdFaculty.getId();

        ResponseEntity<Faculty> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + facultyId,
                HttpMethod.DELETE,
                null,
                Faculty.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + facultyId, Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testGetAllFaculties() {
        restTemplate.postForEntity(baseUrl, testFaculty, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Слизерин");
        faculty2.setColor("зеленый");
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
    }

    @Test
    void testGetFacultiesByColor() {
        restTemplate.postForEntity(baseUrl, testFaculty, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Хаффлпафф");
        faculty2.setColor("желтый");
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        Faculty faculty3 = new Faculty();
        faculty3.setName("Слизерин");
        faculty3.setColor("зеленый");
        restTemplate.postForEntity(baseUrl, faculty3, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/filter/зеленый",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("зеленый", response.getBody().get(0).getColor());
    }

    @Test
    void testSearchFaculties() {
        restTemplate.postForEntity(baseUrl, testFaculty, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Слизерин");
        faculty2.setColor("зеленый");
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/search?name=Гриффиндор",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Гриффиндор", response.getBody().get(0).getName());
    }

    @Test
    void testSearchFacultiesByTerm() {
        restTemplate.postForEntity(baseUrl, testFaculty, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Слизерин");
        faculty2.setColor("зеленый");
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/search-by-term?searchTerm=зеленый",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("зеленый", response.getBody().get(0).getColor());
    }

    @Test
    void testGetFacultyStudents() {
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(
                baseUrl, testFaculty, Faculty.class);
        Faculty createdFaculty = facultyResponse.getBody();

        String studentUrl = "http://localhost:" + port + "/student";
        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(17);
        student.setFaculty(createdFaculty);
        restTemplate.postForEntity(studentUrl, student, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/" + createdFaculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}