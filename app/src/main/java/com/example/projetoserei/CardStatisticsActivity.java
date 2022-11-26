package com.example.projetoserei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;

import com.google.gson.Gson;

public class CardStatisticsActivity extends AppCompatActivity {

    WebView webViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_statistics);

        getSupportActionBar().hide();

        webViewContent = (WebView) findViewById(R.id.webViewCardStatistics);
        webViewContent.addJavascriptInterface(this, "CardStatisticsActivity" );
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.loadUrl("file:///android_asset/card_statistics.html");



    }

    @JavascriptInterface
    public String GetStudyingCardJson(){

        String studyingCardJson = this.getIntent().getStringExtra("studyingCardJson");
        return studyingCardJson;
    }




}