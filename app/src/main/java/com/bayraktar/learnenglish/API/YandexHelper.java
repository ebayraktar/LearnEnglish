package com.bayraktar.learnenglish.API;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bayraktar.learnenglish.Clients.YandexClient;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YandexHelper {

    private Context context;
    private IYandex yandexListener;

    public YandexHelper(Context context, IYandex yandexListener) {
        this.context = context;
        this.yandexListener = yandexListener;
    }

    public void initalize() {
    }

    public void translate(String[] text) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Yandex.Translate bağlanılıyor...");
        dialog.show();

        final IYandex yandex = YandexClient.getClient().create(IYandex.class);
        String YANDEX_API_KEY = "trnsl.1.1.20191123T191655Z.c4e490008f275f7b.1e4729b73e064c7b91ff8beaabd17e713d0dfb73";
        Call<YandexModel> callYandex = yandex.getYandex(YANDEX_API_KEY, text, "en-tr");
        callYandex.enqueue(new Callback<YandexModel>() {
            @Override
            public void onResponse(@NonNull Call<YandexModel> call, @NonNull Response<YandexModel> response) {
                if (response.isSuccessful()) {
                    YandexModel yandexModel = response.body();
                    if (yandexModel == null || yandexModel.text == null) {
                        yandexListener.onErrorOccurred("Response null");
                        return;
                    }
                    List<String> translationList = new ArrayList<>(yandexModel.text);
                    yandexListener.onTranslatedValue(translationList);
                } else {
                    yandexListener.onErrorOccurred("Translation request failed");
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<YandexModel> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                yandexListener.onErrorOccurred("TRANSLATE: " + t.getMessage());
            }
        });
    }
}
