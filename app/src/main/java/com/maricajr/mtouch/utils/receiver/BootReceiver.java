package com.maricajr.mtouch.utils.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.maricajr.mtouch.test.job.schedule.Util;
import com.maricajr.mtouch.utils.service.MTouchService;

import static android.content.ContentValues.TAG;

public class BootReceiver extends BroadcastReceiver {
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Util.scheduleJob(context);
        }else {
            context.startService(new Intent(context,MTouchService.class));
        }


    }

}
