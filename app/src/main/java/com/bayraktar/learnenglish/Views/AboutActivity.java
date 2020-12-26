package com.bayraktar.learnenglish.Views;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class AboutActivity extends BaseActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.clRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.cvWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.cvFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.cvTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.cvYoutube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.coming_soon, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.cvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
