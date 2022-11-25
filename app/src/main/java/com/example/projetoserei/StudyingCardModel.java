package com.example.projetoserei;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class StudyingCardModel {

    private final String collectionUsers = Constants.collectionUsers;
    private final String collectionStudyingCards = Constants.collectionStudyingCards;

    public StudyingCardModel() {
    }

    public boolean CreateCard(String userId, StudyingCard card, String cardId){

        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference documentReference = dataBase.collection(collectionUsers).document(userId).
                collection(collectionStudyingCards).document(cardId);

        Task task1 = documentReference.set(card);

        do {

        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return true;
        }
        else{
            Exception exception1 = task1.getException();
            return false;
        }

    }

    public Map<String, StudyingCard> GetUserStudyingCards(String userId){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference1 = database.collection(collectionUsers).document(userId).
                collection(collectionStudyingCards);

        Task task1 = collectionReference1.get();

        do {
        }
        while (!task1.isComplete());

        if (task1.isSuccessful()){

            QuerySnapshot querySnapshot = (QuerySnapshot) task1.getResult();
            Map<String, StudyingCard> studyingCardsMap= GetStudyingCardsList(querySnapshot);
            return studyingCardsMap;
        }
        else{
            return null;
        }

    }

    private Map<String, StudyingCard> GetStudyingCardsList(QuerySnapshot querySnapshot){

        Map<String, StudyingCard> studyingCardsMap = new HashMap<>();

        for (QueryDocumentSnapshot document : querySnapshot) {

            String StudyingCardId = document.getId();
            StudyingCard studyingCard = document.toObject(StudyingCard.class);
            studyingCardsMap.put(StudyingCardId, studyingCard);
        }

        return studyingCardsMap;
    }

    public boolean DeleteStudyingCard(String userId, String cardId){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(collectionUsers).document(userId).
                collection(collectionStudyingCards).document(cardId);

        Task task1 = documentReference.delete();

        do {
        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean UpdateStudyingCard(String userId, String cardId, StudyingCard card){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(collectionUsers).document(userId).
                collection(collectionStudyingCards).document(cardId);

        Task task1 = documentReference.set(card);

        do {
        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return true;
        }
        else{
            return false;
        }
    }

}
