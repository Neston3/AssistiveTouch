package com.maricajr.mtouch;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maricajr.mtouch.utils.service.MTouchService;

import static com.maricajr.mtouch.StringUtil.NAME;
import static com.maricajr.mtouch.StringUtil.PREF_NAME;
import static com.maricajr.mtouch.StringUtil.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDrawOverlayPermission();

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(MainActivity.this)) {
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
            Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
        }

        boolean getServiceState = isMyServiceRunning();
        if (getServiceState) {
            button.setText("OFF");
        } else {
            button.setText("ON");
        }

    }

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

    //Added method
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        if (requestCode == REQUEST_CODE) {
            //not set
            if (!Settings.canDrawOverlays(this)) {
                finish();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void createRunningService(){
        boolean getServiceState = isMyServiceRunning();

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


    public void howToUSe(View view) {
    }

    public void feedback(View view) {
    }

    public void about(View view) {
    }
}
