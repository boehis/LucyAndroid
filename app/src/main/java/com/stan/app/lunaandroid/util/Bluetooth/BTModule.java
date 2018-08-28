package com.stan.app.lunaandroid.util.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BTModule {

    private static BluetoothDevice activeDevice;
    private static ArrayList<BluetoothDevice> pairedDevices;
    private static List<String> deviceNames;
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothSocket btSocket;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static OutputStream outputStream;

    public static void init(Activity activity) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Bluetooth not available", Toast.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivity(enableBtIntent);
            }
        }

        deviceNames = new ArrayList<>();
        pairedDevices = new ArrayList<>();
        for (BluetoothDevice bluetoothDevice : bluetoothAdapter.getBondedDevices()) {
            pairedDevices.add(bluetoothDevice);
            deviceNames.add(bluetoothDevice.getName());
        }
        if (deviceNames.isEmpty()) {
            deviceNames.add("No Devices paired");
        } else {
            setActiveDevice(pairedDevices.get(0));
        }

    }

    public static BluetoothDevice getActiveDevice() {
        return activeDevice;
    }

    public static void setActiveDevice(final BluetoothDevice activeDevice) {
        if (BTModule.activeDevice != activeDevice) {
            BTModule.activeDevice = activeDevice;
            connect(null);
        }
    }

    private static boolean isConnecting = false;

    public static void connect(final Runnable callback) {
        if (!isConnecting) {
            isConnecting = true;
            Thread connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    closeAll();

                    try {
                        btSocket = BTModule.activeDevice.createRfcommSocketToServiceRecord(activeDevice.getUuids()[0].getUuid());
                    } catch (IOException e) {
                        Log.d("DEBUG BT", "SOCKET CREATION FAILED :" + e.toString());
                        Log.d("BT SERVICE", "SOCKET CREATION FAILED, STOPPING SERVICE");
                    }
                    bluetoothAdapter.cancelDiscovery();
                    try {
                        btSocket.connect();
                        outputStream = btSocket.getOutputStream();
                    } catch (IOException e) {
                        closeAll();
                        e.printStackTrace();
                    }
                    if (callback != null) {
                        callback.run();
                    }
                }
            });
            connectThread.start();
            isConnecting = false;
        }
    }

    public static void closeAll() {
        try {
            outputStream.close();
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }

    /**
     * @param s
     * @return true if could write with open outputstream, false if writing failed --> may still succeed on reconnect
     */
    public static boolean write(final String s) {
        if (btSocket.isConnected()) {
            try {
                 outputStream.write(s.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    public static ArrayList<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }

    public static List<String> getDeviceNames() {
        return deviceNames;
    }

    public static BluetoothSocket getBtSocket() {
        return btSocket;
    }
}
