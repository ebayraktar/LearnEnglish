package com.bayraktar.learnenglish.Manager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocaleManager {
    private Activity activity;

    public LocaleManager(Activity activity) {
        this.activity = activity;
    }

    public void setAppLocale(@NonNull String localeCode) {

        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }
}
