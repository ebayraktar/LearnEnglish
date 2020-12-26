package com.bayraktar.learnenglish.ui.random;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Clients.RandomWordsClient;
import com.bayraktar.learnenglish.Interfaces.IRandomWords;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomViewModel extends ViewModel {
    MutableLiveData<String[]> wordsLiveData;

    public RandomViewModel() {
        wordsLiveData = new MutableLiveData<>();
    }

    public LiveData<String[]> getRandomWords(final int wordCount) {
        RandomWordsClient.getClient().create(IRandomWords.class).getRandomWords(wordCount).enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                wordsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
                wordsLiveData.setValue(null);
            }
        });
        return wordsLiveData;
    }
}
