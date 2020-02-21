package com.bayraktar.learnenglish.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bayraktar.learnenglish.API.FirebaseHelper;
import com.bayraktar.learnenglish.Interfaces.IFirebase;
import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.R;
import com.bayraktar.learnenglish.Views.MainActivity;

import java.util.List;

import static com.bayraktar.learnenglish.App.CHANNEL_ID;

public class MyFirebaseService extends Service implements IFirebase {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FirebaseHelper firebaseHelper = new FirebaseHelper(this, this);
        firebaseHelper.initialize();
        //firebaseHelper.getWordList();

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

//        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onGetWordList(List<Word> wordList) {
        for (Word word : wordList) {
            Log.d("TAGS", "SERVICE SetData: TR => " + word.getTr());

//            Intent notificationIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    0, notificationIntent, 0);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("Example Service")
//                    .setContentText(word.getTr())
//                    .setSmallIcon(R.drawable.ic_android)
//                    .setContentIntent(pendingIntent)
//                    .build();
//                startForeground(1, notification);

        }

        Intent serviceIntent = new Intent(this, MyFirebaseService.class);
        serviceIntent.putExtra("inputExtra", "SERVICE");
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    @Override
    public void onSetWord(boolean isSuccess) {

    }
}
