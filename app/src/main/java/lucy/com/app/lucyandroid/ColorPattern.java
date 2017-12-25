package lucy.com.app.lucyandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import lucy.com.app.lucyandroid.util.BTModule;

public class ColorPattern extends Fragment {

    private BTModule btModule;
    private EditText editText;

    public ColorPattern() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btModule = BTModule.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_pattern, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.editText);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onFabClick(view);
            }
        });

    }

    private void onFabClick(final View view) {
        Snackbar.make(view, "sending data...", Snackbar.LENGTH_LONG).show();
        btModule.write(editText.getText().toString() , new Runnable() {
            @Override
            public void run() {
                if (!btModule.isWriteSuccess()) {
                    Snackbar.make(view, "Could not send data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFabClick(view);
                        }
                    });
                } else {
                    Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
