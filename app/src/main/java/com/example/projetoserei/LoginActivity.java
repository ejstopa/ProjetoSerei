package com.example.projetoserei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        WebView webView = (WebView) findViewById(R.id.webViewLogin);
        webView.addJavascriptInterface(new AccountModel(this), "AccountModel" );
        webView.addJavascriptInterface(this, "LoginActivity" );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/login.html");

    }

    @JavascriptInterface
    public void ShowMainActivity(){
        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @JavascriptInterface
    public void ShowNewUserActivity(){
        Intent intent1 = new Intent(LoginActivity.this, NewUserActivity.class);
        startActivity(intent1);
        finish();
    }

    @JavascriptInterface
    public void MessageLoginOk() {
        CharSequence message = "Login realizado com sucesso";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void MessageLoginFailed() {
        CharSequence message = "Login inválido";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}