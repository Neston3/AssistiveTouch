package com.maricajr.mtouch.test.job.schedule;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.maricajr.mtouch.utils.service.MTouchService;

import static android.content.ContentValues.TAG;
import static com.maricajr.mtouch.StringUtil.NAME;
import static com.maricajr.mtouch.StringUtil.PREF_NAME;

public class Util {

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        assert jobScheduler != null;
        jobScheduler.schedule(builder.build());


        checkIfrunningAfterBoot(context);

    }

    private static void checkIfrunningAfterBoot(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.contains(NAME)){
            String d=sharedPreferences.getString(NAME,"");
            if (d.contains("on")){
                Intent mfloat=new Intent(context, MTouchService.class);
                context.startService(mfloat);
            }else {
                Log.i(TAG, "scheduleJob: shared preference is OFF");
            }
        }
    }

}
