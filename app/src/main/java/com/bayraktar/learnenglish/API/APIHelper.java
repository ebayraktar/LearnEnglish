package com.bayraktar.learnenglish.API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bayraktar.learnenglish.Clients.OxfordClient;
import com.bayraktar.learnenglish.Clients.RandomWordsClient;
import com.bayraktar.learnenglish.Clients.YandexClient;
import com.bayraktar.learnenglish.Interfaces.IAPIDataChanged;
import com.bayraktar.learnenglish.Interfaces.IOxford;
import com.bayraktar.learnenglish.Interfaces.IRandomWords;
import com.bayraktar.learnenglish.Interfaces.IYandex;
import com.bayraktar.learnenglish.Models.Oxford.Entry;
import com.bayraktar.learnenglish.Models.Oxford.LexicalEntry;
import com.bayraktar.learnenglish.Models.Oxford.OxfordModel;
import com.bayraktar.learnenglish.Models.Oxford.Result;
import com.bayraktar.learnenglish.Models.Oxford.Sense;
import com.bayraktar.learnenglish.Models.Yandex.YandexModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIHelper implements IAPIDataChanged {
    private String RANDOMWORDS_KEY;
    private Activity activity;
    private IAPIDataChanged dataChanged;

    public APIHelper(Activity activity, IAPIDataChanged dataChanged) {
        this.activity = activity;
        this.dataChanged = dataChanged;
        Initialization();
    }

    public void nextQuestion() {
        Initialization();
    }

    private void Initialization() {
//        if (TextUtils.isEmpty(RANDOMWORDS_KEY)) {
//            RandomKeyTask task = new RandomKeyTask(this);
//            task.execute();
//        } else {
            APIHelper.RandomWordsTask task = new APIHelper.RandomWordsTask(this);
            task.execute(RANDOMWORDS_KEY);
//        }
    }

    private void dictionary(final String text) {
        final ProgressDialog _dialog = new ProgressDialog(activity);
        _dialog.setTitle("Sözlüğe bağlanılıyor...");
        _dialog.show();

        IOxford oxford = OxfordClient.getClient().create(IOxford.class);
        Call<OxfordModel> callOxford = oxford.getOxford(text);
        callOxford.enqueue(new Callback<OxfordModel>() {
            @Override
            public void onResponse(@NonNull Call<OxfordModel> call, @NonNull Response<OxfordModel> response) {
                if (response.isSuccessful()) {
                    OxfordModel oxfordModel = response.body();
                    if (oxfordModel == null || oxfordModel.results == null) {
                        dataChanged.onErrorOccurred("Response null");
                        return;
                    }
                    List<String> definitionList = new ArrayList<>();
                    try {
                        //DEFINITIONS
                        for (Result result : oxfordModel.results) {
                            for (LexicalEntry lexicalEntry : result.lexicalEntries) {
                                for (Entry entry : lexicalEntry.entries) {
                                    for (Sense sense : entry.senses) {
                                        definitionList.addAll(sense.definitions);
                                    }
                                }
                            }
                        }
                        //EXAMPLES
                        List<String> exampleList = new ArrayList<>();
//                    for(:){}

                        onDefinitionChanged(text, definitionList, exampleList);
                        dataChanged.onDefinitionChanged(text, definitionList, exampleList);
                    } catch (Exception ex) {
                        dataChanged.onErrorOccurred("2Response error: " + ex.getMessage());
                    }
                } else {
                    dataChanged.onErrorOccurred("Dictionary request failed");
                }
                if (_dialog.isShowing()) {
                    _dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OxfordModel> call, @NonNull Throwable t) {
                if (_dialog.isShowing())
                    _dialog.dismiss();
                dataChanged.onErrorOccurred("DICTIONARY: " + t.getMessage());
            }
        });
    }

    private void translate(String[] text) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle("Yandex.Translate bağlanılıyor...");
        dialog.show();

        IYandex yandex = YandexClient.getClient().create(IYandex.class);
        String YANDEX_API_KEY = "trnsl.1.1.20191123T191655Z.c4e490008f275f7b.1e4729b73e064c7b91ff8beaabd17e713d0dfb73";
        Call<YandexModel> callYandex = yandex.getYandex(YANDEX_API_KEY, text, "en-tr");
        callYandex.enqueue(new Callback<YandexModel>() {
            @Override
            public void onResponse(@NonNull Call<YandexModel> call, @NonNull Response<YandexModel> response) {
                if (response.isSuccessful()) {
                    YandexModel yandexModel = response.body();
                    if (yandexModel == null || yandexModel.text == null) {
                        dataChanged.onErrorOccurred("Response null");
                        return;
                    }
                    List<String> translationList = new ArrayList<>(yandexModel.text);
                    dataChanged.onTranslationChanged(translationList);

                } else {
                    dataChanged.onErrorOccurred("Translation request failed");
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<YandexModel> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                dataChanged.onErrorOccurred("TRANSLATE: " + t.getMessage());
            }
        });
    }

    private String[] getRandomWordsExecute(String api_key) throws IOException {
        IRandomWords randomWordsService = RandomWordsClient.getInstance().getRandomWordsService();
        Call<String[]> randomWordArray = randomWordsService.getRandomWords(5);
        Response<String[]> response = randomWordArray.execute();
        return response.body();
    }

    private String getRandomWordKeyExecute() throws IOException {
        IRandomWords randomWordsService = RandomWordsClient.getInstance().getRandomWordsService();
        Call<String> randomKey = randomWordsService.getKey();
        Response<String> response = randomKey.execute();
        return response.body();
    }


    @Override
    public void onKeyChanged(String key) {
        RandomWordsTask task = new RandomWordsTask(this);
        task.execute(key);
    }

    @Override
    public void onWordsChanged(String[] words) {
        try {

            Random r = new Random();
            int a = r.nextInt(5);
            dictionary(words[a]);
        } catch (Exception ex) {
            Log.d("TAG", "onWordsChanged: ERROR " + words.length);
            Log.d("TAG", "onWordsChanged: ERROR " + ex.getMessage());
        }
    }

    @Override
    public void onDefinitionChanged(String word, List<String> definitionList, List<String> exampleList) {
        try {
            String[] definitions = new String[definitionList.size()];
            definitionList.toArray(definitions);
            translate(definitions);
        } catch (Exception ex) {
            dataChanged.onErrorOccurred("Translate error: " + ex.getMessage());
        }
    }

    @Override
    public void onTranslationChanged(List<String> translatedString) {

    }

    @Override
    public void onErrorOccurred(String errorMessage) {

    }


    //  RANDOM KEY TASK
    private static class RandomKeyTask extends AsyncTask<Void, Integer, String> {
        private WeakReference<APIHelper> activityWeakReference;

        RandomKeyTask(APIHelper helper) {
            activityWeakReference = new WeakReference<>(helper);
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return;
            }

            dialog = new ProgressDialog(helper.activity);
            dialog.setTitle("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return null;
            }

            try {
                return helper.getRandomWordKeyExecute();
            } catch (IOException e) {
                helper.dataChanged.onErrorOccurred("KEY CATCH " + e.getMessage());
                Log.d("TAG", "WORDS ERROR: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return;
            }

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (TextUtils.isEmpty(result)) {
                helper.dataChanged.onErrorOccurred("Key error!");
                return;
            }

            helper.RANDOMWORDS_KEY = result;
            helper.onKeyChanged(result);
            helper.dataChanged.onKeyChanged(result);
        }
    }

    //  RANDOM WORDS TASK
    private static class RandomWordsTask extends AsyncTask<String, Integer, String[]> {
        private WeakReference<APIHelper> activityWeakReference;

        RandomWordsTask(APIHelper helper) {
            activityWeakReference = new WeakReference<>(helper);
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return;
            }

            dialog = new ProgressDialog(helper.activity);
            dialog.setTitle("Please wait");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return null;
            }
            try {
                return helper.getRandomWordsExecute(strings[0]);
            } catch (IOException e) {
                helper.dataChanged.onErrorOccurred("WORDS CATCH " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] words) {
            super.onPostExecute(words);
            APIHelper helper = activityWeakReference.get();
            if (helper == null || helper.activity.isFinishing()) {
                return;
            }

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            if (words == null || words.length == 0) {
                helper.dataChanged.onErrorOccurred("WORDS is null");
                return;
            }

            helper.onWordsChanged(words);
            helper.dataChanged.onWordsChanged(words);
        }
    }

}
