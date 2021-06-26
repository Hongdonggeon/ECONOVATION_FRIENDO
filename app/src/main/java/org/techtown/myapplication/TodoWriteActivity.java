package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class TodoWriteActivity extends AppCompatActivity {
    TextView textViewTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_write);

        textViewTest = findViewById(R.id.textViewTest);

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(getApplicationContext(), PopupAlarmActivity.class);
                    startActivityForResult(intent,101);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==102) {
            int hour = data.getIntExtra("hour",0);
            int minute = data.getIntExtra("minute",0);
            textViewTest.setText(hour + ":" + minute);
        }
    }
}