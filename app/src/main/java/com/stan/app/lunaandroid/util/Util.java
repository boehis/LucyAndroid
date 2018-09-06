package com.stan.app.lunaandroid.util;

import android.graphics.Color;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Util {

    public static int getAbsColor(int color) {
        float[] hsv = {0, 0, 0};
        Color.colorToHSV(color, hsv);
        hsv[2] = 1;
        return Color.HSVToColor(hsv);
    }
    public static int getAbsColorWithBlack(int color) {
        float[] hsv = {0, 0, 0};
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] != 0 ? 1:0;
        return Color.HSVToColor(hsv);
    }

    public static Set<String> convertCollection(Collection<Integer> old) {
        Set<String> converted  = new HashSet<>();
        for (Integer element: old) {
            converted.add(element.toString());
        }
        return converted;
    }
}
