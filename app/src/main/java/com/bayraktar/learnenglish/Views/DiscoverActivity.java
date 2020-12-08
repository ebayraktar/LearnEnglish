package com.bayraktar.learnenglish.Views;

import android.app.AlertDialog;
import android.net.DnsResolver;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bayraktar.learnenglish.API.FirebaseService;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.Models.Firebase.UserWordInformation;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.Models.Message;
import com.bayraktar.learnenglish.Models.MobileResult;
import com.bayraktar.learnenglish.R;
import com.bayraktar.learnenglish.ViewModels.DiscoverViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Locale;

public class DiscoverActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, Observer<MobileResult> {

    private static final String WORD_KEY = "WORD_KEY" + 385;
    private static final String IMAGE_INDEX_KEY = "IMAGE_INDEX_KEY" + 385;
    private static final String DEFINITION_INDEX_KEY = "DEFINITION_INDEX_KEY" + 385;
    private static final String EXAMPLE_INDEX_KEY = "EXAMPLE_INDEX_KEY" + 385;
    private static final String LANGUAGE_INDEX_KEY = "LANGUAGE_INDEX_KEY" + 385;

    LottieAnimationView av_splash_animation;

    ImageView ivWordImage;
    ImageView ivWordIsApproved, ivAddFav;
    ImageView ivWordSound, ivDefinitionSound, ivExampleSound;
    ImageView ivNextExample, ivNextDefinition;
    TextView tvWordText, tvWordDefinition, tvWordExample;
    ConstraintLayout clContent;
    boolean currentFav;
    int isApproved;
    private TextToSpeech myTTS;

    DiscoverViewModel discoverViewModel;
    Word currentWord;
    int examplesCurrentIndex;
    int definitionsCurrentIndex;
    int languagesCurrentIndex;
    int imagesCurrentIndex;
    int tryCount;
    PrefManager prefManager;
    UserWordInformation wordInformation;

    FirebaseService firebaseService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        SetBackButton(this);
        prefManager = new PrefManager(DiscoverActivity.this);
        languagesCurrentIndex = prefManager.getLanguage();
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        firebaseService = new FirebaseService();
        clContent = findViewById(R.id.clContent);
        av_splash_animation = findViewById(R.id.av_splash_animation);

        ivWordImage = findViewById(R.id.ivWordImage);

        ivWordIsApproved = findViewById(R.id.ivWordIsApproved);
        ivAddFav = findViewById(R.id.ivAddFav);

        ivWordSound = findViewById(R.id.ivWordSound);
        ivDefinitionSound = findViewById(R.id.ivDefinitionSound);
        ivExampleSound = findViewById(R.id.ivExampleSound);

        ivNextExample = findViewById(R.id.ivNextExample);
        ivNextDefinition = findViewById(R.id.ivNextDefinition);

        tvWordText = findViewById(R.id.tvWordText);
        tvWordDefinition = findViewById(R.id.tvWordDefinition);
        tvWordExample = findViewById(R.id.tvWordExample);


        ///Listeners
        ivWordIsApproved.setLongClickable(true);

        ivWordIsApproved.setOnLongClickListener(this);
        ivAddFav.setOnLongClickListener(this);

        ivWordIsApproved.setOnClickListener(this);
        ivAddFav.setOnClickListener(this);
        ivWordSound.setOnClickListener(this);
        ivDefinitionSound.setOnClickListener(this);
        ivExampleSound.setOnClickListener(this);

        ivNextExample.setOnClickListener(this);
        ivNextDefinition.setOnClickListener(this);

        findViewById(R.id.cvNext).setOnClickListener(this);

        if (savedInstanceState != null) {
            String wordJson = savedInstanceState.getString(WORD_KEY);
            examplesCurrentIndex = savedInstanceState.getInt(EXAMPLE_INDEX_KEY);
            definitionsCurrentIndex = savedInstanceState.getInt(DEFINITION_INDEX_KEY);
            languagesCurrentIndex = savedInstanceState.getInt(LANGUAGE_INDEX_KEY);
            imagesCurrentIndex = savedInstanceState.getInt(IMAGE_INDEX_KEY);
            Gson gson = new Gson();
            currentWord = gson.fromJson(wordJson, Word.class);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        HeaderEvents(this);
        setLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    void setLoading() {
        clContent.animate().alpha(0.0f);
        av_splash_animation.animate().alpha(1.0f);
    }

    void hideLoading() {
        clContent.animate().alpha(1.0f);
        av_splash_animation.animate().alpha(0.0f);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Gson gson = new Gson();
        String wordJson = gson.toJson(currentWord);
        outState.putString(WORD_KEY, wordJson);
        outState.putInt(EXAMPLE_INDEX_KEY, examplesCurrentIndex);
        outState.putInt(DEFINITION_INDEX_KEY, definitionsCurrentIndex);
        outState.putInt(LANGUAGE_INDEX_KEY, languagesCurrentIndex);
        outState.putInt(IMAGE_INDEX_KEY, imagesCurrentIndex);
        super.onSaveInstanceState(outState);
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
                    Toast.makeText(DiscoverActivity.this, "No Engines Installed", Toast.LENGTH_LONG).show();
                } else {
                    if (status == TextToSpeech.SUCCESS) {
                        ttsInitialized();
                    }
                }
            }
        });
        if (currentWord == null) {
            discoverViewModel.getNewResult().observe(this, this);
        } else {
            discoverViewModel.getMutableLiveData().observe(this, this);
            setWord(currentWord);
        }
    }

    void newWord() {
        setLoading();
        clearScreen();
        discoverViewModel.getNewResult();
    }

    void clearScreen() {
        currentFav = false;
        isApproved = 0;
        ivAddFav.setImageResource(R.drawable.ic_un_favorite_24);
        ivWordIsApproved.setImageResource(R.drawable.ic_none_approved_24);
    }

    void setWord(Word word) {
        if (!isWordValid(word)) {
            tryCount++;
            if (tryCount < 5) {
                newWord();
            } else {
                wordError();
            }
            return;
        }
        currentWord = word;
        wordInformation = new UserWordInformation();
        wordInformation.setWordID(word.getWordId());
        initializeWord(word);
    }

    void initializeWord(Word word) {
        tryCount = 0;
        setImage(word, imagesCurrentIndex);
        setWordText(word);
        setDefinition(word, definitionsCurrentIndex);
        setExample(word, examplesCurrentIndex);
        hideLoading();
    }

    void wordError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR")
                .setMessage("Word error 5 times")
                .setPositiveButton("OK", null)
                .create().show();
    }

    void setImage(Word word, int imageIndex) {
        imagesCurrentIndex = imageIndex;
        Glide.with(this)
                .load(word.getImages().get(imagesCurrentIndex))
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_terrain_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(ivWordImage);
    }

    void setWordText(Word word) {
        tvWordText.setText(word.getLanguage().get(languagesCurrentIndex).getCode());
    }

    void setDefinition(Word word, int definitionIndex) {
        definitionsCurrentIndex = definitionIndex;
        if (definitionsCurrentIndex >= word.getLanguage().get(languagesCurrentIndex).getDefinitions().size()) {
            definitionsCurrentIndex = 0;
        }
        tvWordDefinition.setText(word.getLanguage().get(languagesCurrentIndex).getDefinitions().get(definitionsCurrentIndex));

    }

    void setExample(Word word, int exampleIndex) {
        if (word != null) {
            examplesCurrentIndex = exampleIndex;
            if (examplesCurrentIndex >= word.getLanguage().get(languagesCurrentIndex).getExamples().size()) {
                examplesCurrentIndex = 0;
            }
            tvWordExample.setText(word.getLanguage().get(languagesCurrentIndex).getExamples().get(examplesCurrentIndex));
        } else {
            newWord();
        }
    }

    boolean isWordValid(Word word) {
        boolean isValid = true;
        if (word == null ||
                //word.getIsDeleted() ||
                word.getLanguage() == null || word.getLanguage().size() == 0 ||
                word.getImages() == null || word.getImages().size() == 0 ||
                word.getLanguage().get(languagesCurrentIndex).getDefinitions() == null || word.getLanguage().get(languagesCurrentIndex).getDefinitions().size() == 0 ||
                word.getLanguage().get(languagesCurrentIndex).getExamples() == null || word.getLanguage().get(languagesCurrentIndex).getExamples().size() == 0
        ) {
            isValid = false;
        }
        return isValid;
    }

    String setFav(Boolean isFav) {
        String message;
        if (isFav) {
            ivAddFav.setImageResource(R.drawable.ic_favorite_24);
            message = getString(R.string.liked_word);
        } else {
            ivAddFav.setImageResource(R.drawable.ic_un_favorite_24);
            message = getString(R.string.did_not_like_word);
        }
        currentFav = isFav;
        wordInformation.setFav(currentFav);
        sendUserWordInformation();
        //wordInformation
        return message;
    }

    String setApproved(int approved) {
        isApproved = Math.abs(approved) % 3;
        String message = getString(R.string.none_approved_word);
        int icon;
        switch (isApproved) {
            case 0:
                icon = R.drawable.ic_none_approved_24;
                break;
            case 1:
                icon = R.drawable.ic_approved;
                message = getString(R.string.approved_word);
                break;
            case 2:
                icon = R.drawable.ic_unapproved;
                message = getString(R.string.unapproved_word);
                break;
            default:
                icon = R.drawable.ic_none_approved_24;
                isApproved = 0;
                break;
        }
        ivWordIsApproved.setImageResource(icon);
        wordInformation.setApproved(isApproved);
        sendUserWordInformation();
        return message;
    }

    void sendUserWordInformation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            firebaseService.setUserWordInfo(getDeviceID(), wordInformation, new DnsResolver.Callback<MobileResult>() {
                @Override
                public void onAnswer(@NonNull MobileResult answer, int rcode) {
                    Log.d("TAG2", "onAnswer: " + answer.getMessage() + " " + rcode);
                }

                @Override
                public void onError(@NonNull DnsResolver.DnsException error) {
                    Log.d("TAG2", "onError: " + error.getMessage());
                }
            });
        }
    }

    void onVolumeUp(ImageView image, boolean isUp) {
        ClearVolumes();
        if (isUp && image != null) {
            image.setImageResource(R.drawable.ic_sound_active_24);
        }
    }

    void ClearVolumes() {
        ivWordSound.setImageResource(R.drawable.ic_sound_24);
        ivDefinitionSound.setImageResource(R.drawable.ic_sound_24);
        ivExampleSound.setImageResource(R.drawable.ic_sound_24);
    }

    @Override
    public void onClick(View v) {
        String utteranceID;
        ImageView view;
        switch (v.getId()) {
            case R.id.ivWordIsApproved:
                String snackApprovedMessage = setApproved(++isApproved);
                Snackbar.make(clContent, snackApprovedMessage, Snackbar.LENGTH_SHORT).setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setApproved(--isApproved);
                    }
                }).show();
                break;
            case R.id.ivAddFav:
                String snackFavMessage = setFav(!currentFav);
                Snackbar.make(clContent, snackFavMessage, Snackbar.LENGTH_SHORT).setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setFav(!currentFav);
                    }
                }).show();
                break;
            case R.id.cvNext:
                newWord();
                break;
            case R.id.ivWordSound:
                view = (ImageView) v;
                onVolumeUp(view, true);
                utteranceID = String.valueOf(R.id.ivWordSound);
                ConvertTextToSpeech(tvWordText.getText().toString(), utteranceID);
                break;
            case R.id.ivDefinitionSound:
                view = (ImageView) v;
                onVolumeUp(view, true);
                utteranceID = String.valueOf(R.id.ivDefinitionSound);
                ConvertTextToSpeech(tvWordDefinition.getText().toString(), utteranceID);
                break;
            case R.id.ivExampleSound:
                view = (ImageView) v;
                onVolumeUp(view, true);
                utteranceID = String.valueOf(R.id.ivExampleSound);
                ConvertTextToSpeech(tvWordExample.getText().toString(), utteranceID);
                break;
            case R.id.ivNextDefinition:
                setDefinition(currentWord, ++definitionsCurrentIndex);
                break;
            case R.id.ivNextExample:
                setExample(currentWord, ++examplesCurrentIndex);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.ivWordIsApproved:
                Message approvedMessage = new Message();
                approvedMessage.icon = R.drawable.ic_info_accent_24;
                approvedMessage.title = getString(R.string.approved_info_title);
                approvedMessage.message = getString(R.string.approved_info_message);
                approvedMessage.negativeButton = getString(R.string.approved_info_positive);
                ShowAlertDialog(DiscoverActivity.this, approvedMessage, null);
                break;
            case R.id.ivAddFav:
                Message favMessage = new Message();
                favMessage.icon = R.drawable.ic_info_accent_24;
                favMessage.title = "getString(R.string.approved_info_title)";
                favMessage.message = "getString(R.string.approved_info_message)";
                favMessage.negativeButton = getString(R.string.approved_info_positive);
                ShowAlertDialog(DiscoverActivity.this, favMessage, null);
                break;
            default:
                break;
        }
        return false;
    }

    void ConvertTextToSpeech(String text, String utteranceID) {
        Bundle bundle = new Bundle();
        bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceID);
        if (text == null || text.trim().equals("")) {
            text = getString(R.string.content_not_available);
        }
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, utteranceID);
    }

    void ttsInitialized() {
        myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            // this method will always called from a background thread.
            public void onDone(final String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onVolumeUp(null, false);
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
            }
        });

        // set Language
        if (languagesCurrentIndex == 1) {
            myTTS.setLanguage(new Locale("tr", "TR"));
        } else if (languagesCurrentIndex == 2) {
            myTTS.setLanguage(new Locale("es", "ES"));
        } else {
            myTTS.setLanguage(Locale.US);
        }
    }

    @Override
    public void onChanged(MobileResult result) {
        if (result != null && result.getCode() == 0 && result.getResult() != null) {
            setWord(result.getResult(Word.class));
        }
    }
}
