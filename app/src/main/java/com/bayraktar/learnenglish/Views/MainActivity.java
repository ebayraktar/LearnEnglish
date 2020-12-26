package com.bayraktar.learnenglish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Constants;
import com.bayraktar.learnenglish.Manager.LocaleManager;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity {
    PrefManager prefManager;
    LocaleManager localeManager;

    ImageView ivLanguages;
    int language = 0;
    boolean doubleBackToExitPressedOnce = false;

    int[] titles = new int[]{
            R.string.app_name,
            R.string.start_app,
            R.string.statistics,
            R.string.about,
            R.string.exit
    };
    TextView[] titleTVs;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_back_to_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(MainActivity.this);
        localeManager = new LocaleManager(MainActivity.this);

        titleTVs = new TextView[]{
                findViewById(R.id.tvAppName),
                findViewById(R.id.tvStart),
                findViewById(R.id.tvStatistics),
                findViewById(R.id.tvAbout),
                findViewById(R.id.tvExit)
        };

        ivLanguages = findViewById(R.id.ivLanguages);

        //EVENTS
        ivLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language++;
                language = language % Constants.languagePositions.length;
                prefManager.setLanguage(language);
                localeManager.setAppLocale(Constants.localePositions[language]);
                setLanguage(language);
            }
        });
        findViewById(R.id.ivSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        findViewById(R.id.cvStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        findViewById(R.id.cvStatistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, DiscoverActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.cvAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        findViewById(R.id.cvExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowExitMessage(MainActivity.this, Constants.messageManager.getExit());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        language = prefManager.getLanguage();
        ivLanguages.setImageResource(Constants.languagePositions[language]);
    }

    private void setLanguage(int language) {
        this.language = language;
        ivLanguages.setImageResource(Constants.languagePositions[language]);
        Constants.messageManager.setLanguage(language);
        setTitles();
    }

    private void setTitles() {
        for (int i = 0; i < titleTVs.length; i++) titleTVs[i].setText(titles[i]);
    }
}