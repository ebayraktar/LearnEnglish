package com.bayraktar.learnenglish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bayraktar.learnenglish.Adapter.BackgroundPagerAdapter;
import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Constants;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.R;

import java.util.Random;

public class ChangeBGActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ViewPager vpBackground;
    BackgroundPagerAdapter pagerAdapter;
    PrefManager prefManager;
    int defaultBG;

    @Override
    public void onBackPressed() {
        if (defaultBG != -1) {
            Constants.BACKGROUND_POSITION = defaultBG;
            Constants.BACKGROUND_DRAWABLE = Constants.backgroundPositions[defaultBG];
        }
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bg);
        HeaderEvents(this);

        vpBackground = findViewById(R.id.vpBackground);

        prefManager = new PrefManager(ChangeBGActivity.this);
        defaultBG = prefManager.getBackgroundPosition();

        pagerAdapter = new BackgroundPagerAdapter(ChangeBGActivity.this, Constants.backgroundPositions);
        vpBackground.setAdapter(pagerAdapter);
        vpBackground.addOnPageChangeListener(ChangeBGActivity.this);

        findViewById(R.id.cvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.cvRandom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                int position;
                do {
                    position = rnd.nextInt(Constants.backgroundPositions.length);
                } while (position == Constants.BACKGROUND_POSITION);
                vpBackground.setCurrentItem(position);

            }
        });
        findViewById(R.id.cvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", prefManager.getBackgroundPosition());
                setResult(ChangeBGActivity.RESULT_OK, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vpBackground.setCurrentItem(Constants.BACKGROUND_POSITION);
    }

    private void setBackground(int position) {
        prefManager.setBackgroundPosition(position);
        Constants.BACKGROUND_DRAWABLE = Constants.backgroundPositions[position];
        Constants.BACKGROUND_POSITION = position;
        HeaderEvents(ChangeBGActivity.this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setBackground(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
