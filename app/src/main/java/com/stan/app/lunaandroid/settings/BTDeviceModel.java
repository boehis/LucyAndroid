package com.stan.app.lunaandroid.settings;

import android.bluetooth.BluetoothDevice;

public class BTDeviceModel {
    private String name;
    private String status;
    private int batterylevel;
    private boolean connected;
    private boolean myDevice;
    private BluetoothDevice bluetoothDevice;


    public BTDeviceModel(String name, String status, int batterylevel, boolean connected, boolean myDevice, BluetoothDevice bluetoothDevice) {
        this.name = name;
        this.status = status;
        this.batterylevel = batterylevel;
        this.connected = connected;
        this.myDevice = myDevice;
        this.bluetoothDevice = bluetoothDevice;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isMyDevice() {
        return myDevice;
    }

    public void setMyDevice(boolean myDevice) {
        this.myDevice = myDevice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBatterylevel() {
        return batterylevel;
    }

    public void setBatterylevel(int batterylevel) {
        this.batterylevel = batterylevel;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
