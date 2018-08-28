package luna.com.app.lunaandroid.util;

import android.graphics.Color;

import java.util.ArrayList;


public class ObservableColor {

    private ArrayList<ColorObserver> observers = new ArrayList<>();
    private int color = Color.BLACK;


    public void addObserver(ColorObserver colorObserver) {
        observers.add(colorObserver);
    }

    public void removeObserver(ColorObserver colorObserver) {
        observers.remove(colorObserver);
    }

    public void set(int color) {
        this.color = color;
        for (ColorObserver observer : observers) {
            observer.update(color);
        }
    }

    public void updatePreserveBrightness(int val, Mode mode) {
        float[] hsv = {0, 0, 0};
        Color.colorToHSV(color, hsv);
        update(Math.round(val * hsv[2]), mode);
        float[] hsvPost = {0, 0, 0};
        Color.colorToHSV(color, hsvPost);
        hsvPost[2] = hsv[2];
        color = Color.HSVToColor(hsvPost);
    }

    public void update(int val, Mode mode) {
        switch (mode.getNumVal()) {
            case 0:
                set(Color.rgb(val, green(), blue()));
                break;
            case 1:
                set(Color.rgb(red(), val, blue()));
                break;
            case 2:
                set(Color.rgb(red(), green(), val));
                break;
            default:
        }
    }

    public int red() {
        return Color.red(color);
    }

    public int green() {
        return Color.green(color);
    }

    public int blue() {
        return Color.blue(color);
    }

    public int get() {
        return color;
    }

    public int get(Mode mode) {
        switch (mode.getNumVal()) {
            case 0:
                return red();
            case 1:
                return green();
            case 2:
                return blue();
            default:
                return 0;
        }
    }
}
