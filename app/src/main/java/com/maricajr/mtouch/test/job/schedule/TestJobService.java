package com.maricajr.mtouch.test.job.schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.maricajr.mtouch.utils.receiver.BootReceiver;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {

    /*on job started */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //it has no use....check it once more
        Intent service = new Intent(getApplicationContext(), BootReceiver.class);
        getApplicationContext().startService(service);
        // reschedule the job
        Util.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

}
