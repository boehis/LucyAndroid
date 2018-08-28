package luna.com.app.lunaandroid.util.Bluetooth;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class BTColorWriter {

    private static boolean writeSuccess;

    public static void write(int mode, int red, int green, int blue, int delya, boolean invert, Runnable callback) {
        String msg = intTo3DigitString(mode) + "-" +
                intTo3DigitString(red) + "-" +
                intTo3DigitString(green) + "-" +
                intTo3DigitString(blue) + "-" +
                intTo3DigitString(delya) + "-" +
                intTo3DigitString(invert ? 1 : 0);

        write(msg, callback);
    }

    private static void write(final String s, final Runnable callback) {
        writeSuccess = BTModule.write(s);
        if(!writeSuccess) {
            Log.d("DEBUG", "Could not write!");
            BTModule.connect(new Runnable() {
                @Override
                public void run() {
                    writeSuccess = BTModule.write(s);
                    callback.run();
                }
            });
        }else {
            callback.run();
        }
    }

    @NonNull
    private static String intTo3DigitString(int c) {
        String s = "00" + String.valueOf(c);
        return s.substring(s.length() - 3, s.length());
    }

    public static boolean isWriteSuccess() {
        return writeSuccess;
    }
}
