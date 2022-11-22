package com.example.projetoserei;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Base64;
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
            SetStudyingCardsMap();
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

        playingCardId = cardId;
        playingQuestionIndex = 0;
        SortStudyingQuestionList(cardId);

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

        StudyingCardModel studyingCardModel = new StudyingCardModel();
        studyingCardsMap = studyingCardModel.GetUserStudyingCards(userId);
    }

    private void SortStudyingQuestionList(String cardId){

        List<StudyingQuestion> StudyingQuestionsList = GetStudyingQuestionsList(cardId);



        StudyingQuestionsList.sort(Comparator.comparing(StudyingQuestion::getTargetOrder));
    }

    @JavascriptInterface
    public boolean SetNextQuestion(){

        playingQuestionIndex ++;

        if (playingQuestionIndex <= GetStudyingQuestionsList(playingCardId).size() -1){
            return true;
        }
        else{
            return false;
        }
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

        int targetQuestionsQuantity = GetPlayingCard().getTargetQuestions();
        int TargetQuestionsRemaining = targetQuestionsQuantity - playingQuestionIndex;

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

    // endregion




}