package com.example.projetoserei;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    WebView webViewNavigation;
    WebView webViewContent;
    Map<String, StudyingCard> studyingCardsMap;
    String playingCardId;
    int playingQuestionIndex;
    TextSpeaker textSpeaker;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK){

                        recreate();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        webViewNavigation = (WebView) findViewById(R.id.webViewNavigation);
        webViewNavigation.addJavascriptInterface(new AccountModel(this), "AccountModel" );
        webViewNavigation.addJavascriptInterface(new CategoryModel(this), "CategoryModel" );
        webViewNavigation.addJavascriptInterface(this, "MainActivity" );
        webViewNavigation.getSettings().setJavaScriptEnabled(true);
        webViewNavigation.loadUrl("file:///android_asset/navigation.html");

        webViewContent = (WebView) findViewById(R.id.webViewContent);
        webViewContent.addJavascriptInterface(this, "MainActivity" );
        webViewContent.getSettings().setJavaScriptEnabled(true);



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){
            ShowLoginActivity();
        }
        else{
            ShowStudyingCardsContent();
            CreateTextSpeakerObject();
        }
    }

    @Override
    public void onBackPressed(){

        if (playingCardId != null){

            ShowStudyingCardsContent();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (textSpeaker != null){
            textSpeaker.Shutdown();
        }

    }

    // region Activity Functions
    @JavascriptInterface
    public void ShowLoginActivity(){
        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent1);
        finish();
    }

    @JavascriptInterface
    public void ShowNewCardActivity(){

        Intent intent1 = new Intent(MainActivity.this, NewCardActivity.class);
        startActivityIntent.launch(intent1);
    }

    @JavascriptInterface
    public void ShowEditCardActivity(String cardId){

        Intent intent1 = new Intent(MainActivity.this, NewCardActivity.class);
        intent1.putExtra("cardId", cardId);

        startActivityIntent.launch(intent1);
    }

    @JavascriptInterface
    public void ShowPublicCardsActivity(){

        Intent intent1 = new Intent(MainActivity.this, PublicCardsActivity.class);
        startActivity(intent1);
    }

    // endregion

    // region Content Functions
    @JavascriptInterface
    public void ShowStudyingCardsContent(){

        if (textSpeaker != null){
            textSpeaker.Stop();
        }

        SetStudyingCardsMap();
        playingCardId = null;
        playingQuestionIndex = 0;

        webViewContent.post(new Runnable() {
            @Override
            public void run() {
                webViewContent.loadUrl("about:blank");
                webViewContent.loadUrl("file:///android_asset/studying_cards.html");
            }
        });

        webViewNavigation.post(new Runnable() {
            @Override
            public void run() {

                View viewBlockUI = findViewById(R.id.viewBlockUI);

                if (viewBlockUI.getVisibility() == View.VISIBLE){
                    viewBlockUI.setVisibility(View.INVISIBLE);
                    webViewNavigation.loadUrl("javascript:unblockUI()");

                }
                else{
                        webViewNavigation.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            webViewNavigation.loadUrl("javascript:unblockUI()");
                        }
                    });
                }

            }
        });
    }

    @JavascriptInterface
    public void ShowPlayCardContent(String cardId){

        if (studyingCardsMap.get(cardId).getTargetQuestions() == 0 ){
            return;
        }

        playingCardId = cardId;
        playingQuestionIndex = 0;
        SortStudyingQuestionList(cardId);
        SetStudyingCardCurrentPlay(cardId);

        webViewContent.post(new Runnable() {
            @Override
            public void run() {
                webViewContent.loadUrl("about:blank");
                webViewContent.loadUrl("file:///android_asset/play_card.html");

                webViewContent.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {

//                        webViewContent.loadUrl();
                    }
                });
            }
        });

        webViewNavigation.post(new Runnable() {
            @Override
            public void run() {
                webViewNavigation.loadUrl("javascript:blockUI()");
                View viewBlockUI = findViewById(R.id.viewBlockUI);
                viewBlockUI.setVisibility(View.VISIBLE);
            }
        });

    }

    // endregion

    // region Setting Information

    private void SetStudyingCardsMap(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        studyingCardsMap = GetStudyingCardsDatabase(userId);
    }

    private void SortStudyingQuestionList(String cardId){

        List<StudyingQuestion> StudyingQuestionsList = GetStudyingQuestionsList(cardId);

        StudyingQuestionsList.sort(Comparator.comparing(StudyingQuestion::getTargetOrder));
        Collections.reverse(StudyingQuestionsList);
    }

    @JavascriptInterface
    public boolean SetNextQuestion(boolean answeredRight){

        StudyingCard playingCard = GetPlayingCard();
        StudyingQuestion studyingQuestion =  playingCard.getStudyingQuestionsList().get(playingQuestionIndex);
        studyingQuestion.setTargetOrder(0);
        SetStudyingCardTargetQuestions();

        if (answeredRight){
            SetStudyingCardPlayQuestionRight(studyingQuestion.getId());
        }
        else{
            SetStudyingCardPlayQuestionWrong(studyingQuestion.getId());
        }

        playingQuestionIndex ++;

        if (GetTargetQuestionsRemaining() > 0 &&
                GetStudyingQuestionsList(playingCardId).get(playingQuestionIndex).getTargetOrder() != 0){
            return true;
        }
        else{
            SetStudyingCardPlayFinished();
            return false;
        }
    }

    private void SetStudyingCardTargetQuestions(){

        int targetQuestions = GetPlayingCard().getStudyingQuestionsList().stream().filter(e -> e.getTargetOrder() != 0).collect(Collectors.toList()).size();
        GetPlayingCard().setTargetQuestions(targetQuestions);
    }

    private void SetStudyingCardPlayQuestionRight(String questionId){

        StudyingCardPlay studyingCardPlay = GetPlayingCardCurrentPlay();
        studyingCardPlay.getRightAnswers().add(questionId);
    }

    private void SetStudyingCardPlayQuestionWrong(String questionId){

        StudyingCardPlay studyingCardPlay = GetPlayingCardCurrentPlay();
        studyingCardPlay.getWrongAnswers().add(questionId);
    }

    private void SetStudyingCardCurrentPlay(String cardId){

        StudyingCard studyingCard = studyingCardsMap.get(cardId);

        if (studyingCard.getStudyingCardPlaysList() == null){

            studyingCard.setStudyingCardPlaysList(new ArrayList<>());
            studyingCard.getStudyingCardPlaysList().add(new StudyingCardPlay());
        }
        else{
            List<StudyingCardPlay> studyingCardPlaysList = studyingCard.getStudyingCardPlaysList();
            StudyingCardPlay lastStudyingCardPlay = studyingCardPlaysList.get(studyingCardPlaysList.size() - 1);

            if (lastStudyingCardPlay.isFinished()){
                studyingCardPlaysList.add(new StudyingCardPlay());
            }
        }
    }

    private void SetStudyingCardPlayFinished(){

        StudyingCardPlay studyingCardPlay = GetPlayingCardCurrentPlay();
        studyingCardPlay.setFinished(true);
        studyingCardPlay.setFinishedWhen(Timestamp.now());
    }

    @JavascriptInterface
    public void FilterStudyingCards(String category){

        String StudyingCardsFilteredJson;

        if (!Objects.equals(category, Constants.valueAllCategories)){
            StudyingCardsFilteredJson = GetStudyingCardsJson(category);
        }
        else{
            StudyingCardsFilteredJson = GetStudyingCardsJson();
        }

        webViewContent.post(new Runnable() {
            @Override
            public void run() {

                webViewContent.loadUrl("javascript:updateStudyingCards('" + StudyingCardsFilteredJson + "')");
            }
        });

    }

    //endregion

    // region Getting Information
    @JavascriptInterface
    public String GetCurrentUserId(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        return userId;
    }

    @JavascriptInterface
    public String GetStudyingCardsJson(){

        Gson gson = new Gson();
        String studyingCardsMapJson = gson.toJson(studyingCardsMap);

        return studyingCardsMapJson;

    }

    @JavascriptInterface
    public String GetStudyingCardsJson(String categoryFilter){

        Map<String, StudyingCard> studyingCardsFilteredMap = studyingCardsMap.entrySet().stream().
                filter(card -> Objects.equals(card.getValue().getCategory(), categoryFilter)).
                collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        Gson gson = new Gson();
        String studyingCardsMapJson = gson.toJson(studyingCardsFilteredMap);

        return studyingCardsMapJson;

    }

    private StudyingCard GetPlayingCard(){

        StudyingCard playingCard = studyingCardsMap.get(playingCardId);
        return playingCard;
    }

    @JavascriptInterface
    public String GetPlayingCardJson(){

        String playingCardJson = new Gson().toJson(GetPlayingCard());
        return playingCardJson;
    }

    @JavascriptInterface
    public int GetTargetQuestionsRemaining(){

        int TargetQuestionsRemaining = GetPlayingCard().getTargetQuestions();

        return  TargetQuestionsRemaining;
    }

    private List<StudyingQuestion> GetStudyingQuestionsList(String cardId){

        List<StudyingQuestion> StudyingQuestionsList = studyingCardsMap.get(cardId).getStudyingQuestionsList();

        return StudyingQuestionsList;
    }

    private String GetPlayingQuestionId(){
        List<StudyingQuestion> studyingQuestionsList = GetStudyingQuestionsList(playingCardId);
        StudyingQuestion studyingQuestion = studyingQuestionsList.get(playingQuestionIndex);
        String studyingQuestionId = studyingQuestion.getId();

        return studyingQuestionId;
    }

    private StudyingCardPlay GetPlayingCardCurrentPlay(){

        StudyingCard studyingCard = GetPlayingCard();
        List<StudyingCardPlay> studyingCardPlaysList = studyingCard.getStudyingCardPlaysList();
        StudyingCardPlay studyingCardPlay = studyingCardPlaysList.get(studyingCardPlaysList.size() - 1);

        return studyingCardPlay;
    }

    @JavascriptInterface
    public int GetPlayingQuestionIndex(){
        return playingQuestionIndex;
    }

    @JavascriptInterface
    public String GetPlayingQuestionDataJson(){

        String playingQuestionId = GetPlayingQuestionId();
        Question question = GetQuestionDatabase(playingCardId, playingQuestionId);
        String questionJson = new Gson().toJson(question);

        return questionJson;
    }


    // endregion

    // region Text Speaker
    private void CreateTextSpeakerObject(){

        textSpeaker = new TextSpeaker(this);
    }

    @JavascriptInterface
    public void SpeakText(String text){

        textSpeaker.Speak(text);
    }

    // endregion

    // region Database

    private Map<String, StudyingCard> GetStudyingCardsDatabase(String userId){

        StudyingCardModel studyingCardModel = new StudyingCardModel();
        studyingCardsMap = studyingCardModel.GetUserStudyingCards(userId);

        return studyingCardsMap;
    }

    private Question GetQuestionDatabase(String cardId, String questionId){

        QuestionModel questionModel = new QuestionModel(this);
        Question question = questionModel.GetQuestion(cardId, questionId);

        if (question != null){
            return question;
        }
        else{
            return null;
        }

    }

    @JavascriptInterface
    public void DeleteStudyingCardDatabase(String cardId){

        StudyingCardModel studyingCardModel = new StudyingCardModel();

        if (studyingCardModel.DeleteStudyingCard(GetCurrentUserId(), cardId)){

            MessageStoppedStudyingCard();
            ShowStudyingCardsContent();
        }
        else{

            MessageErrorStoppingStudyingCard();
        }

    }

    @JavascriptInterface
    public String GetImageAsBytesDatabase(String imageName){

        QuestionImageStorageModel questionImageStorageModel = new QuestionImageStorageModel((this));
        byte[] imageBytes = questionImageStorageModel.GetImageAsBytes(imageName);

        if (imageBytes != null){
            String imageBytesString = Base64.getEncoder().encodeToString(imageBytes);
            return imageBytesString;
        }
        else{
            return null;
        }
    }

    @JavascriptInterface
    public boolean UpdateStudyingCardDatabase(){

        StudyingCard studyingCard = GetPlayingCard();
        StudyingCardModel studyingCardModel = new StudyingCardModel();

        if (studyingCardModel.UpdateStudyingCard(GetCurrentUserId(), playingCardId, studyingCard)){
            return true;
        }
        else{
            return false;
        }

    }

    @JavascriptInterface
    public void ResetStudyingCardTargetQuestionsDatabase(String cardId){

        playingCardId = cardId;

        StudyingCard studyingCard = studyingCardsMap.get(cardId);
        studyingCard.getStudyingQuestionsList().forEach(e -> e.setTargetOrder(1));
        SetStudyingCardTargetQuestions();

        UpdateStudyingCardDatabase();
        ShowStudyingCardsContent();

    }

    // endregion

    // region Messages
    private void MessageStoppedStudyingCard(){

        Toast.makeText(this, "Você deixou de estudar esse baralho", Toast.LENGTH_SHORT).show();
    }

    private void MessageErrorStoppingStudyingCard(){

        Toast.makeText(this, "Ocorreu um erro ao tentar deixar de estudar esse baralho", Toast.LENGTH_SHORT).show();
    }

    // endregion


}