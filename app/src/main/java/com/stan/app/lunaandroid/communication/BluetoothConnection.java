package com.stan.app.lunaandroid.communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.communication.ble.BLEScanner;
import com.stan.app.lunaandroid.dialog.CheckListDialogFragment;
import com.stan.app.lunaandroid.dialog.LoaderDialogFragment;
import com.stan.app.lunaandroid.util.PersistantData;

import java.util.ArrayList;
import java.util.List;

public class BluetoothConnection {

    private ArrayList<BluetoothDevice> connectedDevices = new ArrayList<>();

    private void connectDevices(final BluetoothAdapter btAdapter, final Activity activity, final FragmentManager fragmentManager) {
        ArrayList<ScanFilter> filters = new ArrayList<>();
        ScanCallback scanCallback;
        if(PersistantData.isConnectAutomatically(activity)){
            final LoaderDialogFragment dialog = new LoaderDialogFragment();
            dialog.onAttach(activity.getApplicationContext());
            dialog.show(fragmentManager, LoaderDialogFragment.TAG);
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    connectedDevices.add(result.getDevice());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                    for (ScanResult result: results) {
                        connectedDevices.add(result.getDevice());
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    dialog.dismiss();
                    Snackbar.make(activity.findViewById(R.id.content),R.string.connection_failed,Snackbar.LENGTH_LONG).setAction(R.string.rescan, new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            BluetoothConnection.this.connectDevices(btAdapter,activity,fragmentManager);
                        }
                    });
                }
            };
        }else {
            final CheckListDialogFragment dialog = new CheckListDialogFragment();
            dialog.onAttach(activity.getApplicationContext());
            dialog.show(fragmentManager, CheckListDialogFragment.TAG);
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    dialog.dismiss();
                    Snackbar.make(activity.findViewById(R.id.content),R.string.connection_failed,Snackbar.LENGTH_LONG).setAction(R.string.rescan, new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            BluetoothConnection.this.connectDevices(btAdapter,activity,fragmentManager);
                        }
                    });
                }
            };
        }
        BLEScanner bleScanner = new BLEScanner(btAdapter);
        bleScanner.scanLeDevice(true, filters, scanCallback);
    }
}
