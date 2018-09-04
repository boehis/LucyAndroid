package com.stan.app.lunaandroid.dialog;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.util.PersistantData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CheckListDialogFragment extends DialogFragment {

    public static String TAG = "BT Checklist Dialog";

    private NoticeDialogListener listener;
    private Button neutralButton;
    private CheckBox checkBoxAutoConnect;
    private CheckBox checkBoxAddDevices;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayList<Integer> selectedIndices = new ArrayList<>();
    private ListView btDeviceList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_checklist_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_device)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PersistantData.update(PersistantData.CONNECT_AUTOMATICALLY, checkBoxAutoConnect.isChecked(), getContext());
                        PersistantData.set(PersistantData.CURRENT_LUNAS, new HashSet<>(getSelectedItems()), getContext());
                        if (checkBoxAddDevices.isChecked()) {
                            PersistantData.update(PersistantData.CURRENT_LUNAS, new HashSet<>(getSelectedItems()), getContext());
                        }
                        listener.onDialogPositiveClick(CheckListDialogFragment.this, TAG);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(CheckListDialogFragment.this, TAG);
                    }
                })
                .setNeutralButton(R.string.scanning, null);

        // Create the AlertDialog object and return it
        onDialogCreated(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                neutralButton.setEnabled(false);
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        neutralButton.setText(R.string.rescan);
                        neutralButton.setEnabled(false);
                    }
                });
            }
        });
        return alertDialog;
    }

    private void onDialogCreated(View view) {
        final Set<String> myDevices = PersistantData.getStrings(PersistantData.MY_LUNAS, getContext());
        btDeviceList = view.findViewById(R.id.btDeviceList);
        adapter = new ArrayAdapter<>(getContext(), R.layout.select_dialog_multichoice, new ArrayList<CharSequence>());
        btDeviceList.setAdapter(adapter);
        btDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedIndices.contains(position)) {
                    selectedIndices.remove(Integer.valueOf(position));
                } else {
                    selectedIndices.add(position);
                }
                if (!selectedIndices.isEmpty()) {
                    if (myDevices.isEmpty()) {
                        checkBoxAutoConnect.setEnabled(false);
                    }
                    checkBoxAddDevices.setEnabled(false);
                } else {
                    checkBoxAutoConnect.setEnabled(true);
                    checkBoxAddDevices.setEnabled(true);
                }
            }
        });

        checkBoxAutoConnect = view.findViewById(R.id.checkBoxAutomaticallyConnect);
        checkBoxAddDevices = view.findViewById(R.id.checkBoxAddDevices);
        if (myDevices.isEmpty()) {
            checkBoxAutoConnect.setEnabled(false);
        }
        checkBoxAddDevices.setEnabled(false);
        checkBoxAutoConnect.setSelected(PersistantData.getBoolean(PersistantData.CONNECT_AUTOMATICALLY,getContext()));
    }

    private void addItems(String... items) {
        adapter.addAll(items);
        setCheckedItems();
    }

    private void addItem(String item) {
        adapter.add(item);
        //Todo: optimize don't check all elements
        setCheckedItems();
    }

    private void setCheckedItems() {
        Set<String> devices = PersistantData.getStrings(PersistantData.MY_LUNAS, getContext());
        devices.addAll(PersistantData.getStrings(PersistantData.CURRENT_LUNAS, getContext()));

        for (int i = 0; i < adapter.getCount(); i++) {
            if (devices.contains(Objects.requireNonNull(adapter.getItem(i)).toString())) {
                btDeviceList.setSelection(i);
            }
        }
    }

    public ArrayList<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public ArrayList<String> getSelectedItems() {
        ArrayList<String> items = new ArrayList<>();
        for (int i : selectedIndices) {
            items.add(Objects.requireNonNull(adapter.getItem(i)).toString());
        }
        return items;
    }
}
