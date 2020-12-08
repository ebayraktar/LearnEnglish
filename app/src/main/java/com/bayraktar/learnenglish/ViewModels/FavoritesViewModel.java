package com.bayraktar.learnenglish.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.bayraktar.learnenglish.App.firebaseDatabase;

public class FavoritesViewModel extends ViewModel {
    private MutableLiveData<MobileResult> resultMutableLiveData;
    private MobileResult result;
    DatabaseReference wordsRef = firebaseDatabase.getReference("words");
    DatabaseReference usersRef = firebaseDatabase.getReference("users");

    public FavoritesViewModel() {
        this.resultMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<MobileResult> getWords(String userID) {
        result = new MobileResult();
        result.setMessage("EMPTY");
        usersRef.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final List<Word> words = new ArrayList<>();
                            for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.exists()) {
                                    final UserWordInformation userWordInformation = dataSnapshot.getValue(UserWordInformation.class);
                                    wordsRef.child(Objects.requireNonNull(dataSnapshot.getKey()))
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Word tempWord = snapshot.getValue(Word.class);
                                                        assert tempWord != null;
                                                        assert userWordInformation != null;
                                                        tempWord.setAdditionalProperty("userWordInformation", new Gson().toJson(userWordInformation));
                                                        words.add(tempWord);
                                                        Gson gson = new Gson();
                                                        String json = gson.toJson(words);
                                                        result.setResult(json);
                                                        result.setCode(0);
                                                        result.setMessage("Success");
                                                        resultMutableLiveData.setValue(result);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    result.setMessage(error.getMessage());
                                                    result.setResult("");
                                                    result.setCode(-4);
                                                    resultMutableLiveData.setValue(result);
                                                }
                                            });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        result.setMessage(error.getMessage());
                        result.setResult("");
                        result.setCode(-3);
                        resultMutableLiveData.setValue(result);
                    }
                });
        return resultMutableLiveData;
    }
}
