package com.bayraktar.learnenglish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @SuppressLint("HardwareIds")
    public String getDeviceID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public void HeaderEvents(Activity activity) {
        activity.findViewById(R.id.clRoot).setBackgroundResource(Constants.BACKGROUND_DRAWABLE);
        String ID = getDeviceID();
        TextView tvID = findViewById(R.id.tvID);
        tvID.setText(ID);
    }

    public void HideBackButton(Activity activity, boolean visibility) throws NullPointerException {
        if (!visibility)
            activity.findViewById(R.id.ivBack).setVisibility(View.INVISIBLE);
    }

    public void SetBackButton(Activity activity) throws NullPointerException {
        activity.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void ShowAlertDialog(final Activity activity, Message message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(message.title)
                .setMessage(message.message)
                .setIcon(message.icon)
                .setNegativeButton(message.negativeButton, listener)
                .setPositiveButton(message.positiveButton, listener)
                .create().show();

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
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                alertDialog.dismiss();
                //activity.finish();
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
