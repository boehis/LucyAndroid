package com.stan.app.lunaandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.stan.app.lunaandroid.util.BTModuleOld;
import com.stan.app.lunaandroid.util.Bluetooth.BTColorWriter;
import com.stan.app.lunaandroid.util.Util;

public class ColorPatternFragment extends Fragment {

    private final int COLOR_PICKER_REQUEST_CODE = 1;
    private int color;
    private Button pickColorButton;

    private SeekBar delay;
    private Spinner mode;
    private Switch invert;

    public ColorPatternFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_pattern, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
        pickColorButton = view.findViewById(R.id.pickcolorButton);
        pickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, COLOR_PICKER_REQUEST_CODE);
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onFabClick(view);
            }
        });

        mode = view.findViewById(R.id.spinnerModes);
        delay = view.findViewById(R.id.seekBarDelay);
        invert = view.findViewById(R.id.switchInvert);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLOR_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int r = data.getIntExtra("COLOR_R", 0);
                int g = data.getIntExtra("COLOR_G", 0);
                int b = data.getIntExtra("COLOR_B", 0);
                color = Color.rgb(r, g, b);
                pickColorButton.setBackgroundColor(Util.getAbsColor(color));
            }
        }
    }

    private void onFabClick(final View view) {
        Snackbar.make(view, "sending data...", Snackbar.LENGTH_LONG).show();
        BTColorWriter.write(mode.getSelectedItemPosition() + 2,
                Color.red(color),
                Color.green(color),
                Color.blue(color),
                delay.getProgress() + 30,
                invert.isChecked(),
                new Runnable() {
                    @Override
                    public void run() {
                        if (BTColorWriter.isWriteSuccess()) {
                            Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(view, "Could not send data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onFabClick(view);
                                }
                            });
                        }
                    }
                });
    }
}
