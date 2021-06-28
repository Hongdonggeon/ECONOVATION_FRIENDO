package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TodoWriteActivity extends AppCompatActivity {
    TextView textView;
    TextView textViewTest;
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    CheckBox checkBox;

    int hour;
    int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_write);

        textViewTest = findViewById(R.id.textViewTest);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        editText = findViewById(R.id.editText);

        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        int month = intent.getIntExtra("month",0);
        int dayOfMonth = intent.getIntExtra("dayOfMonth",0);
        textView.setText((month+1) + "월" + " "+ dayOfMonth + "일");


        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(getApplicationContext(), PopupAlarmActivity.class);
                    startActivityForResult(intent,101);
                } else if(!isChecked){
                    hour = 0;
                    minute = 0;
                    textViewTest.setText("지정 안됨");
                }
            }
        });

        CustomAdapter customAdapter = new CustomAdapter();
        customAdapter.addItem(new Todo(1,"테스트"));

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkBox);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoContent = editText.getText().toString();
                customAdapter.addItem(new Todo(2,todoContent));
                editText.setText(null);
                Toast.makeText(getApplicationContext(),editText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==102) {
            hour = data.getIntExtra("hour",0);
            minute = data.getIntExtra("minute",0);
            if(hour < 10 && minute < 10) {
                textViewTest.setText("0" + hour + ":" + "0" + minute);
            } else if (hour < 10 && minute > 10){
                textViewTest.setText("0" + hour + ":" + minute);
            } else if (hour > 10 && minute < 10){
                textViewTest.setText(hour + ":" + "0" + minute);
            } else {
                textViewTest.setText(hour + ":" + minute);
            }
        }
    }
}