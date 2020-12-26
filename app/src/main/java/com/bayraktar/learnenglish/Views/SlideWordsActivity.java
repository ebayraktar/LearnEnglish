package com.bayraktar.learnenglish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bayraktar.learnenglish.API.FirebaseService;
import com.bayraktar.learnenglish.Adapter.SlideCardAdapter;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Constants;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.Message;
import com.bayraktar.learnenglish.R;
import com.bayraktar.learnenglish.ui.firebase.FirebaseViewModel;
import com.google.gson.Gson;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.List;

public class SlideWordsActivity extends BaseActivity implements SwipeCardsView.CardsSlideListener {

    private static final int LIMIT = 100;

    FirebaseService firebaseService;
    FirebaseViewModel firebaseViewModel;
    ConstraintLayout clContent;

    private SwipeCardsView swipeCardsView;
    private SlideCardAdapter slideCardAdapter;
    LottieAnimationView av_splash_animation;
    PrefManager prefManager;
    int index;
    List<Word> wordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_words);

        clContent = findViewById(R.id.clContent);
        av_splash_animation = findViewById(R.id.av_splash_animation);
        swipeCardsView = findViewById(R.id.swipeCardsView);

        swipeCardsView.enableSwipe(true);
        swipeCardsView.setCardsSlideListener(this);
        prefManager = new PrefManager(SlideWordsActivity.this);
        firebaseService = new FirebaseService();
        index = prefManager.getDiscoveredIndex();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.discover);
        initialize();
    }

    void initialize() {
        if (wordList == null)
            wordList = new ArrayList<>();
        if (slideCardAdapter == null)
            slideCardAdapter = new SlideCardAdapter();
        if (firebaseViewModel != null) {
            hideLoading();
            return;
        }
        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.getWordList(index, LIMIT).observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                wordList.clear();
                hideLoading();
                if (words != null && words.size() > 0) {
                    wordList = words;
                    slideCardAdapter.setModelList(words);
                    swipeCardsView.setAdapter(slideCardAdapter);
                } else {
                    ShowAlertDialog(SlideWordsActivity.this, new Message("Hata oluştu", "Kelime Alınamadı", "TAMAM", "", R.drawable.ic_en), null);
                }
            }
        });
    }

    void setLoading() {
        clContent.animate().alpha(0.0f);
        av_splash_animation.animate().alpha(1.0f);
    }

    void hideLoading() {
        clContent.animate().alpha(1.0f);
        av_splash_animation.animate().alpha(0.0f);
    }

    void getWord() {
        setLoading();
        index += LIMIT;
        firebaseViewModel.getWordList(index, LIMIT);
    }

    //CARD_SLIDE
    @Override
    public void onShow(int index) {
    }

    @Override
    public void onCardVanish(int index, SwipeCardsView.SlideType type) {
        if (wordList != null && index >= 0) {
            Word word = wordList.get(index);
            if (word != null) {
                UserWordInformation wordInformation = new UserWordInformation();
                wordInformation.setWordID(word.getWordId());
                switch (type) {
                    case LEFT:
                        wordInformation.setFav(false);
                        break;
                    case RIGHT:
                        wordInformation.setFav(true);
                        break;
                }
                firebaseService.setUserWordInfo(getDeviceID(), wordInformation, null);
                prefManager.setDiscoveredIndex(word.getIndex());
            }
        }

        if (index >= LIMIT - 1) {
            getWord();
        }
    }

    @Override
    public void onItemClick(View cardImageView, int index) {
        boolean isSuccess = false;
        if (wordList != null && index >= 0) {
            Word word = wordList.get(index);
            if (word != null) {
                Intent discoverIntent = new Intent(SlideWordsActivity.this, DiscoverActivity.class);
                discoverIntent.putExtra(Constants.WORD_KEY, new Gson().toJson(word));
                startActivity(discoverIntent);
                isSuccess = true;
            }
        }
        if (!isSuccess) {
            //TODO: Error msg
            Log.d("TAG", "onItemClick: HATA");
        }
    }
}
