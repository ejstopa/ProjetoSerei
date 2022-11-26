package com.example.projetoserei;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.List;

public class StudyingCardPlay {

    @ServerTimestamp
    private Timestamp StartedWhen = Timestamp.now();
    private boolean finished;
    @ServerTimestamp
    private Timestamp FinishedWhen;
    private List<String> rightAnswers = new ArrayList<>();
    private List<String> wrongAnswers = new ArrayList<>();

    public StudyingCardPlay() {
    }

    public Timestamp getStartedWhen() {
        return StartedWhen;
    }

    public void setStartedWhen(Timestamp startedWhen) {
        StartedWhen = startedWhen;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Timestamp getFinishedWhen() {
        return FinishedWhen;
    }

    public void setFinishedWhen(Timestamp finishedWhen) {
        FinishedWhen = finishedWhen;
    }

    public List<String> getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(List<String> rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
