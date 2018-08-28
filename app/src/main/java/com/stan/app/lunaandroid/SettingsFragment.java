package com.stan.app.lunaandroid;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stan.app.lunaandroid.util.Bluetooth.BTModule;


public class SettingsFragment extends Fragment {

    private Spinner devices;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        devices = view.findViewById(R.id.spinnerDevices);

        ArrayAdapter<String> adp = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, BTModule.getDeviceNames());
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devices.setAdapter(adp);
        devices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BTModule.setActiveDevice(BTModule.getPairedDevices().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
