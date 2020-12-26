package com.bayraktar.learnenglish.ui.yandex;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bayraktar.learnenglish.Clients.YandexClient;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YandexViewModel extends ViewModel {
    private static final String YANDEX_API_KEY = "trnsl.1.1.20191123T191655Z.c4e490008f275f7b.1e4729b73e064c7b91ff8beaabd17e713d0dfb73";
    MutableLiveData<YandexModel> wordsLiveData;

    public YandexViewModel() {
        wordsLiveData = new MutableLiveData<>();
    }

    public LiveData<YandexModel> translate(String word, String language) {
        YandexClient.getClient().create(IYandex.class).getYandex(YANDEX_API_KEY, new String[]{word}, language)
                .enqueue(new Callback<YandexModel>() {
                    @Override
                    public void onResponse(@NonNull Call<YandexModel> call, @NonNull Response<YandexModel> response) {
                        wordsLiveData.setValue(response.body());
                    }
                    @Override
                    public void onFailure(@NonNull Call<YandexModel> call, @NonNull Throwable t) {
                        wordsLiveData.setValue(null);
                    }
                });
        return wordsLiveData;
    }
}