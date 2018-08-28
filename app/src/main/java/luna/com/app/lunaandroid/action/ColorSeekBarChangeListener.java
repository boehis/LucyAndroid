package luna.com.app.lunaandroid.action;

import android.view.View;
import android.widget.SeekBar;

import luna.com.app.lunaandroid.util.ColorObserver;
import luna.com.app.lunaandroid.util.Mode;
import luna.com.app.lunaandroid.util.ObservableColor;
import luna.com.app.lunaandroid.util.Util;

public class ColorSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private Mode mode;
    private View view;
    private ObservableColor color;
    private SeekBar seekBar;
    private boolean progressChanged = false;
    private boolean update = false;

    public ColorSeekBarChangeListener(Mode mode, View view, ObservableColor color, SeekBar seekBar) {
        this.mode = mode;
        this.view = view;
        this.color = color;
        this.seekBar = seekBar;

        this.color.addObserver(new MyColorObserver());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!update) {
            progressChanged = true;
            color.update(i, mode);
            view.setBackgroundColor(Util.getAbsColor(color.get()));
            progressChanged = false;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class MyColorObserver implements ColorObserver {
        @Override
        public void update(int c) {
            if (!progressChanged) {
                update = true;
                seekBar.setProgress(color.get(mode));
                update = false;
            }
        }
    }
}
