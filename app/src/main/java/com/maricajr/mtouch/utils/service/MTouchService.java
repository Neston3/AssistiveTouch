package com.maricajr.mtouch.utils.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
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
import com.maricajr.mtouch.utils.CustomView;
import com.maricajr.mtouch.utils.SettingButton;
import com.maricajr.mtouch.utils.paramsInnitializer;

import static com.maricajr.mtouch.StringUtil.NAME;

public class MTouchService extends Service implements View.OnTouchListener, View.OnClickListener {

    private RelativeLayout.LayoutParams params_imageview;
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
    private View tempoView;
    private final int btnOverlay = View.generateViewId();
    private paramsInnitializer paramsInnitializer = new paramsInnitializer();
/// find a way to get rid of tempoview
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        settingButton.setEnable(false);
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        /*initializing  restart service
         * and
         * floating settingButton classes*/
        restartService = new RestartService(this);
        settingButton = new SettingButton(this);
        settingButton.setEnable(true);
        /*
         * create
         * the
         * floating touch*/
        createMtouch();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void createMtouch() {

        /*windowmanager
         * to allow
         * drawing on the screen*/
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        /*initializing
         * imageview
         * to be displayed
         * and giving it params*/
        overlayedButton = new ImageView(this);
        overlayedButton.setImageResource(R.drawable.drawble_icon);
        //setting id for the overlay button
        overlayedButton.setId(btnOverlay);
        overlayedButton.setAlpha(0.7f);
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setOnClickListener(this);



        /*setting
         *Windowmanager params*/
//        paramsInnitializer paramsInnitializer = new paramsInnitializer();

        paramsInnitializer.setxCoordinate(0);
        paramsInnitializer.setyCoordinate(150);
        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.START | Gravity.TOP;

        /*relative layout getting the params of windowmanager*/
        relativeLayout = new RelativeLayout(this);
        params_imageview = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        params_imageview.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        /*adding view to the relative layout*/
        relativeLayout.addView(overlayedButton, params_imageview);
        /*adding
         * the
         * relative layout inside the windowmanager*/
        windowManager.addView(relativeLayout, params);

        /*get screen width */
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
        /*on clearing recent apps
         * check if the service prefernce
         * was saved then restart it*/
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(NAME)) {
            //String d = sharedPreferences.getString(NAME, "");

            /*
             * restart the service*/
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

    /*onclick
     * show the menu*/
    @Override
    public void onClick(View view) {
        showWindow();
    }

    private void showWindow() {

        /*inflate the layout*/
        final LayoutInflater inflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
////        We need to make the window mananger occupy the whole screen for this to work
////        paramsInnitializer paramsInnitializer = new paramsInnitializer();
//        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
////        but it should be noted that the window menanger layout parameters should be returned to WrapContnent
////        once the custom window is closed. So as to gain access to the screen while the mtouch is returned
////        final View popview = inflater.inflate(R.layout.custom_window_alternative, null); //the original
//        final View popview = inflater.inflate(R.layout.custom_window, null);
//
//        /*relative layout for the menu getting the params of windowmanager
//         * and adding rules*/
//        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//
////        viewParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE); // this is to put with the alternate view
//
//        viewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        viewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//
//        if (settingButton.isEnable()) {
//            settingButton = new SettingButton(popview, relativeLayout, overlayedButton, paramsInnitializer, windowManager, this);
//            /*adding the popview menu
//             * together with its
//             * viewparams
//             * to the main relative layout*/
//            relativeLayout.removeAllViewsInLayout();
//            windowManager.removeView(relativeLayout);
//            relativeLayout.addView( popview, viewParams);
//            windowManager.addView(relativeLayout, params);
////            windowManager.updateViewLayout(relativeLayout, params);
//            tempoView = popview;
//            settingButton.setEnable(false);
//            settingButton.settingButton();
//            settingButton.setEnable(false);
//
//        } else {
//            params_imageview.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            relativeLayout.removeView( tempoView);
//            settingButton.setEnable(true);
//        }

        CustomView customView = new CustomView(overlayedButton, windowManager, relativeLayout, paramsInnitializer);
        customView.Innitialize_CustomView(inflater, params_imageview, settingButton,this);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) relativeLayout.getLayoutParams();

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            //initial position
            initialX = paramsInnitializer.getxCoordinate();
            initialY = paramsInnitializer.getyCoordinate();
//

            //touch locationn
            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();

            return moving;


        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            //calculate x and y coordinate of view

            paramsInnitializer.setxCoordinate(initialX + (int) (motionEvent.getRawX() - initialTouchX));
            paramsInnitializer.setyCoordinate(initialY + (int) (motionEvent.getRawY() - initialTouchY));

            params.x = paramsInnitializer.getxCoordinate();
            params.y = paramsInnitializer.getyCoordinate();


            //update the layout
            windowManager.updateViewLayout(relativeLayout, params);

            moving = false;


        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            //position to left | right always
            int middle = mWidth / 2;
            float nearestXWall = params.x >= middle ? mWidth : 0;

            params.x = (int) nearestXWall;
            paramsInnitializer.setxCoordinate(params.x);

            windowManager.updateViewLayout(relativeLayout, params);

            return moving;

        }

        return false;

    }



}
