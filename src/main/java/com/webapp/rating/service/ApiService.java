package com.webapp.rating.service;

import com.webapp.rating.model.dto.StudentDto;
import com.webapp.rating.model.dto.SubjectDto;
import com.webapp.rating.model.entity.AssignmentCategory;
import com.webapp.rating.model.entity.StudentScore;
import com.webapp.rating.model.vo.Assignment;
import com.webapp.rating.model.vo.Student;
import com.webapp.rating.model.vo.Subject;
import com.webapp.rating.repository.AssignmentCategoryRepo;
import com.webapp.rating.repository.StudentScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ApiService {
    public static final String DEFAULT_POINTS = "100";
    public static final String DEFAULT_SUBMISSION_OCT_2022 = "6-Oct-2022";
    public static final String ONE_1 = "1";
    public static final String TWO_2 = "2";
    public static final String THREE_3 = "3";
    public static final String FOUR_4 = "4";
    public static final String TEST = "test";
    public static final String QUIZ = "quiz";
    public static final String LAB = "lab";
    public static final String PROJECT = "project";

    @Autowired
    StudentScoreRepo studentScoreRepo;
    @Autowired
    AssignmentCategoryRepo assignmentCategoryRepo;

    public List<StudentScore> getAllStudentScore() {
        return studentScoreRepo.findAll();
    }


    public void saveStudent(StudentScore studentScore) {
        studentScoreRepo.save(studentScore);
    }

    public void deleteById(Optional<StudentScore> studentScore) {
        studentScoreRepo.delete(studentScore.get());
    }

    public Optional<StudentScore> getStudentScoreById(String studentId) {
        Optional<StudentScore> studentScore = studentScoreRepo.findById(Long.valueOf(studentId));
        return studentScore;
    }

    public List<Student> getAllStudents() {
        return studentScoreRepo.findAll()
                .stream()
                .map(score -> {
                    Assignment assignment =
                            new Assignment(score.getCategory(),
                                    String.valueOf(score.getPoints()),
                                    score.getSubmissionDate().toString());
                    Subject subject = new Subject(assignment, score.getSubject());
                    return new Student(subject, score.getName(), String.valueOf(score.getStudentId()));
                })
                .collect(Collectors.toList());
    }

    public List<AssignmentCategory> getAssignmentCategories() {
        List<AssignmentCategory> assignmentCategories = assignmentCategoryRepo.findAll();
        return assignmentCategories;
    }

    public Map<String, List<Student>> groupByStudentName(List<Student> studentList, String studentName) {
        return studentList.stream()
                .filter(student -> student.getName().equals(studentName))
                .collect(Collectors.groupingBy(student -> student.getSubjects().getSubject()));
    }

    public Map<String, List<Student>> groupBySubjectName(List<Student> studentList, String subjectName) {
        return studentList.stream()
                .filter(student -> student.getSubjects().getSubject().equals(subjectName))
                .collect(Collectors.groupingBy(student -> student.getName()));
    }


    public List<SubjectDto> getSubjectsByStudentName(Map<String, List<Student>> bySubject) {

        return bySubject.entrySet()
                .stream()
                .map(sujbectKey -> {
                    String subject = sujbectKey.getKey();
                    List<Student> students = sujbectKey.getValue();

                    ArrayList<Double> test = new ArrayList();
                    ArrayList<Double> quiz = new ArrayList();
                    ArrayList<Double> lab = new ArrayList();
                    ArrayList<Double> project = new ArrayList();
                    students.stream()
                            .forEach(student -> {
                                Assignment assignmentList = student.getSubjects().getAssignmentList();

                                if (assignmentList.getCategory().startsWith(TEST)) {
                                    test.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(QUIZ)) {
                                    quiz.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(LAB)) {
                                    lab.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(PROJECT)) {
                                    project.add(Double.valueOf(assignmentList.getPoints()));
                                }
                            });


                    Double testRatings = deriveRating(test, test.size(), 40.0);
                    Double quizRatings = deriveRating(quiz, quiz.size(), 20.0);
                    Double labRatings = deriveRating(lab, lab.size(), 10.0);
                    Double projectRatings = deriveRating(project, project.size(), 30.0);


                    SubjectDto dto = new SubjectDto();
                    dto.setSubject(subject);

                    dto.setLab(getIfApplicable(lab, labRatings));
                    dto.setQuiz(getIfApplicable(quiz, quizRatings));
                    dto.setTest(getIfApplicable(test, testRatings));
                    dto.setProject(getIfApplicable(project, projectRatings));

                    Double ovrallRatings = labRatings + quizRatings + testRatings + projectRatings;
                    dto.setOverall(String.valueOf(ovrallRatings));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public static String getIfApplicable(ArrayList<Double> lab, Double labRatings) {
        return lab.size() > 0 ? String.valueOf(labRatings) : "NA";
    }

    Double deriveRating(ArrayList<Double> assignments, int numberOfAssignments, Double assigmentCategory) {
        return assignments.stream()
                .map(score -> calculatePercentage(numberOfAssignments, score, assigmentCategory))
                .reduce((double) 0, (a, b) -> a + b);
    }


    Double calculatePercentage(int numberOfAssignments, Double points, Double assignmentCategory) {
        return numberOfAssignments != 0 ? (((assignmentCategory / numberOfAssignments) * points) / 100) : 0;
    }

    public List<StudentDto> getStudentsBySubjectName(Map<String, List<Student>> bySubject) {
        return bySubject.entrySet()
                .stream()
                .map(stringListEntry -> {

                    String students = stringListEntry.getKey();
                    List<Student> subjects = stringListEntry.getValue();

                    ArrayList<Double> test = new ArrayList();
                    ArrayList<Double> quiz = new ArrayList();
                    ArrayList<Double> lab = new ArrayList();
                    ArrayList<Double> project = new ArrayList();
                    subjects.stream()
                            .forEach(stdnt -> {
                                Assignment assignmentList = stdnt.getSubjects().getAssignmentList();

                                if (assignmentList.getCategory().startsWith(TEST)) {
                                    test.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(QUIZ)) {
                                    quiz.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(LAB)) {
                                    lab.add(Double.valueOf(assignmentList.getPoints()));

                                } else if (assignmentList.getCategory().startsWith(PROJECT)) {
                                    project.add(Double.valueOf(assignmentList.getPoints()));
                                }
                            });


                    Double testRatings = deriveRating(test, test.size(), 40.0);
                    Double quizRatings = deriveRating(quiz, quiz.size(), 20.0);
                    Double labRatings = deriveRating(lab, lab.size(), 10.0);
                    Double projectRatings = deriveRating(project, project.size(), 30.0);


                    StudentDto dto = new StudentDto();
                    dto.setName(students);

                    dto.setLab(getIfApplicable(lab, labRatings));
                    dto.setQuiz(getIfApplicable(quiz, quizRatings));
                    dto.setTest(getIfApplicable(test, testRatings));
                    dto.setProject(getIfApplicable(project, projectRatings));

                    Double ovrallRatings = labRatings + quizRatings + testRatings + projectRatings;
                    dto.setOverall(String.valueOf(ovrallRatings));
                    return dto;
                }).collect(Collectors.toList());
    }

}
