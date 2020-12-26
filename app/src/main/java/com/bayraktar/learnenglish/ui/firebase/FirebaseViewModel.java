package com.bayraktar.learnenglish.ui.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.bayraktar.learnenglish.App.firebaseDatabase;

public class FirebaseViewModel extends ViewModel {
    MutableLiveData<Word> wordLiveData;
    MutableLiveData<List<Word>> wordListLiveData;

    public FirebaseViewModel() {
        wordLiveData = new MutableLiveData<>();
        wordListLiveData = new MutableLiveData<>();
    }

    public LiveData<Word> getWord(int index) {
        firebaseDatabase.getReference("words").orderByChild("index").startAt(index, "index").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Word tempWord = null;
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            tempWord = dataSnapshot.getValue(Word.class);
                            if (tempWord != null) {
                                tempWord.setWordId(dataSnapshot.getKey());
                                break;
                            }
                        }
                    }
                }
                wordLiveData.setValue(tempWord);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                wordLiveData.setValue(null);
            }
        });
        return wordLiveData;
    }

    public LiveData<List<Word>> getWordList(int index, int count) {
        firebaseDatabase.getReference("words").orderByChild("index").startAt(index, "index").limitToFirst(count).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Word> tempWordList = null;
                if (snapshot.exists()) {
                    tempWordList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            Word tempWord = dataSnapshot.getValue(Word.class);
                            if (tempWord != null) {
                                tempWord.setWordId(dataSnapshot.getKey());
                                tempWordList.add(tempWord);
                            }
                        }
                    }
                }
                wordListLiveData.setValue(tempWordList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                wordListLiveData.setValue(null);
            }
        });
        return wordListLiveData;
    }
}