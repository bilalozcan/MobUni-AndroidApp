package com.fmbg.moobuni.Model;

public class Answer {
    private String answer;
    private String publisher;

    public Answer(String answer, String publisher) {
        this.answer = answer;
        this.publisher = publisher;
    }

    public Answer() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
