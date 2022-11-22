package com.example.projetoserei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class PublicCardsActivity extends AppCompatActivity {

    WebView webViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_cards);

        getSupportActionBar().hide();

        webViewContent = (WebView) findViewById(R.id.webViewPublicCards);
        webViewContent.addJavascriptInterface(this, "PublicCardsActivity" );
        webViewContent.addJavascriptInterface(new CategoryModel(this), "CategoryModel" );
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.loadUrl("file:///android_asset/public_cards.html");

    }

    @JavascriptInterface
    public void ShowStudyingCardsActivity(){

        Intent intent1 = new Intent(PublicCardsActivity.this, MainActivity.class);
        startActivity(intent1);
    }

    private Map<String, Card> GetCardsMapByCategoryDatabase(String category, int limit){

        CardModel cardModel = new CardModel();
        Map<String, Card> filteredCardsMap = cardModel.GetPublicCardsByCategory(category, limit);

        if (filteredCardsMap != null){
            return filteredCardsMap;
        }
        else{
            MessageErrorFilteringCards();
            return null;
        }

    }

    @JavascriptInterface
    public String GetCardsMapByCategoryJson(String category, int limit){

        Map<String, Card> filteredCardsMap = GetCardsMapByCategoryDatabase(category, limit);
        String filteredCardsListJson = new Gson().toJson(filteredCardsMap);

        return filteredCardsListJson;
    }

    @JavascriptInterface
    public void AddToStudyingCards(String cardId){

        CardModel cardModel = new CardModel();
        Card card = cardModel.GetCard(cardId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StudyingCardModel studyingCardModel = new StudyingCardModel();
        Map<String, StudyingCard> studyingCardsMap = studyingCardModel.GetUserStudyingCards(user.getUid());
        StudyingCard studyingCard = studyingCardsMap.get(cardId);

        if (studyingCard == null){

            StudyingCard studyingCardToCreate = NewCardActivity.CreateStudyingCardObject(this, cardId, card.getName(),
                    card.getDescription(), card.getCategory(), true, card.getQuestionQuantity(), card.getUserId(),
                    card.getQuestionQuantity(), false);

            CreateStudyingCardDatabase(user.getUid(),studyingCardToCreate, cardId);
            MessageAddedToStudyingCards();
        }
        else{
            MessageAlreadyStudyingCard();
        }

    }

    private boolean CreateStudyingCardDatabase(String userId, StudyingCard card, String cardId){

        StudyingCardModel studyingCardModel1 = new StudyingCardModel();

        if (!studyingCardModel1.CreateCard(userId, card, cardId)){
            NewCardActivity.MessageErrorAddingCard(this);
            return false;
        }
        else{
            return true;
        }

    }

    private void MessageErrorFilteringCards(){

        Toast.makeText(this, "Ocorreu um erro ao tentar filtrar os baralhos.", Toast.LENGTH_LONG).show();
    }

    private void MessageAlreadyStudyingCard(){

        Toast.makeText(this, "Você já está estudando esse baralho.", Toast.LENGTH_SHORT).show();
    }

    private void MessageAddedToStudyingCards(){

        Toast.makeText(this, "Baralho adicionado para estudo.", Toast.LENGTH_SHORT).show();
    }



}