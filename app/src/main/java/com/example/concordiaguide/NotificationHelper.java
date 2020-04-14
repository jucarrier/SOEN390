package com.example.concordiaguide;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_1_ID = "channel1ID";
    public static final String CHANNEL_1_NAME = "channel 1";

    private NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, CHANNEL_1_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableVibration(true);
        channel1.enableLights(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_directions_walk_black_24dp);
    }
}
