package com.example.projetoserei;

public class StudyingQuestion {

    private String id;
    private int targetOrder;

    public StudyingQuestion() {
    }

    public StudyingQuestion(StudyingQuestion studyingQuestion) {
        this.id = studyingQuestion.id;
        targetOrder = studyingQuestion.targetOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTargetOrder() {
        return targetOrder;
    }

    public void setTargetOrder(int targetOrder) {
        this.targetOrder = targetOrder;
    }
}
