package com.maricajr.mtouch.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maricajr.mtouch.MainActivity;
import com.maricajr.mtouch.R;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;

/*menu button class*/
public class SettingButton {
    private View popview;
    private RelativeLayout relativeLayout;
    private Context context;
    private boolean enable;

    public SettingButton(View popview, RelativeLayout relativeLayout, Context context) {
        this.popview = popview;
        this.relativeLayout = relativeLayout;
        this.context = context;
    }

    public SettingButton(Context context) {
        this.context = context;
    }

    public void settingButton() {

        /*each button with
         * its
         * functionality*/
        ImageView btnClose = popview.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.removeView(popview);
                setEnable(true);
            }
        });

        ImageView btnHome = popview.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                relativeLayout.removeView(popview);
                setEnable(true);
            }
        });

        ImageView btnBluetooth = popview.findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBluetooth();
                relativeLayout.removeView(popview);
                setEnable(true);
            }
        });

        ImageView btnWifi = popview.findViewById(R.id.btnWifi);
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableWiFi();
                relativeLayout.removeView(popview);
                setEnable(true);
            }
        });

        ImageView btnSetting = popview.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                relativeLayout.removeView(popview);
                setEnable(true);
            }
        });

    }

    private void enableWiFi() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        assert wifiManager != null;
        boolean wifiEnabled = wifiManager.isWifiEnabled();

        if (wifiEnabled) {
            Toast.makeText(context, "Wi-Fi disabled", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(false);
        } else {
            Toast.makeText(context, "Wi-Fi enabled", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
    }

    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context.getApplicationContext(), "disabled", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Toast.makeText(context, "Bluetooth disable", Toast.LENGTH_SHORT).show();
                bluetoothAdapter.disable();
            } else {
                Toast.makeText(context, "Bluetooth enaled", Toast.LENGTH_SHORT).show();
                bluetoothAdapter.enable();
            }
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
