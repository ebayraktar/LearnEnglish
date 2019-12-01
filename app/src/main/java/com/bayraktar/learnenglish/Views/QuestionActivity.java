package com.bayraktar.learnenglish.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bayraktar.learnenglish.API.APIHelper;
import com.bayraktar.learnenglish.Interfaces.IAPIDataChanged;
import com.bayraktar.learnenglish.R;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends AppCompatActivity implements IAPIDataChanged {

    APIHelper apiHelper;
    List<String> definitionList;
    List<String> translationList;
    int point;
    int total;
    int currentIndex;
    String correctWord;
    String ID;
    SharedPreferences sharedPref;

    RadioGroup rgAnswers;
    RadioButton[] radioButtons;
    TextView tvDefinition, tvTranslation, tvIndex, tvPoint, tvID;
    ImageView ivPrev, ivNext;
    Button btnContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        rgAnswers = findViewById(R.id.rgAnswers);
        radioButtons = new RadioButton[]{
                findViewById(R.id.rbA),
                findViewById(R.id.rbB),
                findViewById(R.id.rbC),
                findViewById(R.id.rbD),
                findViewById(R.id.rbE)
        };
        tvDefinition = findViewById(R.id.tvDefinition);
        tvTranslation = findViewById(R.id.tvTranslation);
        tvIndex = findViewById(R.id.tvIndex);
        tvPoint = findViewById(R.id.tvPoint);
        tvID = findViewById(R.id.tvID);
        ivPrev = findViewById(R.id.ivPrev);
        ivNext = findViewById(R.id.ivNext);
        btnContinue = findViewById(R.id.btnContinue);

        ID = getID();
        point = 0;
        total = 0;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        getReferences(sharedPref);
        currentIndex = 0;
        definitionList = new ArrayList<>();
        translationList = new ArrayList<>();
        apiHelper = new APIHelper(this, this);

        tvPoint.setText(point + "/" + total);
        tvID.setText(ID);
        ivPrev.setOnClickListener(new View.OnClickListener() {
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
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (definitionList.size() < 2)
                    return;
                currentIndex++;
                currentIndex = currentIndex % definitionList.size();
                setDefinitions(currentIndex, definitionList.size());
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
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
//        for (int i = 0; i < 50; i++) {
//            Random r = new Random();
//            int a = r.nextInt(5);
//            Log.d("TAG", "onWordsChanged: Random " + a);
//        }
    }

    private void getReferences(SharedPreferences sharedPref) {
        point = sharedPref.getInt("_POINT", 0);
        total = sharedPref.getInt("_TOTAL", 0);
    }

    private void setReferences(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("_POINT", point);
        editor.putInt("_TOTAL", total);
        editor.commit();
    }

    @SuppressLint("HardwareIds")
    private String getID() {
        return Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
    }

    private void nextQuestion() {
        currentIndex = 0;
        tvTranslation.setText(null);
        tvDefinition.setText(null);
        tvIndex.setText("0/0");
        setReferences(sharedPref);
        rgAnswers.clearCheck();
        setWords(new String[5]);

        tvPoint.setText(new StringBuilder().append(point).append("/").append(total).toString());
        apiHelper.nextQuestion();
    }

    private void showWrongDialog(String wrongWord) {
        //apiHelper.tra
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(wrongWord + " is wrong")
                .setMessage(wrongWord + " \n"
                        + "DOĞRUSU: " + correctWord)
                .setNegativeButton("TAMAM", new DialogInterface.OnClickListener() {
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

    private void setDefinitions(int index, int totalIndex) {
        if (definitionList == null || definitionList.size() == 0) {
            Log.d("TAG", "ERROR SET DEFINITON");
            onErrorOccurred("ERROR SET DEFINITON");
            return;
        }
        tvDefinition.setText(definitionList.get(index));
        tvTranslation.setText(translationList.get(index));
        tvIndex.setText(index + 1 + "/" + totalIndex);
        setTranslations(index);
    }

    private void setDefinitions(String definition, int index, int totalIndex) {
        if (definitionList == null || definitionList.size() == 0) {
            Log.d("TAG", "ERROR SET DEFINITON");
            onErrorOccurred("ERROR SET DEFINITON");
            return;
        }
        tvDefinition.setText(definition);
        tvIndex.setText(index + 1 + "/" + totalIndex);
    }

    private void setTranslations(int index) {
        if (translationList == null || translationList.size() == 0) {
            Log.d("TAG", "ERROR SET TRANSTALION");
            onErrorOccurred("ERROR SET TRANSTALION");
            return;
        }
        tvTranslation.setText(translationList.get(index));
    }

    private void setTranslations(String translation) {
        if (translationList == null || translationList.size() == 0) {
            Log.d("TAG", "ERROR SET TRANSTALION");
            onErrorOccurred("ERROR SET TRANSTALION");
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
                Log.d("TAGI", "ERROR DEFINITONS NULL: ");
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
            Log.d("TAGI", "ERROR TRANSLATEDSTRING NULL: ");
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
