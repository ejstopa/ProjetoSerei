package com.example.projetoserei;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewCardActivity extends AppCompatActivity {

    List<Question> questionsList = new ArrayList<Question>();
    List<Question> deleteQuestionsList = new ArrayList<Question>();

    WebView webView;

    ActivityResultLauncher<Intent> newQuestionActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK){

                        Intent intentQuestion = result.getData();
                        String question = intentQuestion.getStringExtra("question");
                        String answer = intentQuestion.getStringExtra("answer");
                        Uri imageUri = intentQuestion.getData();

                        Question question1 = CreateQuestionObject(question, answer, null, imageUri, true, false);

                        if (question1 != null){
                            questionsList.add(question1);
                            UpdateQuestionsSummary();
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> editQuestionActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK){

                        Intent intentQuestion = result.getData();
                        String questionId = intentQuestion.getStringExtra("questionId");
                        String questionImage = intentQuestion.getStringExtra("questionImage");
                        String question = intentQuestion.getStringExtra("question");
                        String answer = intentQuestion.getStringExtra("answer");
                        Uri imageUri = intentQuestion.getData();
                        Question question1 = null;

                        if (questionId != null){
                            question1 = CreateQuestionObject(question, answer, questionImage, imageUri,  false, true );
                            question1.setId(questionId);
                        }
                        else{
                            question1 = CreateQuestionObject(question, answer, questionImage, imageUri, true, false );
                        }

                        if (question1 != null){
                            int questionNumber = intentQuestion.getIntExtra("questionNumber", 0);
                            questionsList.set(questionNumber - 1, question1 );

                            UpdateQuestionsSummary();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        getSupportActionBar().hide();

        SetEditingCardQuestions();

        webView = (WebView) findViewById(R.id.webViewNewCard);
        webView.addJavascriptInterface(this, "NewCardActivity" );
        webView.addJavascriptInterface(new CategoryModel(this), "CategoryModel" );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/new_card.html");

    }

    // region Activity Functions
    @JavascriptInterface
    public void ShowNewQuestionActivity(){

        Intent intent1 = new Intent(NewCardActivity.this, NewQuestionActivity.class);
        newQuestionActivityLauncher.launch(intent1);
    }

    @JavascriptInterface
    public void ShowEditQuestionActivity(int questionNumber){

        Intent intent1 = new Intent(NewCardActivity.this, NewQuestionActivity.class);
        intent1.putExtra("editQuestion", true);
        intent1.putExtra("questionNumber", questionNumber);

        Question editingQuestion = questionsList.get(questionNumber - 1);

        if (!editingQuestion.isNewQuestion()){

            editingQuestion = GetQuestionDatabase(getCardId(), editingQuestion.getId());
            intent1.putExtra("questionId", editingQuestion.getId());

            if (editingQuestion == null){
                MessageErrorGettingQuestion();
                return;
            }

            if (editingQuestion.getQuestionImage() != null){
                intent1.putExtra("questionImage", editingQuestion.getQuestionImage());
            }
        }
        else {
            Uri imageUri = editingQuestion.getNewImageUri();

            if (imageUri != null){
                String imageName = imageUri.getLastPathSegment();
                intent1.putExtra("questionImage", imageName);
                intent1.setData(imageUri);
            }
        }

        intent1.putExtra("question", editingQuestion.getQuestion());
        intent1.putExtra("answer", editingQuestion.getAnswer());

        editQuestionActivityLauncher.launch(intent1);
    }

    @JavascriptInterface
    public void FinishSaveNewCard(String txtCardName, String txtCardDescription,
                               String txtCardCategory, boolean checkPublicCard){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        int questionQuantity = questionsList.size();

        Card card1 = CreateCardObject(txtCardName, txtCardDescription, txtCardCategory,
                                        checkPublicCard, questionQuantity, userId);

        String newCardId = CreateCardDatabase(card1);
        if (newCardId == null){
            return;
        }

        if (!CreateUpdateQuestionsDatabase(newCardId)){
            return;
        }

        StudyingCard studyingCard1 = CreateStudyingCardObject(newCardId, txtCardName, txtCardDescription, txtCardCategory,
                checkPublicCard, questionQuantity, userId, questionQuantity);

        if (!CreateStudyingCardDatabase(userId, studyingCard1, newCardId)){
            return;
        }

        int resultCode = RESULT_OK;
        setResult(resultCode);

        MessageCardCreated();
        CloseActivity();
    }

    @JavascriptInterface
    public void FinishSaveEditCard(String cardId, String txtCardName, String txtCardDescription,
                                  String txtCardCategory, boolean checkPublicCard){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        int questionQuantity = questionsList.size();

        Card card1 = CreateCardObject(txtCardName, txtCardDescription, txtCardCategory,
                checkPublicCard, questionQuantity, userId);

        if (!EditCardDatabase(cardId, card1)){
            return;
        }

        if (!CreateUpdateQuestionsDatabase(cardId)){
            return;
        }

        if (!DeleteQuestionDatabase(cardId)){
            return;
        }

        StudyingCard studyingCard1 = CreateStudyingCardObject(cardId, txtCardName, txtCardDescription, txtCardCategory,
                checkPublicCard, questionQuantity, userId, questionQuantity);

        if (studyingCard1 == null){
            return;
        }

        if (!CreateStudyingCardDatabase(userId, studyingCard1, cardId)){
            return;
        }

        int resultCode = RESULT_OK;
        setResult(resultCode);

        MessageCardEdited();
        CloseActivity();
    }

    @JavascriptInterface
    public void CloseActivity(){
        finish();
    }

    // endregion

    // region Getting Information
    @JavascriptInterface
    public boolean isQuestionListEmpty(){

        if (questionsList.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    @JavascriptInterface
    public String GetEditingCardData(){

        String cardJson = null;
        String cardId = getCardId();

        if (cardId != null){
            Card card = GetCardDatabase(cardId);

            if (card != null){
                cardJson = new Gson().toJson(card);
            }
            else{
                MessageErrorGettingCard();
                CloseActivity();
            }
        }

        return cardJson;
    }

    @JavascriptInterface
    public String GetQuestionsList(){

        String questionsSummaryListJson = new Gson().toJson(questionsList);
        return questionsSummaryListJson;

    }

    @JavascriptInterface
    public String getCardId(){

        String cardId = getIntent().getStringExtra("cardId");

        return cardId;

    }

    // endregion

    // region Setting Information
    private void SetEditingCardQuestions(){

        String cardId = getCardId();

        if (cardId != null){
            QuestionModel questionModel = new QuestionModel(this);
            Map<String, Object> questionsSummaryList = questionModel.GetQuestionsSummaryList(cardId);

            for (Map.Entry<String, Object> entry : questionsSummaryList.entrySet()) {

                Question question = new Question();
                question.setId(entry.getKey());
                question.setQuestion(entry.getValue().toString());
                question.setNewQuestion(false);

                questionsList.add(question);
            }

        }

    }

    @JavascriptInterface
    public void DeleteQuestion(int questionNumber){
        Question question = questionsList.get(questionNumber - 1);

        if (!question.isNewQuestion()){
            Question deleteQuestion = GetQuestionDatabase(getCardId(), question.getId());
            deleteQuestionsList.add(new Question(deleteQuestion));
        }

        questionsList.remove(questionNumber - 1);
        UpdateQuestionsSummary();
    }

    private void UpdateQuestionsSummary(){
        Gson gson = new Gson();
        String questionsListJson = gson.toJson(questionsList);
        String url = "javascript:updateQuestions('" + questionsListJson + "')";

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    // endregion

    // region Creating Objects

    private Question CreateQuestionObject(String question, String answer, String questionImage,
                                          Uri imageUri, boolean newQuestion, boolean updateQuestion){

        Question question1 = new Question();
        question1.setQuestion(question);
        question1.setAnswer(answer);
        question1.setQuestionImage(questionImage);
        question1.setNewImageUri(imageUri);
        question1.setNewQuestion(newQuestion);
        question1.setUpdateQuestion(updateQuestion);

        return question1;
    }

    private Card CreateCardObject(String txtCardName, String txtCardDescription, String txtCardCategory,
                                  boolean checkPublicCard, int questionQuantity, String userId){

        Card card1 = new Card();
        card1.setName(txtCardName);
        card1.setDescription(txtCardDescription);
        card1.setCategory(txtCardCategory);
        card1.setPublicCard(checkPublicCard);
        card1.setQuestionQuantity(questionQuantity);
        card1.setUserId(userId);

        return card1;

    }

    private StudyingCard CreateStudyingCardObject(String cardId, String txtCardName, String txtCardDescription, String txtCardCategory,
                                                  boolean checkPublicCard, int questionQuantity, String userId, int targetQuestions){

        List<StudyingQuestion> studyingQuestionsList = CreateStudyingQuestionsList(cardId);

        if (studyingQuestionsList.size() == 0){
            MessageErrorAddingCard();
            return null;
        }

        StudyingCard card1 = new StudyingCard();
        card1.setName(txtCardName);
        card1.setDescription(txtCardDescription);
        card1.setCategory(txtCardCategory);
        card1.setPublicCard(checkPublicCard);
        card1.setQuestionQuantity(questionQuantity);
        card1.setUserId(userId);
        card1.setTargetQuestions(targetQuestions);
        card1.setOwnCard(true);
        card1.setStudyingQuestionsList(studyingQuestionsList);

        return card1;
    }

    private List<StudyingQuestion> CreateStudyingQuestionsList(String cardId){

        List<StudyingQuestion> studyingQuestionsList = new ArrayList<>();

        QuestionModel questionModel = new QuestionModel(this);
        Map<String, Object> questionSummaryList = questionModel.GetQuestionsSummaryList(cardId);

        for (Map.Entry question : questionSummaryList.entrySet()){
            StudyingQuestion studyingQuestion = new StudyingQuestion();
            studyingQuestion.setId(question.getKey().toString());
            studyingQuestion.setTargetOrder(1);

            studyingQuestionsList.add(studyingQuestion);
        }

        return studyingQuestionsList;
    }

    // endregion

    // region Database
    private String CreateCardDatabase(Card card){

        CardModel cardModel1 = new CardModel();
        String cardId = cardModel1.CreateCard(card);

        if (cardId == null){
            MessageErrorCreatingCard();
            return null;
        }
        else{
            return cardId;
        }
    }

    private boolean EditCardDatabase(String cardId, Card card){

        CardModel cardModel1 = new CardModel();

        if (!cardModel1.UpdateCard(cardId, card)){
            MessageErrorEditingCard();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean CreateUpdateQuestionsDatabase(String cardId){

        QuestionModel questionModel1 = new QuestionModel(this);

        if (!questionModel1.CreateUpdateQuestions(questionsList, cardId, new QuestionImageStorageModel(this))){
            MessageErrorCreatingQuestions();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean DeleteQuestionDatabase(String cardId){

        QuestionModel questionModel = new QuestionModel(this);

        if (deleteQuestionsList.size() > 0){
            if (!questionModel.DeleteQuestions(deleteQuestionsList, cardId, new QuestionImageStorageModel(this))){

                MessageErrorDeletingQuestions();
                return false;
            }
        }

        return true;
    }

    private boolean CreateStudyingCardDatabase(String userId, StudyingCard card, String cardId){

        StudyingCardModel studyingCardModel1 = new StudyingCardModel();

        if (!studyingCardModel1.CreateCard(userId, card, cardId)){
            MessageErrorAddingCard();
            return false;
        }
        else{
            return true;
        }

    }

    private Card GetCardDatabase(String cardId){

        CardModel cardModel = new CardModel();
        Card card = cardModel.GetCard(cardId);

        return card;
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

    // endregion

    // region Messages
    @JavascriptInterface
    public void MessageQuestionListEmpty(){
        CharSequence message = "Você precisa adicionar ao menos uma pergunta ao seu baralho.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorCreatingCard(){
        CharSequence message = "Ocorreu um erro ao tentar criar o baralho.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorCreatingQuestions(){
        CharSequence message = "Ocorreu um erro ao tentar criar as perguntas do baralho.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorDeletingQuestions(){
        CharSequence message = "Ocorreu um erro ao tentar excluir as perguntas selecionadas.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorEditingCard(){
        CharSequence message = "Ocorreu um erro ao tentar editar o baralho.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorAddingCard(){
        CharSequence message = "Ocorreu um erro ao tentar adicionar o baralho ao seus baralhos.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorGettingCard(){
        CharSequence message = "Ocorreu um erro ao tentar abrir o baralho para edição.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageErrorGettingQuestion(){
        CharSequence message = "Ocorreu um erro ao tentar abrir o pergunta para edição.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageCardCreated(){
        CharSequence message = "Baralho criado com sucesso.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageCardEdited(){
        CharSequence message = "Baralho editado com sucesso.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // endregion








}