package com.firebase.explore;

import java.util.List;

public class Questions {
    private String question;

    private List<String> answers;

    private int correctIndex;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    @Override
    public String toString() {
        return "ClassPojo [question = " + question + ", answers = " + answers + ", correctIndex = " + correctIndex + "]";
    }
}
