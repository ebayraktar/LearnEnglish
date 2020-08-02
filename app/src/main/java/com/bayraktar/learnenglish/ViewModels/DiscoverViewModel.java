package com.bayraktar.learnenglish.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static com.bayraktar.learnenglish.App.firebaseDatabase;

public class DiscoverViewModel extends ViewModel {

    private MutableLiveData<MobileResult> resultMutableLiveData;
    private MobileResult result;
    DatabaseReference wordsRef = firebaseDatabase.getReference("words");

    public DiscoverViewModel() {
        resultMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<MobileResult> getMutableLiveData() {
        result = new MobileResult();
        return resultMutableLiveData;
    }

    public LiveData<MobileResult> getNewResult() {
        result = new MobileResult();
        wordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long size = snapshot.getChildrenCount();
                long randomIndex = Math.abs(new Random().nextInt()) % size;
                wordsRef.orderByChild("index").startAt(randomIndex).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Word word = data.getValue(Word.class);
                            if (word != null) {
                                word.setWordId(data.getKey());
                                result.setCode(0);
                                result.setMessage("Success");
                                result.setResult(word);
                                resultMutableLiveData.setValue(result);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        result.setCode(error.getCode());
                        result.setMessage(error.getMessage());
                        result.setResult(null);
                        resultMutableLiveData.setValue(result);
                    }
                });
//                        .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot data : snapshot.getChildren()) {
//                            Word word = data.getValue(Word.class);
//                            if (word != null) {
//                                word.setWordId(data.getKey());
//                                result.setCode(0);
//                                result.setMessage("Success");
//                                result.setResult(word);
//                                resultMutableLiveData.setValue(result);
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        result.setCode(error.getCode());
//                        result.setMessage(error.getMessage());
//                        result.setResult(null);
//                        resultMutableLiveData.setValue(result);
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return resultMutableLiveData;
    }
}
