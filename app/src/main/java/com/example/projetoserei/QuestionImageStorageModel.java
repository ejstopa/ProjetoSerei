package com.example.projetoserei;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class QuestionImageStorageModel {

    Context context1;
    private String folderQuestionImageStorage = Constants.folderQuestionImageStorage;

    public QuestionImageStorageModel(Context c) {
        context1 = c;
    }

    public String StoreImage(String cardId, String questionId, Uri imageUri){

        String imageName = CreateImageStorageName(cardId, questionId, imageUri);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(folderQuestionImageStorage);
        StorageReference imageRef = storageRef.child(folderQuestionImageStorage + "/" + imageName);

        try {
            UploadTask task1 = imageRef.putFile(imageUri);

            do {
            }
            while(!task1.isComplete());

            if (task1.isSuccessful()){
                return imageName;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }

    }

    public boolean DeleteImage(String imageName){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(folderQuestionImageStorage);
        StorageReference imageRef = storageRef.child(folderQuestionImageStorage + "/" + imageName);

        try {
            Task task1 = imageRef.delete();

            do {
            }
            while(!task1.isComplete());

            if (task1.isSuccessful()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public byte[] GetImageAsBytes(String imageName){

        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(folderQuestionImageStorage);
        StorageReference imageRef = storageRef.child(folderQuestionImageStorage + "/" + imageName);

        Task task1 = imageRef.getBytes(ONE_MEGABYTE);

        do {
        }while (!task1.isComplete());

        if (task1.isSuccessful()){
            byte[] imageBytes = (byte[])task1.getResult();
            return imageBytes;
        }
        else{
            return null;
        }

    }

    private String CreateImageStorageName(String cardId, String questionId, Uri imageUri){

        String imageExtension = getImageExtension(imageUri);
        String result = cardId + "-" + questionId + "." + imageExtension;
        return result;

    }

    private String getImageExtension(Uri imageUri){
        final MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = mime.getExtensionFromMimeType(context1.getContentResolver().getType(imageUri));

        return extension;
    }
}
