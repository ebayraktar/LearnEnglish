package com.bayraktar.learnenglish.Views;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.API.FirebaseService;
import com.bayraktar.learnenglish.Adapter.SlideCardAdapter;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.List;

public class SlideWordsActivity extends BaseActivity implements ValueEventListener, SwipeCardsView.CardsSlideListener {

    private static final int LIMIT = 5;

    private SwipeCardsView swipeCardsView;
    private SlideCardAdapter slideCardAdapter;
    FirebaseService firebaseService;
    List<Word> wordList;
    int index = 0;
    int counter = 0;
    PrefManager prefManager;
    int language = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_words);

        swipeCardsView = findViewById(R.id.swipeCardsView);
        swipeCardsView.enableSwipe(true);
        prefManager = new PrefManager(SlideWordsActivity.this);
        language = prefManager.getLanguage();

        initialize();
    }

    void initialize() {
        wordList = new ArrayList<>();
        firebaseService = new FirebaseService();
        swipeCardsView.setCardsSlideListener(this);
        slideCardAdapter = new SlideCardAdapter(wordList, language);
        swipeCardsView.setAdapter(slideCardAdapter);
        getWord();
    }

    void getWord() {
        counter = 0;
        firebaseService.getWord(index, LIMIT + 1, this);
        index += LIMIT;
    }


    //FIREBASE
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
            List<Word> words = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                if (dataSnapshot.exists()) {
                    words.add(dataSnapshot.getValue(Word.class));
                }
            }
            wordList = words;
            slideCardAdapter.setModelList(wordList);
            swipeCardsView.setAdapter(slideCardAdapter);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        if (slideCardAdapter == null)
            slideCardAdapter = new SlideCardAdapter(null, language);
        else
            slideCardAdapter.setModelList(null);
    }

    //CARD_SLIDE
    @Override
    public void onShow(int index) {

    }

    @Override
    public void onCardVanish(int index, SwipeCardsView.SlideType type) {
        switch (type) {
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                break;
        }
        counter++;
        if (counter >= LIMIT) {
            getWord();
        }
    }

    @Override
    public void onItemClick(View cardImageView, int index) {
    }
}
