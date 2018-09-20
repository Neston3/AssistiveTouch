package com.maricajr.mtouch.utils.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maricajr.mtouch.MainActivity;
import com.maricajr.mtouch.R;

import java.util.List;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;
import static android.content.ContentValues.TAG;

public class MTouchService extends Service implements View.OnTouchListener, View.OnClickListener {

    private RelativeLayout relativeLayout;
    private View topLeftView;
    private ImageView overlayedButton;
    private boolean moving;
    private WindowManager windowManager;
    private Boolean enable = true;
    List<Integer> imageList;
    int imageIndex;
    private boolean active;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private int mWidth;
    private static final String PREF_NAME="MTouch";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        enable = false;
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        createMtouch();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void createMtouch() {

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);


        overlayedButton = new ImageView(this);
        overlayedButton.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        //overlayedButton.setAlpha(0.5f);
        /*overlayedButton.setMaxWidth();
        overlayedButton.setMaxHeight();*/
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setOnClickListener(this);


        int LAYOUT_PARAMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_PARAMS = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_PARAMS = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        LAYOUT_PARAMS,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params_imageview = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        relativeLayout.addView(overlayedButton, params_imageview);
        windowManager.addView(relativeLayout, params);

        getScreenWidth();

    }

    private void getScreenWidth() {
        //get screen width
        Display display=windowManager.getDefaultDisplay();
        final Point size=new Point();
        display.getSize(size);


        ViewTreeObserver vto=relativeLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = relativeLayout.getMeasuredWidth();
                mWidth = size.x - width;
            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayedButton != null) {
            windowManager.removeView(relativeLayout);
            overlayedButton = null;
        }

    }

    @Override
    public void onClick(View view) {
        showWindow();
    }

    private void showWindow() {

        final LayoutInflater inflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View popview = inflater.inflate(R.layout.custom_window, null);

        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        viewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        viewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        if (enable) {
            relativeLayout.addView(popview, viewParams);
            enable = false;
        }

        settingButton(popview);

        enable = false;

    }

    private void settingButton(final View popview) {

        ImageView btnClose = popview.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.removeView(popview);
                enable = true;
            }
        });

        ImageView btnHome = popview.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                relativeLayout.removeView(popview);
                enable = true;
            }
        });

        ImageView btnBluetooth = popview.findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBluetooth();
                relativeLayout.removeView(popview);
                enable = true;
            }
        });

        ImageView btnWifi=popview.findViewById(R.id.btnWifi);
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableWiFi();
                relativeLayout.removeView(popview);
                enable = true;

            }
        });

        ImageView btnSetting=popview.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                relativeLayout.removeView(popview);
                enable = true;
            }
        });

    }

    private void enableWiFi() {
        WifiManager wifiManager = (WifiManager)getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        assert wifiManager != null;
        boolean wifiEnabled = wifiManager.isWifiEnabled();

        if (wifiEnabled){
            wifiManager.setWifiEnabled(false);
        }else {
            wifiManager.setWifiEnabled(true);
        }
    }

    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter=getDefaultAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(this, "disabled", Toast.LENGTH_SHORT).show();
        }else {
            if (bluetoothAdapter.isEnabled()){
                bluetoothAdapter.disable();
            }else {
                bluetoothAdapter.enable();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            //initial position
            initialX = params.x;
            initialY = params.y;

            //touch locationn
            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();

            return moving;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            //calculate x and y coordinate of view

            params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
            params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);

            //update the layout
            windowManager.updateViewLayout(relativeLayout, params);

            moving = false;


        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            //position to left | right always
            int middle = mWidth / 2;
            float nearestXWall = params.x >= middle ? mWidth : 0;
            params.x = (int) nearestXWall;

            windowManager.updateViewLayout(relativeLayout, params);

            return moving;

        }

        return false;

    }



}
