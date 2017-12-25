package lucy.com.app.lucyandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import lucy.com.app.lucyandroid.action.BrightnessSeekbarChangeListener;
import lucy.com.app.lucyandroid.action.ColorSeekBarChangeListener;
import lucy.com.app.lucyandroid.util.BTModule;
import lucy.com.app.lucyandroid.util.Mode;
import lucy.com.app.lucyandroid.util.ObservableColor;


public class ColorPickerFragment extends Fragment {

    private ObservableColor color = new ObservableColor();
    private BTModule btModule;

    public ColorPickerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btModule = BTModule.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fabSend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onFabClick(view);
            }
        });

        final ConstraintLayout triangleBackground = view.findViewById(R.id.triangleBackground);

        final SeekBar skRed = view.findViewById(R.id.seekBarRed);
        final SeekBar skGreen = view.findViewById(R.id.seekBarGreen);
        final SeekBar skBlue = view.findViewById(R.id.seekBarBlue);
        skRed.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.RED, triangleBackground, color, skRed));
        skGreen.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.GEEEN, triangleBackground, color, skGreen));
        skBlue.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.BLUE, triangleBackground, color, skBlue));

        final SeekBar skBrightness = view.findViewById(R.id.seekBarBrightness);
        skBrightness.setOnSeekBarChangeListener(new BrightnessSeekbarChangeListener(triangleBackground, color, skBrightness));
    }

    private void onFabClick(final View view) {
        Snackbar.make(view, "Sending Data...", Snackbar.LENGTH_LONG).show();
        btModule.write("002/050/001/" + colorString(color.red()) + "/" + colorString(color.blue()) + "/" + colorString(color.green()), new Runnable() {
            @Override
            public void run() {
                if (!btModule.isWriteSuccess()) {
                    Snackbar.make(view, "Could not send data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFabClick(view);
                        }
                    }).show();
                } else {
                    Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String colorString(int c) {
        String s = "000" + String.valueOf(c);
        return s.substring(s.length() - 4, s.length() - 1);
    }

}
