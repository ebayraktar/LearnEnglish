package com.bayraktar.learnenglish.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.Constants;
import com.bayraktar.learnenglish.Manager.LocaleManager;
import com.bayraktar.learnenglish.Manager.MessageManager;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

public class SplashActivity extends Activity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        logEvent();

        //Pref Settings
        PrefManager prefManager = new PrefManager(this);
        setBackground(prefManager.getBackgroundPosition());

        //Locale Settings
        LocaleManager localeManager = new LocaleManager(SplashActivity.this);
        localeManager.setAppLocale(Constants.localePositions[prefManager.getLanguage()]);

        Constants.messageManager = new MessageManager(prefManager.getLanguage());
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void logEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setBackground(int position) {
        if (position < 0 || position > Constants.backgroundPositions.length) {
            Random rnd = new Random();
            position = rnd.nextInt(Constants.backgroundPositions.length);
        }

        Constants.BACKGROUND_DRAWABLE = Constants.backgroundPositions[position];
        Constants.BACKGROUND_POSITION = position;
    }
}
