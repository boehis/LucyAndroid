package lucy.com.app.lucyandroid.action.triangle;

import lucy.com.app.lucyandroid.util.ColorObserver;
import lucy.com.app.lucyandroid.util.Util;

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
