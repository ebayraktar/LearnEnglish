package com.bayraktar.learnenglish.ui.oxford;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Clients.OxfordClient;
import com.bayraktar.learnenglish.Interfaces.IOxford;
import com.bayraktar.learnenglish.Models.Oxford.OxfordModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OxfordViewModel extends ViewModel {
    MutableLiveData<OxfordModel> wordLiveData;

    public OxfordViewModel() {
        wordLiveData = new MutableLiveData<>();
    }

    public LiveData<OxfordModel> getRandomWords(String word) {
        OxfordClient.getClient().create(IOxford.class).getOxford(word)
                .enqueue(new Callback<OxfordModel>() {
                    @Override
                    public void onResponse(@NonNull Call<OxfordModel> call, @NonNull Response<OxfordModel> response) {
                        wordLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<OxfordModel> call, @NonNull Throwable t) {
                        wordLiveData.setValue(null);
                    }
                });
        return wordLiveData;
    }
}