package com.bayraktar.learnenglish.API;

import android.net.DnsResolver;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.bayraktar.learnenglish.App.firebaseDatabase;

public class FirebaseService {
    DatabaseReference usersRef = firebaseDatabase.getReference("users");
    DatabaseReference wordsRef = firebaseDatabase.getReference("words");

    public FirebaseService() {
    }

    public void getWord(int index, int limit, ValueEventListener listener) {
        wordsRef.orderByChild("index").startAt(index).limitToFirst(limit).addListenerForSingleValueEvent(listener);
    }

    public void setUserWordInfo(String userID, final UserWordInformation userWordInformation, OnCompleteListener<Void> listener) {
        if (userWordInformation == null || TextUtils.isEmpty(userWordInformation.getWordID())) {
            return;
        }
        usersRef.child(userID).child(userWordInformation.getWordID()).setValue(userWordInformation)
                .addOnCompleteListener(listener);
    }

}