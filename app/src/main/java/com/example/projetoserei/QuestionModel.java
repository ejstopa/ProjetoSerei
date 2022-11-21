package com.example.projetoserei;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionModel {

    private String collectionCards = Constants.collectionCards;
    private String collectionQuestions = Constants.collectionQuestions;
    private String collectionQuestionSummary = Constants.collectionQuestionSummary;
    private String documentQuestionSummaryList = Constants.documentQuestionSummaryList;
    private String fieldQuestion = Constants.fieldQuestion;
    private String fieldAnswer = Constants.fieldAnswer;
    private String fieldQuestionImage = Constants.fieldQuestionImage;

    Context context1;

    public QuestionModel(Context c) {

        context1 = c;
    }

    public boolean CreateUpdateQuestions(List<Question> questionList, String cardId, QuestionImageStorageModel questionImageStorageModel ){

        try {
            Map<String, Object> questionSummaryListData = new HashMap<>();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference questionReference;

            for (Question question : questionList) {

                questionReference = SetQuestionReference(question.getId(), cardId, database);
                String questionId = questionReference.getId();
                questionSummaryListData.put(questionId, question.getQuestion());

                if (question.isNewQuestion() || question.isUpdateQuestion()){

                    Map<String, Object> questionData = new HashMap<>();
                    Uri imageUri = question.getNewImageUri();
                    String imageStorageName;

                    questionData.put(fieldQuestion, question.getQuestion());
                    questionData.put(fieldAnswer, question.getAnswer());

                    if (question.getQuestionImage() != null){
                        if (imageUri == null){
                            questionData.put(fieldQuestionImage, question.getQuestionImage());
                        }
                        else{
                            if (!questionImageStorageModel.DeleteImage(question.getQuestionImage())){
                                return false;
                            }
                        }
                    }

                    if (imageUri != null){
                        imageStorageName = questionImageStorageModel.StoreImage(cardId, questionId, imageUri);

                        if (imageStorageName == null){
                            return false;
                        }

                        questionData.put(fieldQuestionImage, imageStorageName);
                    }

                    Task task1 = questionReference.set(questionData);

                    do {
                    }while (!task1.isComplete());

                    if (!task1.isSuccessful()){
                        return false;
                    }
                }
            }

            if (!CreateQuestionSummaryList(database, questionSummaryListData, cardId)){
                return false;
            }

            return true;
        }
        catch(Exception e){
            return false;
        }

    }

    public boolean DeleteQuestions(List<Question> questionsToDeleteList, String cardId, QuestionImageStorageModel questionImageStorageModel){

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        for (Question question : questionsToDeleteList) {

            if (!DeleteQuestionDocument(database, question, cardId)){
                return false;
            }

            if (!DeleteQuestionImage(question, questionImageStorageModel)){
                return false;
            }
        }

        return true;
    }

    public Map<String, Object> GetQuestionsSummaryList(String cardId){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(collectionCards).document(cardId).
                collection(collectionQuestionSummary).document(documentQuestionSummaryList);

        Task task1 = documentReference.get();

        do {
        }
        while (!task1.isComplete());

        if (task1.isSuccessful()){
            DocumentSnapshot document = (DocumentSnapshot)task1.getResult();
            Map<String, Object> result = document.getData();

            return result;
        }
        else{
            return null;
        }

    }

    public Question GetQuestion(String cardId, String questionId){

        Question question;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference document =  database.collection(collectionCards).document(cardId).
                collection(collectionQuestions).document(questionId);

        Task task1 = document.get();

        do {
        }
        while (!task1.isComplete());

        if (task1.isSuccessful()){
            question = ((DocumentSnapshot)task1.getResult()).toObject(Question.class);
            question.setId(questionId);
            return question;
        }
        else{
            return null;
        }

    }

    private boolean CreateQuestionSummaryList(FirebaseFirestore dataBase, Map<String,
            Object> questionSummaryListData, String cardId){

        try {
            dataBase.collection(collectionCards).document(cardId).collection(collectionQuestionSummary).
                    document(documentQuestionSummaryList).set(questionSummaryListData);

            return true;
        }
        catch (Exception e){

            return false;

        }

    }

    private DocumentReference SetQuestionReference(String questionId, String cardId, FirebaseFirestore dataBase){

        DocumentReference questionReference;

        if (questionId == null){
            questionReference = dataBase.collection(collectionCards).document(cardId).
                    collection(collectionQuestions).document();
        }
        else{
            questionReference = dataBase.collection(collectionCards).document(cardId).
                    collection(collectionQuestions).document(questionId);
        }

        return questionReference;
    }

    private boolean DeleteQuestionDocument(FirebaseFirestore database, Question question, String cardId){

        DocumentReference questionDocument = database.collection(collectionCards).document(cardId).
                collection(collectionQuestions).document(question.getId());

        Task task1 = questionDocument.delete();

        do {
        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean DeleteQuestionImage(Question question, QuestionImageStorageModel questionImageStorageModel){

        String imageName = question.getQuestionImage();

        if (imageName != null){
            if (!questionImageStorageModel.DeleteImage(imageName)){
                return false;
            }
        }

        return true;
    }
}
