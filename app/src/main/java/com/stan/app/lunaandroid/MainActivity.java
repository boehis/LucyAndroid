package com.stan.app.lunaandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stan.app.lunaandroid.colorpicker.SolidColorPickerFragment;
import com.stan.app.lunaandroid.communication.ble.BLEConnector;
import com.stan.app.lunaandroid.communication.ble.BLEScanner;
import com.stan.app.lunaandroid.dialog.CheckListDialogFragment;
import com.stan.app.lunaandroid.dialog.LoaderDialogFragment;
import com.stan.app.lunaandroid.dialog.NoticeDialogListener;
import com.stan.app.lunaandroid.util.PersistantData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoticeDialogListener {

    public final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter bluetoothAdapter;
    private SolidColorPickerFragment solidColorPickerFragment = new SolidColorPickerFragment();
    private ColorPatternFragment colorPatternFragment = new ColorPatternFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, solidColorPickerFragment).commit();

        //initBT();
        LoaderDialogFragment loaderDialogFragment = new LoaderDialogFragment();
        loaderDialogFragment.onAttach((Context)this);
        loaderDialogFragment.show(getSupportFragmentManager(), LoaderDialogFragment.TAG);

        CheckListDialogFragment dialog = new CheckListDialogFragment();
        dialog.onAttach((Context) this);
        dialog.show(getSupportFragmentManager(), CheckListDialogFragment.TAG);
    }


    //Todo: handle app focus changes

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initBT() {
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else {
            connectToDevice();
        }
    }
    private void connectToDevice() {
        /* Todo:
        0. get 'my device list'
        1. start scan
        2. if my device list not empty
            a. show dialog 'connecting to 'my' devices; cancellable
            b. scan time ~ ex. 3sec (stop if all 'my' devices were found
            c. once one of 'my' devices was found
                i. continue scan in background with low energy consumtion for rest of 'my' devices
                ii. toast once new device was detected with option to open 'available devices dialog'
        3. if non of 'my' devices are available or cancelled scan
            a. show dialog with scan results (still scanning)
                i. checklist with devices
                ii. check option 'automatically connect to my devices'
                iii. button 'ok'
                vi. button 'rescan' --> disabled while scanning, with loading icon when scanning
                v. check option 'add to my devices' (only enabled once found device checkt and not in my device list
        4. direct acces to dialog always to pick connected devices


         */
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String tag) {
        if(tag.equals(CheckListDialogFragment.TAG)){

        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, String tag) {
        if(tag.equals(CheckListDialogFragment.TAG)){

        }else if(tag.equals(LoaderDialogFragment.TAG)){
            CheckListDialogFragment newDialog = new CheckListDialogFragment();
            newDialog.onAttach(getApplicationContext());
            newDialog.show(getSupportFragmentManager(), CheckListDialogFragment.TAG);
        }
    }

    @Override
    public void onDialogDismissed(DialogFragment dialog, String tag) {
        if(tag.equals(CheckListDialogFragment.TAG)){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                bluetoothAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Toast.makeText(this,"BT doesn't work properly", Toast.LENGTH_LONG).show();
                }else {
                    connectToDevice();
                }
            }else {
                Toast.makeText(this,"BT must be enabled for the app to work properly", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_color_picker) {
            fragment = solidColorPickerFragment;
        } else if (id == R.id.nav_color_pattern) {
            fragment = colorPatternFragment;
        } else if (id == R.id.nav_settings) {
            fragment = settingsFragment;
        } else {
            fragment = solidColorPickerFragment;
        }/*else if (id == R.id.nav_my_colors) {

        }*/



        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

