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
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewQuestionActivity extends AppCompatActivity {

    WebView webView1;
    Uri imageUri = null;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    Intent intent1 = result.getData();

                    if (intent1 != null){
                        imageUri = result.getData().getData();
                        String imagePath = imageUri.getPath();
                        String url = "javascript:updateQuestionImageName('" + imagePath + "')";
                        webView1.loadUrl(url);

                        getContentResolver().takePersistableUriPermission(imageUri, intent1.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        getSupportActionBar().hide();

        webView1 = (WebView) findViewById(R.id.webViewNewQuestion);
        webView1.addJavascriptInterface(this, "NewQuestionActivity" );
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.loadUrl("file:///android_asset/new_question.html");

        webView1.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                loadEditQuestionData();
            }
        });

    }

    @JavascriptInterface
    public void FinishSetResult(String question, String answer){

        String questionId = this.getIntent().getStringExtra("questionId");
        int questionNumber = this.getIntent().getIntExtra("questionNumber", 0);
        String questionImage = this.getIntent().getStringExtra("questionImage");

        int resultCode = RESULT_OK;
        Intent intentResult = new Intent();
        intentResult.putExtra("question", question);
        intentResult.putExtra("answer", answer);

        if(questionId != null){
            intentResult.putExtra("questionId", questionId);
        }

        if (questionNumber != 0){
            intentResult.putExtra("questionNumber", questionNumber);
        }

        if(questionImage != null){
            intentResult.putExtra("questionImage", questionImage);
        }

        intentResult.setData(imageUri);

        setResult(resultCode, intentResult);

        finish();
    }

    @JavascriptInterface
    public void CloseActivity(){
        finish();
    }

    @JavascriptInterface
    public void OpenFile() {

        imageUri = null;

        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent1.setType("image/*");

        startActivityIntent.launch(intent1);

    }

    @JavascriptInterface
    public String getQuestionId(){

        String questiondId = getIntent().getStringExtra("questionId");

        return questiondId;
    }

    @JavascriptInterface
    public String getCardId(){

        String cardId = getIntent().getStringExtra("cardId");

        return cardId;
    }

    @JavascriptInterface
    public boolean GetEditQuestion(){

        boolean editQuestion = getIntent().getBooleanExtra("editQuestion", false);

        return editQuestion;
    }

    private void loadEditQuestionData(){

        if (GetEditQuestion()){

            imageUri = this.getIntent().getData();

            int questionNumber = this.getIntent().getIntExtra("questionNumber", 0);
            String question = this.getIntent().getStringExtra("question");
            String answer = this.getIntent().getStringExtra("answer");

            Map<String, String> editQuestionData = new HashMap<>();
            editQuestionData.put("question", question);
            editQuestionData.put("answer", answer);

            String editQuestionDataJson = new Gson().toJson(editQuestionData);
            webView1.loadUrl("javascript:loadFields('" + editQuestionDataJson + "')");

            String questionImage = this.getIntent().getStringExtra("questionImage");

            if (questionImage != null){
                String imageExtension = questionImage.substring(questionImage.lastIndexOf("."));
                String imageName = "imagem_pergunta_" + questionNumber + imageExtension;

                webView1.loadUrl("javascript:updateQuestionImageName('" + imageName + "')");
            }

        }

    }


}















