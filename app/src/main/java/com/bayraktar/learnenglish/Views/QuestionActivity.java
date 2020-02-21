package com.bayraktar.learnenglish.Views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bayraktar.learnenglish.API.APIHelper;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Interfaces.IAPIDataChanged;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.R;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends BaseActivity implements IAPIDataChanged {

    APIHelper apiHelper;
    List<String> definitionList;
    List<String> translationList;
    int point;
    int total;
    int currentIndex;
    String correctWord;
    PrefManager prefManager;
    RadioGroup rgAnswers;
    RadioButton[] radioButtons;
    TextView tvDefinition, tvTranslation, tvIndex, tvPoint;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        HeaderEvents(this);

        //
        rgAnswers = findViewById(R.id.rgAnswers);
        tvDefinition = findViewById(R.id.tvDefinition);
        tvTranslation = findViewById(R.id.tvTranslation);
        tvIndex = findViewById(R.id.tvIndex);
        tvPoint = findViewById(R.id.tvPoint);

        //
        radioButtons = new RadioButton[]{
                findViewById(R.id.rbA),
                findViewById(R.id.rbB),
                findViewById(R.id.rbC),
                findViewById(R.id.rbD),
                findViewById(R.id.rbE)
        };
        prefManager = new PrefManager(this);
        definitionList = new ArrayList<>();
        translationList = new ArrayList<>();
        apiHelper = new APIHelper(this, this);

        //EVENTS
        findViewById(R.id.ivPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (definitionList.size() < 2)
                    return;
                currentIndex--;
                if (currentIndex < 0)
                    currentIndex = definitionList.size() - 1;
                setDefinitions(currentIndex, definitionList.size());
            }
        });
        findViewById(R.id.ivNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (definitionList.size() < 2)
                    return;
                currentIndex++;
                currentIndex = currentIndex % definitionList.size();
                setDefinitions(currentIndex, definitionList.size());
            }
        });
        findViewById(R.id.cvNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rbID = rgAnswers.getCheckedRadioButtonId();
                if (rbID < 0)
                    return;
                total++;
                RadioButton checkedRB = findViewById(rbID);
                if (checkedRB.getText().toString().equals(correctWord)) {
                    Toast.makeText(QuestionActivity.this, "CORRECT", Toast.LENGTH_SHORT).show();
                    point++;
                    nextQuestion();
                } else {
                    showWrongDialog(checkedRB.getText().toString());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        point = prefManager.getPoint();
        total = prefManager.getTotalPoint();
        currentIndex = 0;

        tvPoint.setText(point + "/" + total);
    }

    @SuppressLint("SetTextI18n")
    private void nextQuestion() {
        currentIndex = 0;
        tvTranslation.setText(null);
        tvDefinition.setText(null);
        tvIndex.setText("0/0");
        prefManager.setTotalPoint(total);
        prefManager.setPoint(point);
        rgAnswers.clearCheck();
        setWords(new String[5]);

        tvPoint.setText(point + "/" + total);
        apiHelper.nextQuestion();
    }

    private void showWrongDialog(String wrongWord) {
        //apiHelper.tra
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(wrongWord + " is wrong")
                .setMessage(wrongWord + " \n"
                        + "The correct answer is " + correctWord)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nextQuestion();
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void setWords(String[] words) {
        if (words.length == radioButtons.length) {
            for (int i = 0; i < words.length; i++) {
                radioButtons[i].setText(words[i]);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDefinitions(int index, int totalIndex) {
        if (definitionList == null || definitionList.size() == 0) {
            Log.d("TAG", "ERROR SET DEFINITION");
            onErrorOccurred("ERROR SET DEFINITION");
            return;
        }
        tvDefinition.setText(definitionList.get(index));
        tvTranslation.setText(translationList.get(index));
        tvIndex.setText(index + 1 + "/" + totalIndex);
        setTranslations(index);
    }

    @SuppressLint("SetTextI18n")
    private void setDefinitions(String definition, int index, int totalIndex) {
        if (definitionList == null || definitionList.size() == 0) {
            Log.d("TAG", "ERROR SET DEFINITION");
            onErrorOccurred("ERROR SET DEFINITION");
            return;
        }
        tvDefinition.setText(definition);
        tvIndex.setText(index + 1 + "/" + totalIndex);
    }

    private void setTranslations(int index) {
        if (translationList == null || translationList.size() == 0) {
            Log.d("TAG", "ERROR SET TRANSLATION");
            onErrorOccurred("ERROR SET TRANSLATION");
            return;
        }
        tvTranslation.setText(translationList.get(index));
    }

    private void setTranslations(String translation) {
        if (translationList == null || translationList.size() == 0) {
            Log.d("TAG", "ERROR SET TRANSLATION");
            onErrorOccurred("ERROR SET TRANSLATION");
            return;
        }
        tvTranslation.setText(translation);
    }


    @Override
    public void onKeyChanged(String key) {

    }

    @Override
    public void onWordsChanged(String[] words) {
        if (words != null && words.length > 0) {
            setWords(words);
        } else {
            Log.d("TAGI", "WORD NULL");
        }
    }

    @Override
    public void onDefinitionChanged(String word, List<String> definitionList, List<String> exampleList) {
        try {
            if (definitionList != null && definitionList.size() > 0) {
                this.definitionList = definitionList;
                this.correctWord = word;
                setDefinitions(definitionList.get(currentIndex), currentIndex, definitionList.size());
            } else
                Log.d("TAGI", "ERROR DEFINITIONS NULL: ");
            if (exampleList != null && exampleList.size() > 0) {
                Log.d("TAGI", "not null: ");
            } else
                Log.d("TAGI", "ERROR EXAMPLES NULL: ");
        } catch (Exception ex) {
            onErrorOccurred("DICTIONARY ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void onTranslationChanged(List<String> translatedString) {
        if (translatedString != null && translatedString.size() > 0) {
            this.translationList = translatedString;
            setTranslations(translationList.get(currentIndex));
        } else
            Log.d("TAGI", "ERROR TRANSLATED STRING NULL: ");
    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        Log.d("TAG", "onErrorOccurred: " + errorMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR")
                .setMessage("An error occurred " + errorMessage)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nextQuestion();
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
