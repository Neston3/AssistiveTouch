package com.maricajr.mtouch.ViewsSettings;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maricajr.mtouch.R;
import com.maricajr.mtouch.utils.SettingButton;
import com.maricajr.mtouch.utils.paramsInnitializer;


public class CustomView {

    private RelativeLayout relativeLayout;
    private com.maricajr.mtouch.utils.paramsInnitializer paramsInnitializer;
    private ImageView overlayedButton;
    private View tempoView;
    private WindowManager windowManager;


    public CustomView( ImageView overlayedButton, WindowManager windowManager, RelativeLayout relativeLayout, paramsInnitializer paramsInnitializer){
        this.overlayedButton = overlayedButton;
        this.windowManager = windowManager;
        this.paramsInnitializer = paramsInnitializer;
        this.relativeLayout = relativeLayout;
    }


    public void Innitialize_CustomView(LayoutInflater inflater, RelativeLayout.LayoutParams params_imageview, SettingButton settingButton, Context context){

//        We need to make the window mananger occupy the whole screen for this to work
//        paramsInnitializer paramsInnitializer = new paramsInnitializer();

        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        but it should be noted that the window menanger layout parameters should be returned to WrapContnent
//        once the custom window is closed. So as to gain access to the screen while the mtouch is returned
//        final View popview = inflater.inflate(R.layout.custom_window_alternative, null);
        final View popview = inflater.inflate(R.layout.custom_window, null);

        /*relative layout for the menu getting the params of windowmanager
         * and adding rules*/
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

//        viewParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE); // this is to put with the alternate view

        viewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        viewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        if (settingButton.isEnable()) {
            settingButton = new SettingButton(popview, relativeLayout, overlayedButton, paramsInnitializer, windowManager, context);
            /*adding the popview menu
             * together with its
             * viewparams
             * to the main relative layout*/
            relativeLayout.removeAllViewsInLayout();
            windowManager.removeView(relativeLayout);
            relativeLayout.addView( popview, viewParams);
            windowManager.addView(relativeLayout, params);
//            windowManager.updateViewLayout(relativeLayout, params);
            tempoView = popview;
            settingButton.setEnable(false);
            settingButton.settingButton();
            settingButton.setEnable(false);

        } else {
            params_imageview.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayout.removeView( tempoView);
            settingButton.setEnable(true);
        }
    }

    //removing the  custom view after using the services in setting Button

    public void removeCustomeView(){
        RelativeLayout.LayoutParams params_imageview = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        params_imageview.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.START;
//                windowManager.updateViewLayout(relativeLayout, params);
        windowManager.removeView(relativeLayout);
        relativeLayout.addView(overlayedButton, params_imageview);
        windowManager.addView(relativeLayout, params);

    }
}
