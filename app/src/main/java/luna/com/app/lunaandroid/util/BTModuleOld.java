package luna.com.app.lunaandroid.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BTModuleOld {

    private static BTModuleOld btModule = null;
    private static Activity activity;

    public static BTModuleOld getInstance(Activity mainActivity) {
        if (btModule == null) {
            btModule = new BTModuleOld();
            activity = mainActivity;
        }
        return btModule;
    }

    private Thread initThread;
    private Runnable initCallback = null;
    private boolean initSuccess;

    private Runnable writerCallback = null;
    private boolean writeSuccess = false;

    private OutputStream outputStream;
    private InputStream inStream;

    private BTModuleOld() {
        init();
    }

    public void write(int mode, int red, int green, int blue, int delya, boolean invert, Runnable callback) {
        String msg = intTo3DigitString(mode) + "-" +
                intTo3DigitString(red) + "-" +
                intTo3DigitString(green) + "-" +
                intTo3DigitString(blue) + "-" +
                intTo3DigitString(delya) + "-" +
                intTo3DigitString(invert ? 1 : 0);
        if (callback != null) {
            write(msg, callback);
        } else {
            write(msg);
        }

    }

    public void write(String s, Runnable callback) {
        writerCallback = callback;
        write(s);
    }

    public void write(String s) {
        writeSuccess = false;
        if (initSuccess) {
            Thread writerThread = new Thread(writer(s));
            writerThread.start();
        } else {
            initCallback = writer(s);
        }
        if (!initThread.isAlive()) {
            init();
        }
    }

    @NonNull
    private String intTo3DigitString(int c) {
        String s = "00" + String.valueOf(c);
        return s.substring(s.length() - 3, s.length());
    }

    private Runnable writer(final String s) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.flush();
                    outputStream.write(s.getBytes());
                    writeSuccess = true;
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
                if (writerCallback != null) {
                    writerCallback.run();
                }
            }
        };
    }

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private Handler handler;
    private void newInit() {
        bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableBtIntent);
        }
        if(bluetoothAdapter.isEnabled()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 200);
        }

    }

    private void init() {
        initThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
                if (blueAdapter != null) {
                    if (blueAdapter.isEnabled()) {
                        Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                        if (bondedDevices.size() > 0) {
                            Object[] devices = bondedDevices.toArray();
                            BluetoothDevice device = (BluetoothDevice) devices[0];
                            ParcelUuid[] uuids = device.getUuids();
                            try {
                                connect(uuids[0].getUuid(), device);
                                initSuccess = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                                initSuccess = false;
                            }
                        }
                        Log.e("error", "No appropriate paired devices.");
                    } else {
                        Log.e("error", "Bluetooth is disabled.");
                    }
                }
                if (initCallback != null) {
                    initCallback.run();
                }
            }
        });
        initThread.start();
    }

    private int attemptCounter = 4;

    private void connect(UUID uuid, BluetoothDevice device) throws IOException {
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            outputStream = socket.getOutputStream();
            inStream = socket.getInputStream();
        } catch (IOException e) {
            if (attemptCounter < 0) {
                connect(uuid, device);
            } else {
                throw e;
            }
            attemptCounter--;
        }
        attemptCounter = 4;
    }

    public boolean isWriteSuccess() {
        return writeSuccess;
    }
}
