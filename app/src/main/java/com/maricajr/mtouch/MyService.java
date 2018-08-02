package com.maricajr.mtouch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.circularlist.CircularListView;
import com.jh.circularlist.CircularTouchListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyService extends Service implements View.OnTouchListener, View.OnClickListener {

    private View topLeftView;

    private ImageView overlayedButton;
    private boolean moving;
    private WindowManager windowManager;
    private Boolean enable=true;
    private ArrayList<String> itemTitles;
    List<Integer> imageList;
    int imageIndex;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        enable=false;
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        /*LayoutInflater layoutInflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_window,null);*/

        overlayedButton = new ImageView(this);
        overlayedButton.setImageResource(R.mipmap.float_widget);
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setOnClickListener(this);


        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE | WindowManager.LayoutParams.TYPE_TOAST,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        windowManager.addView(overlayedButton, params);


        topLeftView = new View(this);

        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams
                (WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        topLeftParams.gravity = Gravity.START | Gravity.TOP;

        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        windowManager.addView(topLeftView, topLeftParams);

       // setImageList(ImageAssetsSource.getImageAsset());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayedButton != null) {

            windowManager.removeView(overlayedButton);
            windowManager.removeView(topLeftView);
            topLeftView=null;
            overlayedButton = null;

        }

    }


    @Override
    public void onClick(View view) {
//        if (view.isClickable()){
//            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
//        }

        showWindow();
    }

    private void showWindow() {

        LayoutInflater inflater= (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View popview=inflater.inflate(R.layout.custom_window,null);

        final PopupWindow popupWindow=new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.update();

        if (enable){
            popupWindow.showAsDropDown(overlayedButton,50,-30);
            Toast.makeText(this, "Show", Toast.LENGTH_SHORT).show();
            enable=false;
        }

        Button button=popview.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                enable=true;
            }
        });

        enable=false;


//        ArrayList<String> itemTitles = new ArrayList<>();
//        for(int i = 0 ; i < 6 ; i ++){
//            itemTitles.add(String.valueOf(i));
//        }
//
//        CircularListView circularListView = popview.findViewById(R.id.my_circular_list_1);
//        CircularItemAdapter adapter = new CircularItemAdapter(itemTitles,inflater);
//        circularListView.setOnItemClickListener(new CircularTouchListener.CircularItemClickListener() {
//            @Override
//            public void onItemClick(View view, int index) {
//            }
//        });
//
//
//
//        View views = inflater.inflate(R.layout.view_circular_item, null);
//        TextView itemView = (TextView) views.findViewById(R.id.bt_item);
//        itemView.setText(String.valueOf(adapter.getCount() + 1));*//*
//        circularListView.setAdapter(adapter);
//        adapter.addItem(popview);*/
//
//       //windowManager.addView(popview,params);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) overlayedButton.getLayoutParams();

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            float x = motionEvent.getRawX();
            float y = motionEvent.getRawY();

            moving = false;

            int[] location = new int[2];
            overlayedButton.getLocationOnScreen(location);
            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            float x = motionEvent.getRawX();
            float y = motionEvent.getRawY();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) <=0 && Math.abs(newY - originalYPos) <=0 && !moving) {

                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            windowManager.updateViewLayout(overlayedButton, params);

            moving = true;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            return moving;

        }

        return false;

    }

    public void setImageList(List<Integer> imageList) {
        this.imageList = imageList;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

}
