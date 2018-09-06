package com.stan.app.lunaandroid.colorpicker.action;

import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;

import com.stan.app.lunaandroid.util.ColorObserver;
import com.stan.app.lunaandroid.util.ObservableColor;
import com.stan.app.lunaandroid.util.Util;


public class BrightnessSeekbarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private View view;
    private ObservableColor color;
    private SeekBar seekBar;
    private boolean progressChanged = false;
    private boolean update = false;

    public BrightnessSeekbarChangeListener(View view, final ObservableColor color, final SeekBar seekBar) {
        this.view = view;
        this.color = color;
        this.seekBar = seekBar;

        color.addObserver(new ColorObserver() {
            @Override
            public void update(int c) {
                if (!progressChanged) {
                    update = true;
                    float[] hsv = {0, 0, 0};
                    Color.colorToHSV(color.get(), hsv);
                    seekBar.setProgress((int) (hsv[2] * seekBar.getMax()));
                    update = false;
                }
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!update) {
            progressChanged = true;
            float[] hsv = {0, 0, 0};
            Color.colorToHSV(color.get(), hsv);
            hsv[2] = (float) i / seekBar.getMax();
            color.set(Color.HSVToColor(hsv));
            view.setBackgroundColor(Util.getAbsColorWithBlack(color.get()));
            progressChanged = false;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
