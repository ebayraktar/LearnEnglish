package com.bayraktar.learnenglish.API;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bayraktar.learnenglish.Clients.RandomWordsClient;
import com.bayraktar.learnenglish.Interfaces.IRandomWords;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomWordsHelper {

    MutableLiveData<String[]> wordsLiveData;

    private Context context;
    private IRandomWords randomWords;

    public RandomWordsHelper(Context context) {
        this.context = context;
        wordsLiveData = new MutableLiveData<>();
        initialize();
    }

    void initialize() {
        randomWords = RandomWordsClient.getClient().create(IRandomWords.class);
    }

    public LiveData<String[]> getRandomWords(final int wordCount) {
        randomWords.getRandomWords(wordCount).enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                if (response.body() != null) {
                    wordsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
                wordsLiveData.setValue(null);
            }
        });
        return wordsLiveData;
    }
}
