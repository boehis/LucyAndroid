package lucy.com.app.lucyandroid.action;

import android.view.View;
import android.widget.SeekBar;

import lucy.com.app.lucyandroid.util.ColorObserver;
import lucy.com.app.lucyandroid.util.Mode;
import lucy.com.app.lucyandroid.util.ObservableColor;

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
        if(!update){
            progressChanged = true;
            color.update(i, mode);
            view.setBackgroundColor(color.get());
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
