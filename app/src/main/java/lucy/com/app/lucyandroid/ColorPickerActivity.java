package lucy.com.app.lucyandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lucy.com.app.lucyandroid.util.ColorPicker;
import lucy.com.app.lucyandroid.util.ObservableColor;

public class ColorPickerActivity extends AppCompatActivity implements ColorPicker{

    private ObservableColor color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
    }

    @Override
    public void pickedColor(ObservableColor color) {
        this.color = color;
    }

    public void onOkClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("COLOR_R", color.red());
        intent.putExtra("COLOR_G", color.green());
        intent.putExtra("COLOR_B", color.blue());
        setResult(RESULT_OK,intent);
        finish();
    }
}
