package com.stan.app.lunaandroid.colorpicker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.util.ColorChangeListener;
import com.stan.app.lunaandroid.util.ColorReceiver;
import com.stan.app.lunaandroid.util.ObservableColor;

public class ColorPickerActivity extends AppCompatActivity implements ColorReceiver {

    private ObservableColor color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
    }

    public void onOkClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("COLOR_R", color.red());
        intent.putExtra("COLOR_G", color.green());
        intent.putExtra("COLOR_B", color.blue());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onReceiveColor(ObservableColor color) {
        this.color = color;
    }
}
