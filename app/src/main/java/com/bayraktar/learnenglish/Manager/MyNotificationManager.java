package com.bayraktar.learnenglish.Manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bayraktar.learnenglish.R;

import static com.bayraktar.learnenglish.App.CHANNEL_1_ID;

public class MyNotificationManager {
    private static PendingIntent pendingIntent;

    public static void setTapAction(Context context, Class<?> cls) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, cls);
        intent.putExtra(context.getPackageName(), 1); // this line sets off closing notification in onCreate()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public static void sendOnChannel1(Context context, String title, String message) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_learn_english_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(android.R.drawable.ic_input_add, context.getString(R.string.show), pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
}
