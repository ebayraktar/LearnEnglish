package com.bayraktar.learnenglish.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bayraktar.learnenglish.R;
import com.bayraktar.learnenglish.ui.firebase.FirebaseFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Fragment fragment = FirebaseFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_content, fragment)
                .commit();

    }
}