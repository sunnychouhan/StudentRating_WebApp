package com.webapp.rating.model.dto;

import java.util.List;

public class SubjectDto extends CommonDto {
    List<StudentDto> studentDtos;
    String subject;

    public List<StudentDto> getStudentDtos() {
        return studentDtos;
    }

    public void setStudentDtos(List<StudentDto> studentDtos) {
        this.studentDtos = studentDtos;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
