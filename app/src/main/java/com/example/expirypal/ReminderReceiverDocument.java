package com.example.expirypal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderReceiverDocument extends BroadcastReceiver {

    private static final String CHANNEL_ID = "DocumentReminderChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve additional data if needed
        String documentName = intent.getStringExtra("document_name");

        // Handle the reminder here (e.g., show a notification)
        showNotification(context, documentName);
    }

    private void showNotification(Context context, String documentName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for devices running Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Document Reminder Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.expired)
                .setContentTitle("Document Reminder")
                .setContentText("Don't forget about " + documentName)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }
}
