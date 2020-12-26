package com.bayraktar.learnenglish.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bayraktar.learnenglish.Clients.YandexClient;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YandexHelper extends LiveData<YandexModel> {

    private static final String YANDEX_API_KEY = "trnsl.1.1.20191123T191655Z.c4e490008f275f7b.1e4729b73e064c7b91ff8beaabd17e713d0dfb73";
    final IYandex yandex;

    MutableLiveData<YandexModel> data;

    public YandexHelper() {
        yandex = YandexClient.getClient().create(IYandex.class);
        data = new MutableLiveData<>();
    }

    public LiveData<YandexModel> translate(String lang, String[] text) {
        yandex.getYandex(YANDEX_API_KEY, text, lang).enqueue(new Callback<YandexModel>() {
            @Override
            public void onResponse(@NonNull Call<YandexModel> call, @NonNull Response<YandexModel> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YandexModel> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
