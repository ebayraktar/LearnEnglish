package com.bayraktar.learnenglish.Manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.bayraktar.learnenglish.Constants;

import java.util.Locale;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "_AppPref";

    private static final String IS_FIRST_TIME_LAUNCH = "_IsFirstTimeLaunch";
    private static final String POINT = "_Point";
    private static final String TOTAL_POINT = "_TotalPoint";
    private static final String BACKGROUND_POSITION = "_BackgroundPosition";
    private static final String SELECTED_LANGUAGE = "_SelectedLanguage";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Activity context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setPoint(int value) {
        editor.putInt(POINT, value);
        editor.commit();
    }

    public int getPoint() {
        return pref.getInt(POINT, 0);
    }

    public void setTotalPoint(int value) {
        editor.putInt(TOTAL_POINT, value);
        editor.commit();
    }

    public int getTotalPoint() {
        return pref.getInt(TOTAL_POINT, 0);
    }

    public void setBackgroundPosition(int position) {
        editor.putInt(BACKGROUND_POSITION, position);
        editor.commit();
    }

    public int getBackgroundPosition() {
        return pref.getInt(BACKGROUND_POSITION, -1);
    }

    public void setLanguage(int lang) {
        editor.putInt(SELECTED_LANGUAGE, lang);
        editor.commit();
    }

    public int getLanguage() {
        int defaultItem = 0;
        for (int i = 0; i < Constants.localePositions.length; i++) {
            String item = Constants.localePositions[i];
            if (item.toLowerCase(Locale.ENGLISH).equals(Locale.getDefault().getLanguage().toLowerCase(Locale.ENGLISH))) {
                defaultItem = i;
                break;
            }
        }
        return pref.getInt(SELECTED_LANGUAGE, defaultItem);
    }


}
