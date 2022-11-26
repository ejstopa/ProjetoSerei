package com.example.projetoserei;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;
import java.util.Map;

public class StudyingCard  extends  Card{

    @ServerTimestamp private Timestamp AddedWhen = Timestamp.now();
    private int TargetQuestions;
    private boolean OwnCard;
    private List<StudyingQuestion> StudyingQuestionsList;
    private List<StudyingCardPlay> StudyingCardPlaysList;

    public Timestamp getAddedWhen() {
        return AddedWhen;
    }

    public void setAddedWhen(Timestamp addedWhen) {
        AddedWhen = addedWhen;
    }

    public int getTargetQuestions() {
        return TargetQuestions;
    }

    public void setTargetQuestions(int targetQuestions) {
        TargetQuestions = targetQuestions;
    }

    public boolean isOwnCard() {
        return OwnCard;
    }

    public void setOwnCard(boolean ownCard) {
        OwnCard = ownCard;
    }

    public List<StudyingQuestion> getStudyingQuestionsList() {
        return StudyingQuestionsList;
    }

    public void setStudyingQuestionsList(List<StudyingQuestion> studyingQuestionsList) {

        StudyingQuestionsList = studyingQuestionsList;
    }

    public List<StudyingCardPlay> getStudyingCardPlaysList() {
        return StudyingCardPlaysList;
    }

    public void setStudyingCardPlaysList(List<StudyingCardPlay> studyingCardPlaysList) {
        StudyingCardPlaysList = studyingCardPlaysList;
    }
}
