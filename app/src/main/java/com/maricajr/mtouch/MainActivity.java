package com.maricajr.mtouch;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.maricajr.mtouch.utils.service.MTouchService;

import java.util.Objects;

import static com.maricajr.mtouch.StringUtil.NAME;
import static com.maricajr.mtouch.StringUtil.PREF_NAME;
import static com.maricajr.mtouch.StringUtil.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog=new Dialog(this);

        /*checking floating or overlay permission*/
        checkDrawOverlayPermission();

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(MainActivity.this)) {
                    /*create a lifetime running service*/
                    createRunningService();
                } else {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.contains(NAME)){
            String d=sharedPreferences.getString(NAME,"");
            //Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
        }

        /* check if the service is on or off on resume and change the text on the button*/
        boolean getServiceState = isMyServiceRunning();
        if (getServiceState) {
            button.setText("OFF");
            startService(new Intent(MainActivity.this, MTouchService.class));
        } else {
            button.setText("ON");
            stopService(new Intent(MainActivity.this, MTouchService.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*check the state of the floating window if on or off
         * and
         * restart the service if off
         * unless its already off
         * */
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(NAME)) {
            String d = sharedPreferences.getString(NAME, "");
            restart();
            //Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
        }

    }

    public void restart() {
        /*get the intent to be restarted */
        Intent restartServiceIntent = new Intent(this,
                MTouchService.class);
        restartServiceIntent.setPackage(String.valueOf(getApplicationContext()));

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        /*Alam manager to restart the service
         * whenever it is closed*/
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

    }

    /*check if my service is running
     * */
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MTouchService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /*floating/overlayed permission request
     * */
    public void checkDrawOverlayPermission() {
        /* check if we already  have permission to draw over other apps */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /* if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                /* request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);

            }
        }
        // createRunningService();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        /*request permission if granted or not*/
        if (requestCode == REQUEST_CODE) {
            //not set
            if (!Settings.canDrawOverlays(this)) {
                finish();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("ResourceAsColor")
    protected void createRunningService(){
        boolean getServiceState = isMyServiceRunning();
        /*check the state of the service if running or not
         * */
        if (getServiceState) {
            stopService(new Intent(MainActivity.this, MTouchService.class));
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(NAME,"off");
            editor.apply();
            button.setText("ON");
        } else {
            startService(new Intent(MainActivity.this, MTouchService.class));
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(NAME,"on");
            editor.apply();
            button.setText("OFF");
        }
    }

    public void feedback(View view) {
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.feed_back);
        dialog.show();
    }

    public void aboutus(View view) {
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.about_us);
        dialog.show();
    }

    public void howToUse(View view) {
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.how_to_use_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void close_pop(View view) {
        dialog.dismiss();
    }

    public void changeIcon(View view) {

    }

}
