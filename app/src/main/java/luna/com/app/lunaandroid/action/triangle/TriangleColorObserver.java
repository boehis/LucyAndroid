package luna.com.app.lunaandroid.action.triangle;

import luna.com.app.lunaandroid.util.ColorObserver;
import luna.com.app.lunaandroid.util.Util;

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
