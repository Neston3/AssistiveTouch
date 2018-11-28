package com.maricajr.mtouch.utils;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.WindowManager;

public class paramsInnitializer {
// the x and y coordinates of the mtouch should be traced here.... so as to share with the whole app
//    and to capitalize on that when is needed to update the view, since once the view is update the
//    x and y coordinates are reset and the param.x and param.y in motion touch are neglected.
//    hence the change in param. and param.y should happen here

    public paramsInnitializer() {

    }
    private int xCoordinate;
    private int yCoordinate;

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    //    try to make this go to the params innitializer
    public WindowManager.LayoutParams wmInnitializer(int width, int height){
        int LAYOUT_PARAMS;
        /*The type of windowmanager to be used
         * depending on the
         * android version*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_PARAMS = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_PARAMS = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        width,
                        height,
                        LAYOUT_PARAMS,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        // setting the mtouch button on top left
        params.x = this.xCoordinate;
        params.y = this.yCoordinate;
        return params;
    }

}
