package com.bayraktar.learnenglish.Views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bayraktar.learnenglish.Clients.OxfordClient;
import com.bayraktar.learnenglish.Clients.RandomWordsClient;
import com.bayraktar.learnenglish.Clients.YandexClient;
import com.bayraktar.learnenglish.Interfaces.IOxford;
import com.bayraktar.learnenglish.Interfaces.IRandomWords;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Oxford.Entry;
import com.bayraktar.learnenglish.Models.Oxford.LexicalEntry;
import com.bayraktar.learnenglish.Models.Oxford.OxfordModel;
import com.bayraktar.learnenglish.Models.Oxford.Result;
import com.bayraktar.learnenglish.Models.Oxford.Sense;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;
import com.bayraktar.learnenglish.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.SQLTransactionRollbackException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String YANDEX_API_KEY = "trnsl.1.1.20191123T191655Z.c4e490008f275f7b.1e4729b73e064c7b91ff8beaabd17e713d0dfb73";
    private String RANDOMWORDS_KEY;
    private String[] RANDOM_WORDS;

    IOxford oxford;
    IYandex yandex;

    EditText edtxtDictionary, edtxtTranslate;
    Button btnDictionary, btnTranslate, btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtxtDictionary = findViewById(R.id.edtxtDictionary);
        edtxtTranslate = findViewById(R.id.edtxtTranslate);

        btnDictionary = findViewById(R.id.btnDictionary);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnNext = findViewById(R.id.btnNext);

        btnDictionary.setOnClickListener(this);
        btnTranslate.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        initRandoms();

    }

    private void initRandoms() {
        RandomKeyTask task = new RandomKeyTask(this);
        task.execute();
//        String mKey = task.mMethod();
        //task.
        Log.d("TAG", "KEY:" + RANDOMWORDS_KEY);
//        Log.d("TAG", "mKEY:" + mKey);
        //Log.d("TAG", "VALUES:" + RANDOM_WORDS[0]);
    }


    private void dictionary(String text) {
        final TextView txtDictionary = findViewById(R.id.txtDictionary);
        oxford = OxfordClient.getClient().create(IOxford.class);
        Call<OxfordModel> callOxford = oxford.getOxford(text);
        callOxford.enqueue(new Callback<OxfordModel>() {
            @Override
            public void onResponse(Call<OxfordModel> call, Response<OxfordModel> response) {
                if (response.isSuccessful()) {
                    OxfordModel oxfordModel = response.body();
                    String modelText = "";
                    for (Result result : oxfordModel.results) {
                        for (LexicalEntry lexicalEntry : result.lexicalEntries) {
                            for (Entry entry : lexicalEntry.entries) {
                                for (Sense sense : entry.senses) {
                                    for (String definition : sense.definitions) {
                                        modelText += definition + "\n";
                                    }
                                }
                            }
                        }
                    }
                    txtDictionary.setText(modelText);
                } else {
                    txtDictionary.setText(response.message());
                }
            }

            @Override
            public void onFailure(Call<OxfordModel> call, Throwable t) {
                txtDictionary.setText(t.getMessage());
            }
        });
    }

    private void translate(String[] text) {
        final TextView txtTranslate = findViewById(R.id.txtTranslate);
        yandex = YandexClient.getClient().create(IYandex.class);
        Call<YandexModel> callYandex = yandex.getYandex(YANDEX_API_KEY, text, "en-tr");
        callYandex.enqueue(new Callback<YandexModel>() {
            @Override
            public void onResponse(Call<YandexModel> call, Response<YandexModel> response) {
                if (response.isSuccessful()) {
                    YandexModel yandexModel = response.body();
                    String modelText = "";
                    for (String text : yandexModel.text) {
                        modelText += text + ":";
                    }
                    txtTranslate.setText(modelText);

                } else {
                    txtTranslate.setText(response.message());
                }
            }

            @Override
            public void onFailure(Call<YandexModel> call, Throwable t) {
                txtTranslate.setText(t.getMessage());
            }
        });
    }

    private String[] getRandomWordsExecute(String api_key, int number_of_words) throws IOException {
        IRandomWords randomWordsService = RandomWordsClient.getInstance().getRandomWordsService();
        Call<String[]> randomWordArray = randomWordsService.getRandomWords(api_key, number_of_words);
        Response<String[]> response = randomWordArray.execute();
        return response.body();
    }

    private String getRandomWordKeyExecute() throws IOException {
        IRandomWords randomWordsService = RandomWordsClient.getInstance().getRandomWordsService();
        Call<String> randomKey = randomWordsService.getKey();
        Response<String> response = randomKey.execute();
        Log.d("TAGG", response.body());
        return response.body();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDictionary:
                dictionary(edtxtDictionary.getText().toString());
                break;
            case R.id.btnTranslate:
                translate(new String[]{edtxtTranslate.getText().toString()});
                break;
            case R.id.btnNext:
                RandomWordsTask task = new RandomWordsTask(this);
                task.execute(RANDOMWORDS_KEY);
                break;
            default:
                break;
        }
    }

    private void getKey() {

    }

    //  RANDOM KEY TASK
    private static class RandomKeyTask extends AsyncTask<Void, Integer, String> {
        private WeakReference<MainActivity> activityWeakReference;

        RandomKeyTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        String KEY = "";

        @Override
        protected String doInBackground(Void... params) {
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            try {
                return activity.getRandomWordKeyExecute();
            } catch (IOException e) {
                Log.d("TAG", "ERROR: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            mMethod(result);
            activity.RANDOMWORDS_KEY = result;

            RandomWordsTask task = new RandomWordsTask(activity);
            task.execute(activity.RANDOMWORDS_KEY);
        }

        public String mMethod(String key) {
            return key;
        }
    }

    //  RANDOM WORDS TASK
    private static class RandomWordsTask extends AsyncTask<String, Integer, String[]> {

        private WeakReference<MainActivity> activityWeakReference;

        RandomWordsTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected String[] doInBackground(String... strings) {

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            try {
                return activity.getRandomWordsExecute(strings[0], 5);
            } catch (IOException e) {
                Log.d("TAG", "ERROR: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] words) {
            super.onPostExecute(words);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.RANDOM_WORDS = words;
        }
    }
}
