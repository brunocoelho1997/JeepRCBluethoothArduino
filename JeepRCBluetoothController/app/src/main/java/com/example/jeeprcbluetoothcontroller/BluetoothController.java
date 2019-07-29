package com.example.jeeprcbluetoothcontroller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.jeeprcbluetoothcontroller.Config.MY_PERMISSIONS_REQUEST_CODE_BT;

public class BluetoothController {
    private static String TAG = "BluetoothController";
    private BluetoothAdapter bluetoothAdapter;

    //android as client
    private ConnectThread connectThread;

    //android as server
    private AcceptThread acceptThread;

    private ConnectedThread connectedThread;

    String bluetoothAddressArduino;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private boolean isBtConnected = false;

    public BluetoothController() {

    }

    public void verifyIfBluetoothIsEnabled(Context context){
        //if the bluetooth is not enabled
        if (!bluetoothAdapter.isEnabled()) {
            //Request to enable the bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)context).startActivityForResult(enableBtIntent, MY_PERMISSIONS_REQUEST_CODE_BT);
        }
    }

    public String startDiscoveringDevices(Context context){

        //verify again if the bluetooth is enabled... just for precaution
        if(!bluetoothAdapter.isEnabled())
        {
            Log.d("startDiscoringDevices", "Bluetooth isn't enabled.");
            return "Bluetooth isn't enabled.";
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                Log.d("BluetoothController", "paired device: " + bt.getName() + "\n" + bt.getAddress());

                if(bt.getName().equals("HC-06"))
                {
                    String info = bt.getAddress();
                    String address = info.substring(info.length() - 17);
                    this.bluetoothAddressArduino = address;

                    //start the connection with the bt device
                    new BluetoothConnection().execute();

                    return "Started the connection with the Jeep";

                }
            }
            return "Didn't find a paired device with name HC-06. It's necessary to pair the Android with the device. Pin: 1234";
        }

        return "No Paired Bluetooth Devices Found.";
    }

    public void turnLeft() {

        try {
            mmSocket.getOutputStream().write("0".toString().getBytes());

            Log.d(TAG, "Sent value to android");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Does not exist a connectedThread alive. Error: " + e);
        }
    }






    private class BluetoothConnection extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //progress = ProgressDialog.show(con, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (mmSocket == null || !isBtConnected)
                {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothAddressArduino);//connects to the device's address and checks if it's available
                    mmSocket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Config.RC_UUID));//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mmSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
/*
            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
        */
        }
    }

    /*
        Getters and Setters
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }
}
