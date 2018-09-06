package com.stan.app.lunaandroid.colorpicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.util.ColorChangeListener;
import com.stan.app.lunaandroid.util.ObservableColor;


public class SolidColorPickerFragment extends Fragment implements ColorChangeListener {

    public SolidColorPickerFragment() {
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
        return inflater.inflate(R.layout.fragment_solid_color_picker, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onColorChanged(int color) {
        Log.d("asfd", color+"");
        //Snackbar.make(view, "Bluetooth not available on this device", Snackbar.LENGTH_LONG);
/*        BTColorWriter.write(1, color.red(), color.green(), color.blue(), 0, false, new Runnable() {
            @Override
            public void run() {
                if (!BTColorWriter.isWriteSuccess()) {
                    Snackbar.make(view, "Gay", Snackbar.LENGTH_SHORT).show();
                    /*Snackbar.make(view, "Could not send data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFabClick(view);
                        }
                    });
                }
            }
        });*/
    }
}
