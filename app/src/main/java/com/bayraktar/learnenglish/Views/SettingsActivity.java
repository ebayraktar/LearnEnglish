package com.bayraktar.learnenglish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bayraktar.learnenglish.BaseActivity;
import com.bayraktar.learnenglish.Constants;
import com.bayraktar.learnenglish.Manager.PrefManager;
import com.bayraktar.learnenglish.R;


public class SettingsActivity extends BaseActivity {

    PrefManager prefManager;
    final int REQUEST_CODE = 1;
    int bg;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefManager = new PrefManager(SettingsActivity.this);

        findViewById(R.id.clRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.cvChangeBG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bg = prefManager.getBackgroundPosition();
                Intent changeBG = new Intent(SettingsActivity.this, ChangeBGActivity.class);
                startActivityForResult(changeBG, REQUEST_CODE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //                Toast.makeText(this, "DATA CANCELED", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            int position = data.getIntExtra("result", -1);
//                Toast.makeText(this, "DATA" + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
