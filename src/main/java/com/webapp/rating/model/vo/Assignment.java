package com.webapp.rating.model.vo;

public class Assignment {
    String category;
    String points;
    String submissionDate;

    public Assignment(String category, String points, String submissionDate) {
        this.category = category;
        this.points = points;
        this.submissionDate = submissionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}
