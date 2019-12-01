package com.bayraktar.learnenglish.Clients;

import com.bayraktar.learnenglish.Interfaces.IRandomWords;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomWordsClient {

    private static RandomWordsClient instance;
    public static final String BASE_URL = "https://random-word-api.herokuapp.com/";

    private IRandomWords randomWords;

    public static RandomWordsClient getInstance() {
        if (instance == null) {
            instance = new RandomWordsClient();
        }

        return instance;
    }

    private RandomWordsClient() {
        buildRetrofit(BASE_URL);
    }

    private void buildRetrofit(String url) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        randomWords = retrofit.create(IRandomWords.class);
    }

    public IRandomWords getRandomWordsService() {
        return randomWords;
    }


//
//    public static RandomWordsClient instance = null;
//    private static Retrofit retrofit = null;

//
//    private IRandomWords randomWords;
//
//    public static Retrofit getClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(Base_Url)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(new OkHttpClient())
//                    .build();
//            return retrofit;
//        }
//        return retrofit;
//    }
}