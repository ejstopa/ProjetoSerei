package com.example.projetoserei;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class CardModel {

    private String collectionCards = Constants.collectionCards;

    public CardModel() {

    }

    public String CreateCard(Card card){

        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference cardReference = dataBase.collection(collectionCards).document();
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

}
