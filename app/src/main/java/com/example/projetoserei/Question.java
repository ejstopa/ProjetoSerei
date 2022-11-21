package com.example.projetoserei;

import android.net.Uri;

public class Question{

    private String Id;
    private String Question;
    private String QuestionImage;
    private Uri NewImageUri;
    private String Answer;
    private boolean NewQuestion;
    private boolean UpdateQuestion;

    public Question() {


    }

    public Question(Question questionToCopy) {
        Id = questionToCopy.Id;
        Question = questionToCopy.Question;
        QuestionImage = questionToCopy.QuestionImage;
        NewImageUri = questionToCopy.NewImageUri;
        Answer = questionToCopy.Answer;
        NewQuestion = questionToCopy.NewQuestion;
        UpdateQuestion = questionToCopy.UpdateQuestion;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestionImage() {
        return QuestionImage;
    }

    public void setQuestionImage(String questionImage) {
        QuestionImage = questionImage;
    }

    public Uri getNewImageUri() {
        return NewImageUri;
    }

    public void setNewImageUri(Uri newImageUri) {
        NewImageUri = newImageUri;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public boolean isNewQuestion() {
        return NewQuestion;
    }

    public void setNewQuestion(boolean newQuestion) {
        this.NewQuestion = newQuestion;
    }

    public boolean isUpdateQuestion() {
        return UpdateQuestion;
    }

    public void setUpdateQuestion(boolean updateQuestion) {
        UpdateQuestion = updateQuestion;
    }

}
