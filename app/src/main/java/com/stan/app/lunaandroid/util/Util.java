package com.stan.app.lunaandroid.util;

import android.graphics.Color;

public class Util {

    public static int getAbsColor(int color) {
        float[] hsv = {0, 0, 0};
        Color.colorToHSV(color, hsv);
        hsv[2] = 1;
        return Color.HSVToColor(hsv);
    }
}
