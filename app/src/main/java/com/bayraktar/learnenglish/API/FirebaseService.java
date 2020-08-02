package com.bayraktar.learnenglish.API;

import android.net.DnsResolver;
import android.os.Build;

import androidx.annotation.NonNull;

import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import static com.bayraktar.learnenglish.App.firebaseDatabase;

public class FirebaseService {
    DatabaseReference usersRef = firebaseDatabase.getReference("users");
    MobileResult result;

    public FirebaseService() {
    }

    public void setUserWordInfo(String userID, final UserWordInformation userWordInformation, final DnsResolver.Callback<MobileResult> resultCallback) {
        result = new MobileResult();
//        if (firebaseUser == null) {
//            result.setCode(-1);
//            result.setMessage("Authentication failure");
//            result.setResult(userWordInformation);
//            resultMutableLiveData.setValue(result);
//            return;
//        }

        usersRef.child(userID).child(userWordInformation.getWordID()).setValue(userWordInformation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            result.setCode(0);
                            result.setMessage("Successful");
                            result.setResult(userWordInformation);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                resultCallback.onAnswer(result, 0);
                            }
                        } else if (task.isCanceled()) {
                            result.setCode(-2);
                            result.setMessage("Canceled");
                            result.setResult(userWordInformation);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                resultCallback.onAnswer(result, -2);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.setCode(-3);
                        result.setMessage(e.getMessage());
                        result.setResult(userWordInformation);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            resultCallback.onAnswer(result, -3);
                        }
                    }
                });
    }

}