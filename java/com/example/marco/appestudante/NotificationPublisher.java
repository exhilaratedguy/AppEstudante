package com.example.marco.appestudante;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class NotificationPublisher extends BroadcastReceiver
{
    private SharedPreferences sharedPrefs;

    public void onReceive(Context context, Intent intent)
    {
        sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_file_name), 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_notifications", false);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra("notification");
        int id = intent.getIntExtra("id", 0);

        if(bool_1)
            notificationManager.notify(id, notification);
    }
}
