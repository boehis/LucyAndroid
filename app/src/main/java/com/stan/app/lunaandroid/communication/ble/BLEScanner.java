package com.stan.app.lunaandroid.communication.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;

import java.util.ArrayList;

public class BLEScanner {

    private final BluetoothLeScanner scanner;
    private final ScanSettings settings;
    private Handler handler;


    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    public BLEScanner(BluetoothAdapter bluetoothAdapter) {
        scanner = bluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        handler = new Handler();
    }

    public void scanLeDevice(final boolean enable, ArrayList<ScanFilter> filters, final ScanCallback scanCallback) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanner.stopScan(scanCallback);
                }
            }, SCAN_PERIOD);
            scanner.startScan(filters, settings, scanCallback);
        } else {
            scanner.stopScan(scanCallback);
        }
    }
}
