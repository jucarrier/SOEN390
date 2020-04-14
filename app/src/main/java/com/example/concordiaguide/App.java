package com.example.concordiaguide;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class App extends Application {
//    as shown in this tutorial https://www.youtube.com/watch?v=tTbd1Mfi-Sk
//    this class is called as soon as the app opens, creating the notification channels for later use

    public static final String CHANNEL_1_ID = "channel1";

    //sets up channels as soon as the app opens
    @RequiresApi(api = Build.VERSION_CODES.O)   //requires android oreo or higher
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    CHANNEL_1_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("Main notification channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }


    }
}
