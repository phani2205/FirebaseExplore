package com.firebase.explore;

public class Questions {
    private String question;

    private String[] answers;

    private String correctIndex;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(String correctIndex) {
        this.correctIndex = correctIndex;
    }

    @Override
    public String toString() {
        return "ClassPojo [question = " + question + ", answers = " + answers + ", correctIndex = " + correctIndex + "]";
    }
}
