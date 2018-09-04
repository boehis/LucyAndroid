package com.stan.app.lunaandroid.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.colorpicker.action.BrightnessSeekbarChangeListener;
import com.stan.app.lunaandroid.colorpicker.action.ClickTriangleColorChangeListener;
import com.stan.app.lunaandroid.colorpicker.action.ColorSeekBarChangeListener;
import com.stan.app.lunaandroid.util.ColorPicker;
import com.stan.app.lunaandroid.util.Mode;
import com.stan.app.lunaandroid.util.ObservableColor;


public class ColorPickerFragment extends Fragment {

    private ObservableColor color = new ObservableColor();

    public ColorPickerFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if parent Fragment / Activity implements listener
        if (getParentFragment() instanceof ColorPicker || getActivity() instanceof ColorPicker) {
            if (getParentFragment() != null) {
                ((ColorPicker) getParentFragment()).pickedColor(color);
            } else {
                ((ColorPicker) getActivity()).pickedColor(color);
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ColorPicker");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_picker, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ConstraintLayout triangleBackground = view.findViewById(R.id.triangleBackground);

        final SeekBar skRed = view.findViewById(R.id.seekBarRed);
        final SeekBar skGreen = view.findViewById(R.id.seekBarGreen);
        final SeekBar skBlue = view.findViewById(R.id.seekBarBlue);
        skRed.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.RED, triangleBackground, color, skRed));
        skGreen.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.GEEEN, triangleBackground, color, skGreen));
        skBlue.setOnSeekBarChangeListener(new ColorSeekBarChangeListener(Mode.BLUE, triangleBackground, color, skBlue));

        final SeekBar skBrightness = view.findViewById(R.id.seekBarBrightness);
        skBrightness.setOnSeekBarChangeListener(new BrightnessSeekbarChangeListener(triangleBackground, color, skBrightness));

        ImageView colorTriangle = view.findViewById(R.id.color_triangle);
        colorTriangle.setOnTouchListener(new ClickTriangleColorChangeListener(triangleBackground, (Button) view.findViewById(R.id.color_selector_circle), color));
        colorTriangle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        skBrightness.setProgress(skBrightness.getMax());
    }
}
