package com.stan.app.lunaandroid.colorpicker.action;

import android.view.View;
import android.widget.SeekBar;

import com.stan.app.lunaandroid.util.ColorObserver;
import com.stan.app.lunaandroid.util.Mode;
import com.stan.app.lunaandroid.util.ObservableColor;
import com.stan.app.lunaandroid.util.Util;

public class ColorSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private Mode mode;
    private View view;
    private ObservableColor color;
    private boolean progressChanged = false;
    private boolean update = false;

    public ColorSeekBarChangeListener(final Mode mode, View view, final ObservableColor color, final SeekBar seekBar) {
        this.mode = mode;
        this.view = view;
        this.color = color;

        this.color.addObserver(new ColorObserver() {
            @Override
            public void update(int c) {
                if (!progressChanged) {
                    update = true;
                    seekBar.setProgress(color.get(mode));
                    update = false;
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!update) {
            progressChanged = true;
            color.update(i, mode);
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
