package com.fmbg.moobuni.Model;

public class Question {
    private String question;
    private String questionid;
    private String publisher;
    private String university;
    private String department;

    public Question(String question, String questionid, String publisher, String university, String department) {
        this.question = question;
        this.questionid = questionid;
        this.publisher = publisher;
        this.university = university;
        this.department = department;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
