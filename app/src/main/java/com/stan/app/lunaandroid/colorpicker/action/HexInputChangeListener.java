package com.stan.app.lunaandroid.colorpicker.action;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.stan.app.lunaandroid.util.ColorObserver;
import com.stan.app.lunaandroid.util.ObservableColor;

public class HexInputChangeListener implements TextWatcher {

    private View view;
    private ObservableColor color;
    private EditText editText;
    private boolean hexColorChanged = false;

    public HexInputChangeListener(View view, ObservableColor color, final EditText editText) {
        this.view = view;
        this.color = color;
        this.editText = editText;

        color.addObserver(new ColorObserver() {
            @Override
            public void update(int color) {
                if(!hexColorChanged) {
                    editText.setText(convertText(color));
                }
            }
        });

    }

    private String convertText(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return hexColor.substring(1).toUpperCase();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText.length()== 6) {
            hexColorChanged = true;
            try {
                String col = s.toString().toUpperCase();
                int color = Color.parseColor("#" + col);
                this.color.set(color);
                s.append(col,0,col.length()-1);
            } catch (NumberFormatException ignored) {
                String oldCol = convertText(color.get());
                s.append(oldCol,0,oldCol.length()-1);
            }
            hexColorChanged = false;
        }

    }
}
