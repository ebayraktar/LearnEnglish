package com.bayraktar.learnenglish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bayraktar.learnenglish.Models.Message;

import java.util.Objects;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @SuppressLint("HardwareIds")
    private String getID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public void HeaderEvents(Activity activity) {
        activity.findViewById(R.id.clRoot).setBackgroundResource(Constants.BACKGROUND_DRAWABLE);
        String ID = getID();
        TextView tvID = findViewById(R.id.tvID);
        tvID.setText(ID);
    }

    public void ShowExitMessage(final Activity activity, Message message) {
        if ((alertDialog != null && alertDialog.isShowing())
                || activity == null
                || message == null) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.view_custom, null);

        TextView tvMessage = customView.findViewById(R.id.tvMessage);
        TextView tvPositive = customView.findViewById(R.id.tvPositive);
        TextView tvNegative = customView.findViewById(R.id.tvNegative);

        tvPositive.setText(message.positiveButton);
        tvMessage.setText(message.message);
        tvNegative.setText(message.negativeButton);

        customView.findViewById(R.id.cvPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        customView.findViewById(R.id.cvNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(customView);

        alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

}
