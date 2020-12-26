package com.bayraktar.learnenglish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HideBackButton( false);
        findViewById(R.id.cvLearn).setOnClickListener(this);
        findViewById(R.id.cvFavorites).setOnClickListener(this);
        findViewById(R.id.cvTop500).setOnClickListener(this);
        findViewById(R.id.cvTop1000).setOnClickListener(this);
        findViewById(R.id.cvProgress).setOnClickListener(this);
        findViewById(R.id.cvSettings).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvLearn:
                startActivity(new Intent(HomeActivity.this, DiscoverActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cvFavorites:
                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cvTop500:
                startActivity(new Intent(HomeActivity.this, SlideWordsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cvTop1000:
            case R.id.cvProgress:
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
            case R.id.cvSettings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
