package com.webapp.rating.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.rating.model.dto.SubjectDto;
import com.webapp.rating.model.entity.StudentScore;
import com.webapp.rating.model.vo.Assignment;
import com.webapp.rating.model.vo.Student;
import com.webapp.rating.model.vo.Subject;
import com.webapp.rating.repository.AssignmentCategoryRepo;
import com.webapp.rating.repository.StudentScoreRepo;
import com.webapp.rating.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApiService apiService;

    @MockBean
    DataSource dataSource;

    @MockBean
    CommandLineRunner commandLineRunner;

    @MockBean
    StudentScoreRepo studentScoreRepo;

    @MockBean
    AssignmentCategoryRepo assignmentCategoryRepo;

    @Test
    void testPostStudentEnrolment() throws Exception {

        String ananth = toJson(StudentScore.builder().name("Ananth").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/student/enrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ananth)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("student_enrollment"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studentScore"));
    }


    @Test
    void testStudentEnrolment() throws Exception {

        when(apiService.getAllStudentScore()).thenReturn(Arrays.asList(StudentScore.builder().name("Ananth").build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/enrollment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("student_enrollment"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studentScore"));
    }

    @Test
    void testStudentEnrolmentById() throws Exception {

        when(apiService.getStudentScoreById("1"))
                .thenReturn(Optional.of(StudentScore.builder().name("Ananth").build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/enrollment/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studentdetail"));
    }

    @Test
    void testDeleteById() throws Exception {
        Optional<StudentScore> optStudent = Optional.of(StudentScore.builder().name("Ananth").build());

        when(apiService.getStudentScoreById("1")).thenReturn(optStudent);
        doNothing().when(apiService).deleteById(optStudent);
        when(apiService.getAllStudentScore()).thenReturn(Arrays.asList(StudentScore.builder().name("Ananth").build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/enrollment/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("student_enrollment"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studentScore"));
    }

    @Test
    void testAssignmentCategory() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/assignment/category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("assignment_category"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("assignmentCategories"));
    }

    @Test
    void testDetailForm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/detail_form"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studentdetail"));
    }

    @Test
    void testScoreByStudent() throws Exception {

        List<Student> students = apiService.getAllStudents();
        Assignment as = new Assignment("as", "100", "19/06/2016");
        Subject computing_techniques = new Subject(as, "Computing Techniques");
        Student student = new Student(computing_techniques, "Chaya", "20");
        Map<String, List<Student>> stringListMap = null;

        when(apiService.getAllStudents()).thenReturn(Arrays.asList(student));
        when(apiService.groupByStudentName(any(), any())).thenReturn(stringListMap);
        when(apiService.getSubjectsByStudentName(stringListMap)).thenReturn(Arrays.asList(new SubjectDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/average/scorebystudent/Chaya"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ratebyStudent"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("average"));
    }


    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestObj = null;
        try {
            requestObj = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {

        }
        return requestObj;
    }

}