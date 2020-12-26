package com.bayraktar.learnenglish.Interfaces;

import com.bayraktar.learnenglish.Models.Yandex.YandexModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IYandex {
    @POST("/api/v1.5/tr.json/translate")
    @FormUrlEncoded
    Call<YandexModel> getYandex(@Field("key") String key,
                                @Field("text") String[] text,
                                @Field("lang") String lang);
}