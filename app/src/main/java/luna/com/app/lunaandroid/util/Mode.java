package luna.com.app.lunaandroid.util;

public enum Mode {
    RED(0),
    GEEEN(1),
    BLUE(2);

    private int numVal;

    Mode(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
