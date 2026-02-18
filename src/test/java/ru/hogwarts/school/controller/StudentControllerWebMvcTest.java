package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void testGetStudentById() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void testGetStudentByIdNotFound() throws Exception {
        when(studentService.getStudentById(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Гарри Джеймс Поттер");
        updatedStudent.setAge(18);

        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Джеймс Поттер"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.deleteStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"));
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(17);

        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));
    }

    @Test
    void testGetStudentsByAge() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(17);

        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getStudentsByAge(17)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/filter/17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(17))
                .andExpect(jsonPath("$[1].age").value(17));
    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(18);

        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getStudentsByAgeBetween(16, 19)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/age-between")
                        .param("min", "16")
                        .param("max", "19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetStudentFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        when(studentService.getStudentFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void testGetStudentFacultyNotFound() throws Exception {
        when(studentService.getStudentFaculty(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/999/faculty"))
                .andExpect(status().isNotFound());
    }
}