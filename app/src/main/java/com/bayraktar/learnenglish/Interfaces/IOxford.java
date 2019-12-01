package com.bayraktar.learnenglish.Interfaces;

import com.bayraktar.learnenglish.Models.Oxford.OxfordModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IOxford {
    @Headers(
            {
                    "Accept: application/json",
                    "app_id: 53ff2810",
                    "app_key: 64b72b5f5576bf237440d97e77bb1cdb"
            })
    @GET("en-gb/{word_id}")
    Call<OxfordModel> getOxford(@Path("word_id") String word_id);
}