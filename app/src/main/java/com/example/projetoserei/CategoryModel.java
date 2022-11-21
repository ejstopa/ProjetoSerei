package com.example.projetoserei;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CategoryModel {

    Context context1;
    String collectionCategories = Constants.collectionCategories;
    String documentCategoriesList = Constants.documentCategoriesList;
    String arrayCategories = Constants.arrayCategories;

    public CategoryModel(Context c) {

        context1 = c;

    }

    @JavascriptInterface
    public String GetAllCategoryNames(){

        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> task1 = dataBase.collection(collectionCategories).document(documentCategoriesList).get();

        do {
        }
        while(!task1.isComplete());

        List<String> resultList = new ArrayList<String>();

        if (task1.isSuccessful()){

            DocumentSnapshot DocumentSnapshot = task1.getResult();
            resultList  =  (List<String>)DocumentSnapshot.get(arrayCategories);

            if (resultList != null){
                resultList.removeAll(Collections.singleton(null));
                Collections.sort(resultList);
                JSONArray result = new JSONArray(resultList);

                return result.toString();
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }



}
