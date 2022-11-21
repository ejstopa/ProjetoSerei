package com.example.projetoserei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class NewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        getSupportActionBar().hide();

        WebView webView = (WebView) findViewById(R.id.webViewNewUser);
        webView.addJavascriptInterface(new AccountModel(this), "AccountModel" );
        webView.addJavascriptInterface(new UserModel(this), "UserModel" );
        webView.addJavascriptInterface(this, "NewUserActivity" );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/new_user.html");
    }
    @JavascriptInterface
    public void ShowMainActivity(){
        Intent intent1 = new Intent(NewUserActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @JavascriptInterface
    public void ShowPasswordValidationError(){
        CharSequence message = "As senhas digitadas não conferem.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void MessageAccountCreated(){
        CharSequence message = "Conta criada com sucesso.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void MessageAccountCreationError(){
        CharSequence message = "Ocorreu um erro ao tentar criar a conta.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void ShowMessage(String message){
        CharSequence messageToShow = message;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}