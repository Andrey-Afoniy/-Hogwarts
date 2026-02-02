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
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void testGetFacultyByIdNotFound() throws Exception {
        when(facultyService.getFacultyById(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(1L);
        updatedFaculty.setName("Слизерин");
        updatedFaculty.setColor("зеленый");

        when(facultyService.updateFaculty(eq(1L), any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("зеленый"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        when(facultyService.deleteFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Гриффиндор");
        faculty1.setColor("красный");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Слизерин");
        faculty2.setColor("зеленый");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[1].name").value("Слизерин"));
    }

    @Test
    void testGetFacultiesByColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Гриффиндор");
        faculty1.setColor("красный");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Хаффлпафф");
        faculty2.setColor("желтый");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);
        when(facultyService.getFacultiesByColor("красный")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/filter/красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].color").value("красный"))
                .andExpect(jsonPath("$[1].color").value("красный"));
    }

    @Test
    void testSearchFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        List<Faculty> faculties = Arrays.asList(faculty);
        when(facultyService.getFacultiesByNameOrColor("Гриффиндор", null))
                .thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/search")
                        .param("name", "Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }

    @Test
    void testSearchFacultiesByTerm() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Гриффиндор");
        faculty1.setColor("красный");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Слизерин");
        faculty2.setColor("зеленый");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);
        when(facultyService.searchFaculties("зеленый")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/search-by-term")
                        .param("searchTerm", "зеленый"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetFacultyStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(17);

        List<Student> students = Arrays.asList(student1, student2);
        when(facultyService.getFacultyStudents(1L)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));
    }

    @Test
    void testGetFacultyStudentsNotFound() throws Exception {
        when(facultyService.getFacultyStudents(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/999/students"))
                .andExpect(status().isNotFound());
    }
}