package com.stan.app.lunaandroid.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.colorpicker.action.BrightnessSeekbarChangeListener;
import com.stan.app.lunaandroid.colorpicker.action.ClickTriangleColorChangeListener;
import com.stan.app.lunaandroid.colorpicker.action.ColorSeekBarChangeListener;
import com.stan.app.lunaandroid.colorpicker.action.HexInputChangeListener;
import com.stan.app.lunaandroid.util.ColorChangeListener;
import com.stan.app.lunaandroid.util.ColorObserver;
import com.stan.app.lunaandroid.util.ColorReceiver;
import com.stan.app.lunaandroid.util.Mode;
import com.stan.app.lunaandroid.util.ObservableColor;
import com.stan.app.lunaandroid.util.PersistantData;
import com.stan.app.lunaandroid.util.Util;

import static android.content.ContentValues.TAG;


public class ColorPickerFragment extends Fragment {

    private ObservableColor color = new ObservableColor();
    private ColorChangeListener listener;
    private boolean isPaused = false;

    public ColorPickerFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if parent Fragment / Activity implements listener
        if (getParentFragment() instanceof ColorReceiver || getActivity() instanceof ColorReceiver) {
            if (getParentFragment() != null) {
                ((ColorReceiver) getParentFragment()).onReceiveColor(color);
            } else {
                ((ColorReceiver) getActivity()).onReceiveColor(color);
            }
        }
    }

    public void setListener(ColorChangeListener listener) {
        this.listener = listener;
    }

    public ObservableColor getColor() {
        return color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_picker, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        color.addObserver(new ColorObserver() {
            @Override
            public void update(int color) {
                PersistantData.setLastPickedColor(color);
                if (listener != null) listener.onColorChanged(color);
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

        ImageView colorTriangle = view.findViewById(R.id.color_triangle);
        Button colorSelector = view.findViewById(R.id.color_selector_circle);
        colorTriangle.setOnTouchListener(new ClickTriangleColorChangeListener(triangleBackground, colorSelector, color));
        colorTriangle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        final EditText hexText = view.findViewById(R.id.hex_color_input);
        hexText.setCursorVisible(false);
        hexText.addTextChangedListener(new HexInputChangeListener(triangleBackground, color, hexText));
        hexText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hexText.setCursorVisible(true);
                hexText.setSelection(hexText.length());
            }
        });
        hexText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hexText.setCursorVisible(false);
                return false;
            }
        });
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.findViewById(R.id.selector_holder).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                hexText.setCursorVisible(false);
                return false;
            }
        });


        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                color.set(PersistantData.getLastPickedColor(getContext()));
                triangleBackground.setBackgroundColor(Util.getAbsColorWithBlack(color.get()));
            }
        });
    }
}
