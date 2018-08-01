package com.maricajr.mtouch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean getServiceState = isMyServiceRunning();

                if (getServiceState) {

                    stopService(new Intent(MainActivity.this, MyService.class));

                    button.setText("start");



                } else {

                    startService(new Intent(MainActivity.this, MyService.class));

                    button.setText("stop");



                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean getServiceState = isMyServiceRunning();

        if (getServiceState) {

            button.setText("stop");

        } else {

            button.setText("start");

        }

    }

    private boolean isMyServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (MyService.class.getName().equals(service.service.getClassName())) {

                return true;

            }

        }

        return false;

    }

}
