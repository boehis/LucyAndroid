package com.stan.app.lunaandroid.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stan.app.lunaandroid.R;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {

    /*
    list of
        1. my connected lunas
        2. connected lunas
        3. my lunas
    with
        name (all)
        Status
        battery % (1,2)
        [...] (1&2: disconnect, 2: add to my lunas, 3:remove from my lunas)

     */

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

        ListView devices = view.findViewById(R.id.device_setting_list);
        ArrayList<BTDeviceModel> items = new ArrayList<>();
        items.add(new BTDeviceModel("www", "connected", 80, true, true,null));
        items.add(new BTDeviceModel("dsfwww", "connected", 19, false, true, null));
        items.add(new BTDeviceModel("dsfwww", "connected", 5,true,false, null));
        ArrayAdapter adapter = new DeviceListAdapter(getContext(), R.layout.listview_setting_list_element, items);
        devices.setAdapter(adapter);
    }
}

