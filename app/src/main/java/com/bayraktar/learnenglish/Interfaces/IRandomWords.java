package com.bayraktar.learnenglish.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRandomWords {
    @GET("word")
    Call<String[]> getRandomWords(@Query("key") String my_api_key, @Query("number") int number_of_words);

    @GET("key")
    Call<String> getKey();
}
