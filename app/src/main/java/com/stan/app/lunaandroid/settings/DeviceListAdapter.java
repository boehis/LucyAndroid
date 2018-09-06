package com.stan.app.lunaandroid.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.util.PersistantData;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

class DeviceListAdapter extends ArrayAdapter<BTDeviceModel> {

    DeviceListAdapter(Context context, int resource, List<BTDeviceModel> items) {
        super(context, resource, items);
        Collections.sort(items, new Comparator<BTDeviceModel>() {
            @Override
            public int compare(BTDeviceModel o1, BTDeviceModel o2) {
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_setting_list_element, null);
        }

        final BTDeviceModel device = getItem(position);

        if (device != null) {
            TextView deviceName = v.findViewById(R.id.settings_device_name);
            TextView deviceStatus = v.findViewById(R.id.settings_status);
            TextView powerLevelTextView = v.findViewById(R.id.settings_power_level_textView);
            ProgressBar powerLevel = v.findViewById(R.id.settings_power_level);
            final Button moreButton = v.findViewById(R.id.settings_more_button);

            deviceName.setText(device.getName());
            deviceStatus.setText(device.getStatus());
            powerLevelTextView.setText(String.valueOf(device.getBatterylevel()));
            if (device.getBatterylevel() < 20) {
                powerLevel.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.custom_progress_red));
            } else {
                powerLevel.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.custom_progress_green));
            }
            powerLevel.setProgress(device.getBatterylevel());

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(), moreButton);
                    popupMenu.getMenuInflater().inflate(R.menu.settings_popup_menu,popupMenu.getMenu());
                    final MenuItem connectDisconnectDevice = popupMenu.getMenu().findItem(R.id.meun_connect_disconnect_device);
                    final MenuItem addRemoveDevice = popupMenu.getMenu().findItem(R.id.meun_add_remove_device);

                    int myDev = device.isMyDevice() ? R.string.remove_device :R.string.add_device2;
                    addRemoveDevice.setTitle(myDev);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Todo: update list and list values
                            if (item == addRemoveDevice) {
                                if(addRemoveDevice.getTitle().equals(getContext().getString(R.string.remove_device))) {
                                    PersistantData.removeFromCurrentLunas(device.getName());
                                }else {
                                    HashSet<String> values = new HashSet<>();
                                    values.add(device.getName());
                                    PersistantData.addToCurrentLunas(values);
                                }
                            } else if(item == connectDisconnectDevice){
                                PersistantData.removeFromCurrentLunas(device.getName());
                                //Todo: Disconnect device
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });

        }

        return v;
    }

}
