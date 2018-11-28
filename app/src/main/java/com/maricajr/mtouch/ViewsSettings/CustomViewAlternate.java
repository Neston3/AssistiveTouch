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

public class CustomViewAlternate {
    private RelativeLayout relativeLayout;
    private com.maricajr.mtouch.utils.paramsInnitializer paramsInnitializer;
    private ImageView overlayedButton;
    private View tempoView;
    private WindowManager windowManager;


    public CustomViewAlternate( ImageView overlayedButton, WindowManager windowManager, RelativeLayout relativeLayout, com.maricajr.mtouch.utils.paramsInnitializer paramsInnitializer){
        this.overlayedButton = overlayedButton;
        this.windowManager = windowManager;
        this.paramsInnitializer = paramsInnitializer;
        this.relativeLayout = relativeLayout;
    }

    public void Innitialize_CustomView(LayoutInflater inflater, RelativeLayout.LayoutParams params_imageview, SettingButton settingButton, Context context){


        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final View popview = inflater.inflate(R.layout.custom_window_alternative, null);

        /*relative layout for the menu getting the params of windowmanager
         * and adding rules*/
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        viewParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE); // this is to put with the alternate view

//        viewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        viewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        if (settingButton.isEnable()) {
            settingButton = new SettingButton(popview, relativeLayout, overlayedButton, paramsInnitializer, windowManager, context);
            /*adding the popview menu
             * together with its
             * viewparams
             * to the main relative layout*/
            relativeLayout.removeAllViewsInLayout();
//            windowManager.removeView(relativeLayout); //this is used in custom view

            relativeLayout.addView( popview, viewParams);
            params.gravity = Gravity.START;
//            windowManager.addView(relativeLayout, params); // same! used in the custom view
            windowManager.updateViewLayout(relativeLayout, params);
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

    //removing the  custom alternative view after using the services in setting Button

    public void removeCustomeView(){
        RelativeLayout.LayoutParams params_imageview = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        params_imageview.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        WindowManager.LayoutParams params = paramsInnitializer.wmInnitializer(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.START;
        windowManager.updateViewLayout(relativeLayout, params);
//        windowManager.removeView(relativeLayout);
        relativeLayout.addView(overlayedButton, params_imageview);
//        windowManager.addView(relativeLayout, params);

    }

}
