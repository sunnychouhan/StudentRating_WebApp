package com.webapp.rating.controller;

import com.webapp.rating.model.dto.StudentDto;
import com.webapp.rating.model.dto.SubjectDto;
import com.webapp.rating.model.entity.AssignmentCategory;
import com.webapp.rating.model.entity.StudentScore;
import com.webapp.rating.model.vo.Student;
import com.webapp.rating.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ApplicationController {
    @Autowired
    ApiService apiService;

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/student/enrollment")
    public String studentEnrolment(Model model) {
        List<StudentScore> studentScore = apiService.getAllStudentScore();
        model.addAttribute("studentScore", studentScore);
        return "student_enrollment";
    }

    @PostMapping("/student/enrollment")
    public String updateEnrollment(Model model, @ModelAttribute StudentScore studentScore) {
        apiService.saveStudent(studentScore);
        model.addAttribute("studentScore", studentScore);
        return "student_enrollment";
    }

    @GetMapping("/student/enrollment/{id}")
    public String studentEnrolment(Model model, @PathVariable("id") String studentId) {
        Optional<StudentScore> studentScore = apiService.getStudentScoreById(studentId);
        if (studentScore.isPresent()) {
            StudentScore attributeValue = studentScore.get();
            model.addAttribute("studentdetail", attributeValue);
        }
        return "detail";
    }

    @GetMapping("delete/student/enrollment/{id}")
    public String delete(Model model, @PathVariable("id") String studentId) {
        Optional<StudentScore> studentScore = apiService.getStudentScoreById(studentId);
        if (studentScore.isPresent()) {
            apiService.deleteById(studentScore);
            List<StudentScore> studentScore1 = apiService.getAllStudentScore();
            model.addAttribute("studentScore", studentScore1);
        }
        return "student_enrollment";
    }

    @GetMapping("/assignment/category")
    public String assignmentCategory(Model model) {
        List<AssignmentCategory> assignmentCategories = apiService.getAssignmentCategories();
        model.addAttribute("assignmentCategories", assignmentCategories);
        return "assignment_category";
    }

    @GetMapping("/detail_form")
    public String showRegistrationForm(Model model) {
        StudentScore studentScore = new StudentScore();
        model.addAttribute("studentdetail", studentScore);
        return "detail";
    }

    @GetMapping("/average/scorebystudent/{studentName}")
    public String ratingByStudentName(Model model, @PathVariable("studentName") String studentName) {
        List<Student> students = apiService.getAllStudents();
        Map<String, List<Student>> stringListMap = apiService.groupByStudentName(students, studentName);
        List<SubjectDto> subjectsByStudentName = apiService.getSubjectsByStudentName(stringListMap);
        StudentDto studentDto = new StudentDto(subjectsByStudentName, studentName);
        model.addAttribute("average", studentDto);
        return "ratebyStudent";
    }

    @GetMapping("/average/scorebysubject/{subjectName}")
    public String ratingBySubjectName(Model model, @PathVariable("subjectName") String subjectName) {
        List<Student> students = apiService.getAllStudents();
        Map<String, List<Student>> stringListMap = apiService.groupBySubjectName(students, subjectName);
        List<StudentDto> studentsByStudentName = apiService.getStudentsBySubjectName(stringListMap);
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setStudentDtos(studentsByStudentName);
        subjectDto.setSubject(subjectName);
        model.addAttribute("average", subjectDto);
        return "ratebySubject";
    }

    @GetMapping("/health")
    public String healthStatus() {
        return "Success";
    }
}

