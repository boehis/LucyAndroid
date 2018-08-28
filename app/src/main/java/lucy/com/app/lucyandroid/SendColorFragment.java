package lucy.com.app.lucyandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lucy.com.app.lucyandroid.util.Bluetooth.BTColorWriter;
import lucy.com.app.lucyandroid.util.ColorPicker;
import lucy.com.app.lucyandroid.util.ObservableColor;


public class SendColorFragment extends Fragment implements ColorPicker {

    private ObservableColor color;

    public SendColorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_color, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fabSend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onFabClick(view);
            }
        });
    }

    private void onFabClick(final View view) {
        Snackbar.make(view, "Sending Data...", Snackbar.LENGTH_LONG).show();
        BTColorWriter.write(1, color.red(), color.green(), color.blue(), 0, false, new Runnable() {
            @Override
            public void run() {
                if (BTColorWriter.isWriteSuccess()) {
                    Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "Gay", Snackbar.LENGTH_SHORT).show();
                    /*Snackbar.make(view, "Could not send data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFabClick(view);
                        }
                    });*/
                }
            }
        });
    }


    @Override
    public void pickedColor(ObservableColor color) {
        this.color = color;
    }
}
