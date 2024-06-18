package com.example.railridemate.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.railridemate.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "TrainChannel";
    private static final String CHANNEL_NAME = "Train Notifications";

    private Context mContext;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Display the notification
        notificationManager.notify(0, builder.build());
    }
}
