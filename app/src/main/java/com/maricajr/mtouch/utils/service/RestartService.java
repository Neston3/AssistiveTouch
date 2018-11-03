package com.maricajr.mtouch.utils.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class RestartService {
    private Context context;

    public RestartService(Context context) {
        this.context = context;
    }

    public void restart() {
        Intent restartServiceIntent = new Intent(context.getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(context.getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                context.getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) context.getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

    }

}
