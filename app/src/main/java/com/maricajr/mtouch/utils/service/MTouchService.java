package com.maricajr.mtouch.utils.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

import com.maricajr.mtouch.R;
import com.maricajr.mtouch.utils.SettingButton;

import static com.maricajr.mtouch.StringUtil.NAME;

public class MTouchService extends Service implements View.OnTouchListener, View.OnClickListener {

    private RelativeLayout relativeLayout;
    private ImageView overlayedButton;
    private boolean moving;
    private WindowManager windowManager;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private int mWidth;
    private static final String PREF_NAME="MTouch";
    SharedPreferences sharedPreferences;
    private SettingButton settingButton;
    private RestartService restartService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        settingButton.setEnable(false);
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        restartService = new RestartService(this);
        settingButton = new SettingButton(this);
        settingButton.setEnable(true);
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
        overlayedButton.setImageResource(R.drawable.drawble_icon);
        overlayedButton.setAlpha(0.7f);
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
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(NAME)) {
            String d = sharedPreferences.getString(NAME, "");
            restartService.restart();
        } else {
            Toast.makeText(this, "Oop!", Toast.LENGTH_SHORT).show();
        }
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

        if (settingButton.isEnable()) {
            settingButton = new SettingButton(popview, relativeLayout, this);
            relativeLayout.addView(popview, viewParams);
            settingButton.setEnable(false);
        }

        settingButton.settingButton();
        settingButton.setEnable(false);

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
