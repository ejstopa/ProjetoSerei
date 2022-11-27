package com.example.projetoserei;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;

public class StudyingCardPlay {

    @ServerTimestamp
    private Timestamp StartedWhen = Timestamp.now();
    private boolean Finished;
    @ServerTimestamp
    private Timestamp FinishedWhen;
    private List<String> RightAnswers = new ArrayList<>();
    private List<String> WrongAnswers = new ArrayList<>();

    public StudyingCardPlay() {
    }

    public Timestamp getStartedWhen() {
        return StartedWhen;
    }

    public void setStartedWhen(Timestamp startedWhen) {
        StartedWhen = startedWhen;
    }

    public boolean isFinished() {
        return Finished;
    }

    public void setFinished(boolean finished) {
        this.Finished = finished;
    }

    public Timestamp getFinishedWhen() {
        return FinishedWhen;
    }

    public void setFinishedWhen(Timestamp finishedWhen) {
        FinishedWhen = finishedWhen;
    }

    public List<String> getRightAnswers() {
        return RightAnswers;
    }

    public void setRightAnswers(List<String> rightAnswers) {
        this.RightAnswers = rightAnswers;
    }

    public List<String> getWrongAnswers() {
        return WrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.WrongAnswers = wrongAnswers;
    }
}
