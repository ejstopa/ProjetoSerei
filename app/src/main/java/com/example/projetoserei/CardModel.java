package com.example.projetoserei;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardModel {

    private final String collectionCards = Constants.collectionCards;
    private final String fieldCategory = Constants.fieldCategory;
    private final String fieldPublicCard = Constants.fieldPublicCard;

    public CardModel() {

    }

    public String CreateCard(Card card){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference cardReference = database.collection(collectionCards).document();
        String cardId = cardReference.getId();

        Task task1 = cardReference.set(card);

        do {

        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return cardId;
        }
        else{
            return null;
        }

    }

    public boolean UpdateCard(String cardId, Card card){

        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference cardReference = dataBase.collection(collectionCards).document(cardId);

        Task task1 = cardReference.set(card);

        do {
        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            return true;
        }
        else{
            return false;
        }

    }

    public Card GetCard(String cardId){

        Card card;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference document =  database.collection(collectionCards).document(cardId);

        Task task1 = document.get();

        do {
        }
        while (!task1.isComplete());

        if (task1.isSuccessful()){
            card = ((DocumentSnapshot)task1.getResult()).toObject(Card.class);
            return card;
        }
        else{
            return null;
        }

    }

    public Map<String, Card> GetPublicCardsByCategory(String category, int limit){

        Map<String, Card> filteredCardsList = new HashMap<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Query query = database.collection(collectionCards).whereEqualTo(fieldCategory, category).
                whereEqualTo(fieldPublicCard, true).limit(limit);

        Task task1 = query.get();

        do {
        }while (!task1.isComplete());

        if (!task1.isSuccessful()){
            return null;
        }

        QuerySnapshot querySnapshot = (QuerySnapshot)task1.getResult();

        for (QueryDocumentSnapshot document : querySnapshot){

            String cardId = document.getId();
            filteredCardsList.put(cardId, document.toObject(Card.class));
        }

        return filteredCardsList;

    }
}
