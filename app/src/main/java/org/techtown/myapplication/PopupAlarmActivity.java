package org.techtown.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PopupAlarmActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_alarm);
        TimePicker timePicker = findViewById(R.id.timePicker);

        CustomAdapter customAdapter = new CustomAdapter();


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                int position = intent.getIntExtra("position", 0);
                intent.putExtra("position", position);

                intent.putExtra("hour",timePicker.getHour());
                intent.putExtra("minute",timePicker.getMinute());
                setResult(102,intent);
                finish();
            }
        });
    }
}