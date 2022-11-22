package com.example.projetoserei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

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
}