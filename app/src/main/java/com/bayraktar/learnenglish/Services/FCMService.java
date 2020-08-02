package com.bayraktar.learnenglish.Services;

import android.util.Log;

import com.bayraktar.learnenglish.Manager.MyNotificationManager;
import com.bayraktar.learnenglish.Views.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            MyNotificationManager.setTapAction(this, SplashActivity.class);
            MyNotificationManager.sendOnChannel1(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String s) {
        //register
        Log.d("TAG2", "onNewToken: " + s);
        super.onNewToken(s);
    }
}
