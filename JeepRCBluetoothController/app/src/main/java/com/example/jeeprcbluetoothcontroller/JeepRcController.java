package com.example.jeeprcbluetoothcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Set;

import static com.example.jeeprcbluetoothcontroller.Config.MY_PERMISSIONS_REQUEST_CODE_BT;
import static com.example.jeeprcbluetoothcontroller.Config.PERMISSIONS;

public class JeepRcController {

    private boolean userHasPermissions;
    private BluetoothController bluetoothController;

    public JeepRcController() {
        this.bluetoothController = new BluetoothController();
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void requestPermissionsToUser(Context context) {
        if(!hasPermissions(context, PERMISSIONS)){
            ActivityCompat.requestPermissions((Activity)context, PERMISSIONS, Config.MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    public boolean isUserHasPermissions() {
        return userHasPermissions;
    }

    public void setUserHasPermissions(boolean userHasPermissions) {
        this.userHasPermissions = userHasPermissions;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothController.getBluetoothAdapter();
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothController.setBluetoothAdapter(bluetoothAdapter);
    }

    public void verifyIfBluetoothIsEnabled(Context context){
        bluetoothController.verifyIfBluetoothIsEnabled(context);
    }

    public boolean verifyIfConnectedWithRC(){
        return verifyIfConnectedWithRC();
    }

    public String startDiscoveringDevices(Context context){
        return bluetoothController.startDiscoveringDevices(context);
    }

    public void turnLeft(){
        bluetoothController.turnLeft();
    }


}
