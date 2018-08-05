package com.maricajr.mtouch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    public final static int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* added code
                method called to check permission
                 */
                checkDrawOverlayPermission();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean getServiceState = isMyServiceRunning();

        if (getServiceState) {

            button.setText(R.string.stop);

        } else {

            button.setText(R.string.start);

        }

    }

    private boolean isMyServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

                if (MyService.class.getName().equals(service.service.getClassName())) {

                    return true;

                }

            }
        }

        return false;

    }

    //Added methods
    public void checkDrawOverlayPermission() {
        /* check if we already  have permission to draw over other apps */
        if (android.os.Build.VERSION.SDK_INT > 22) {
            if (!Settings.canDrawOverlays(this)) {
                /* if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /* request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            }
            else {

                createRunningService();
                }
        } else {
            createRunningService();
        }
    }

    //Added method
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
    /* check if received result code
     is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE && android.os.Build.VERSION.SDK_INT > 22) {
            /* if so check once again if we have permission */
            if (Settings.canDrawOverlays(this)) {

                createRunningService();
            }
        } else {
            createRunningService();
        }
    }

    protected void createRunningService(){
        boolean getServiceState = isMyServiceRunning();

        if (getServiceState) {

            stopService(new Intent(MainActivity.this, MyService.class));

            button.setText(R.string.start);



        } else {

            startService(new Intent(MainActivity.this, MyService.class));

            button.setText(R.string.stop);



        }
    }


}
