package com.stan.app.lunaandroid.action.triangle;

import com.stan.app.lunaandroid.util.ColorObserver;
import com.stan.app.lunaandroid.util.Util;

public class TriangleColorObserver implements ColorObserver {

    private ClickTriangleColorChangeListener parent;

    public TriangleColorObserver(ClickTriangleColorChangeListener parent) {
        this.parent = parent;
    }

    @Override
    public void update(int color) {
        if (!parent.isProgressChanged()) {
            parent.colorToTrianglePos(Util.getAbsColor(color));
        }
    }
}
