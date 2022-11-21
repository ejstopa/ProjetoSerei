package com.example.projetoserei;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    Context context1;
    private String collectionUsers = Constants.collectionUsers;
    private String fieldUserName = Constants.fieldUserName;
    private String fieldUserEmail = Constants.fieldUserEmail;


    public UserModel(Context c) {
        context1 = c;
    }

    @JavascriptInterface
    public String CreateUser(String id, String name, String email){
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put(fieldUserName,name);
        user.put(fieldUserEmail, email);

        Task task1 = dataBase.collection(collectionUsers).document(id).set(user);

        do {
        }
        while(!task1.isComplete());

        JSONObject result = new JSONObject();

        if (task1.isSuccessful()){
            try {
                result.put("success", true);

            } catch (JSONException e) {
                return null;
            }
            return result.toString();
        }
        else{
            try {
                String errorMessage = ((FirebaseAuthException)task1.getException()).getErrorCode();

                result.put("success", false);
                result.put("error_message", errorMessage );

            } catch (JSONException e) {
                return null;
            }

            return result.toString();
        }

    }

}
