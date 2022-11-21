package com.example.projetoserei;


import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


import org.json.JSONException;
import org.json.JSONObject;

public class AccountModel {
    private FirebaseAuth auth;
    Context context1;

    public AccountModel(Context c){
       context1 = c;

    }

    @JavascriptInterface
    public boolean Login(String user, String password){

        boolean login;
        auth = FirebaseAuth.getInstance();

        Task task1 = auth.signInWithEmailAndPassword(user, password);

        do {
        }
        while(!task1.isComplete());

        FirebaseUser currentUser1 = auth.getCurrentUser();

        if (currentUser1 != null){
            login = true;
        }
        else{
            login = false;
        }

        return  login;

    }

    @JavascriptInterface
    public void Logout(){
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    @JavascriptInterface
    public String CreateAccount(String userEmail, String password, String userName){

        boolean created;
        auth = FirebaseAuth.getInstance();

        Task task1 = auth.createUserWithEmailAndPassword(userEmail, password);

        do {
        }
        while(!task1.isComplete());

        FirebaseUser currentUser1 = auth.getCurrentUser();
        JSONObject result = new JSONObject();

        if (currentUser1 != null){
            try {

                if (!updateUserName(currentUser1, userName)){
                    return  null;
                }

                result.put("success", true);
                result.put("user_id", currentUser1.getUid());

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

    @JavascriptInterface
    public String GetUserName(){

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userName = user.getDisplayName();

        return userName;
    }

    private boolean updateUserName(FirebaseUser currentUser, String userName){

        UserProfileChangeRequest changeRequest1 = new UserProfileChangeRequest.Builder().
                setDisplayName(userName).build();

        Task updateProfileTask = currentUser.updateProfile(changeRequest1);

        do {
        }
        while(!updateProfileTask.isComplete());

        if (updateProfileTask.isSuccessful()){
            return true;
        }
        else{
            return  false;
        }
    }

}
