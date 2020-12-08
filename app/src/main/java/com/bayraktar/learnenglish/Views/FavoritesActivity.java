package com.bayraktar.learnenglish.Views;

import android.net.DnsResolver;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bayraktar.learnenglish.API.FirebaseService;
import com.bayraktar.learnenglish.Adapter.FavoriteWordAdapter;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.bayraktar.learnenglish.R;
import com.bayraktar.learnenglish.ViewModels.FavoritesViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoritesActivity extends BaseActivity implements FavoriteWordAdapter.IFavoriteWordListener {

    FavoriteWordAdapter favoriteWordAdapter;
    FavoritesViewModel favoritesViewModel;
    PrefManager prefManager;
    RecyclerView rvFavorites;
    int languagesCurrentIndex;
    LottieAnimationView av_splash_animation;
    private TextToSpeech myTTS;
    List<Word> wordList;
    FirebaseService firebaseService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        HeaderEvents(this);
        SetBackButton(this);
        prefManager = new PrefManager(this);
        languagesCurrentIndex = prefManager.getLanguage();

        av_splash_animation = findViewById(R.id.av_splash_animation);
        rvFavorites = findViewById(R.id.rvFavorites);

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        favoritesViewModel.getWords(getDeviceID()).observe(this, new Observer<MobileResult>() {
            @Override
            public void onChanged(MobileResult mobileResult) {
                if (mobileResult != null && mobileResult.getCode() == 0 && mobileResult.getResult() != null) {
                    Type listType = new TypeToken<ArrayList<Word>>() {
                    }.getType();
                    List<Word> words = new Gson().fromJson(mobileResult.getResult().toString(), listType);
                    setWord(words);
                }
            }
        });
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }

    void initialize() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (myTTS.getEngines().size() == 0) {
                    Toast.makeText(FavoritesActivity.this, "No Engines Installed", Toast.LENGTH_LONG).show();
                } else {
                    if (status == TextToSpeech.SUCCESS) {
                        ttsInitialized();
                    }
                }
            }
        });

        firebaseService = new FirebaseService();
        favoriteWordAdapter = new FavoriteWordAdapter(languagesCurrentIndex, myTTS, this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        rvFavorites.setAdapter(favoriteWordAdapter);
    }

    void setWord(List<Word> words) {
        wordList = words;
        favoriteWordAdapter.setWordList(words);
        hideLoading();
    }


    void ttsInitialized() {
        // set Language
        if (languagesCurrentIndex == 1) {
            myTTS.setLanguage(new Locale("tr", "TR"));
        } else if (languagesCurrentIndex == 2) {
            myTTS.setLanguage(new Locale("es", "ES"));
        } else {
            myTTS.setLanguage(Locale.US);
        }
    }

    void setLoading() {
        rvFavorites.animate().alpha(0.0f);
        av_splash_animation.animate().alpha(1.0f);
    }

    void hideLoading() {
        rvFavorites.animate().alpha(1.0f);
        av_splash_animation.animate().alpha(0.0f);
    }

    void sendUserWordInformation(final int position, final UserWordInformation wordInformation) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            firebaseService.setUserWordInfo(getDeviceID(), wordInformation, new DnsResolver.Callback<MobileResult>() {
                @Override
                public void onAnswer(@NonNull MobileResult answer, int rcode) {
                    if (answer.getCode() == 0) {

                        favoriteWordAdapter.notifyItemChanged(position);
                    }
                }

                @Override
                public void onError(@NonNull DnsResolver.DnsException error) {

                }
            });
        }
    }

    @Override
    public void onFavClick(int position) {
        Word currentWord = wordList.get(position);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String json = (String) currentWord.getAdditionalProperties().getOrDefault("userWordInformation", "");
            UserWordInformation userWordInformation = new Gson().fromJson(json, UserWordInformation.class);
            userWordInformation.setFav(!userWordInformation.isFav());
            currentWord.setAdditionalProperty("userWordInformation", new Gson().toJson(userWordInformation));
            sendUserWordInformation(position, userWordInformation);
        }
    }

    @Override
    public void onApproveClick(int position) {
        Word currentWord = wordList.get(position);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String json = (String) currentWord.getAdditionalProperties().getOrDefault("userWordInformation", "");
            UserWordInformation userWordInformation = new Gson().fromJson(json, UserWordInformation.class);
            int approved = userWordInformation.getApproved();
            approved++;
            approved %= 3;
            userWordInformation.setApproved(approved);
            currentWord.setAdditionalProperty("userWordInformation", new Gson().toJson(userWordInformation));
            sendUserWordInformation(position, userWordInformation);
        }
    }

    @Override
    public void onDefinitionsClick(int position) {

    }

    @Override
    public void onExamplesClick(int position) {

    }
}
