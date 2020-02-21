package com.bayraktar.learnenglish.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.API.FirebaseHelper;
import com.bayraktar.learnenglish.API.YandexHelper;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Interfaces.IFirebase;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;
import com.bayraktar.learnenglish.R;

import java.util.List;

import retrofit2.Call;

public class LearnActivity extends BaseActivity implements IFirebase, IYandex {
    FirebaseHelper firebaseHelper;
    YandexHelper yandexHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        firebaseHelper = new FirebaseHelper(this, this);
        firebaseHelper.initialize();

        yandexHelper = new YandexHelper(this, this);
        yandexHelper.initalize();

//        getData();
//        searchSpeech();
//        searchWord();
        findViewById(R.id.cvNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }

    private void setData() {
        Word word = new Word("TEST", "TEST", "TEST", "v");
        firebaseHelper.setWord(word);
    }

    private void getData() {
        firebaseHelper.getWordList();
    }

    private void searchWord() {
        firebaseHelper.searchWord("word");
    }

    private void searchSpeech() {
        firebaseHelper.searchSpeech("v");
    }

    @Override
    public void onGetWordList(List<Word> wordList) {
        for (Word word : wordList) {
            Log.d("TAG", "SetData: TR => " + word.getTr());
        }
    }

    @Override
    public void onSetWord(boolean isSuccess) {
    }

    @Override
    public Call<YandexModel> getYandex(String key, String[] text, String lang) {
        return null;
    }

    @Override
    public void onTranslatedValue(List<String> value) {

    }

    @Override
    public void onErrorOccurred(String value) {

    }
}
