package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class TodoWriteActivity extends AppCompatActivity {

    ArrayList<Todo> items = new ArrayList<Todo>();

    TextView textView;
    TextView textViewTest;
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    CheckBox checkBox;
    CustomAdapter customAdapter;

    String todoContent;
    int hour;
    int minute;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_write);


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        editText = findViewById(R.id.editText);

        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        int month = intent.getIntExtra("month",0);
        int dayOfMonth = intent.getIntExtra("dayOfMonth",0);
        textView.setText((month+1) + "월" + " "+ dayOfMonth + "일");

//        // 카카오API로부터 이메일 값 받아 왔음, 데이터베이스 만들때 UID로 사용하면 됨
//        String email = intent.getStringExtra("email");
//        Log.d("TodoWriteActivityEmail:", email);

        customAdapter = new CustomAdapter();
        customAdapter.addItem(new Todo(1,"테스트","테스트",false,false));

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkBox);

//        // 파이어베이스 데이터베이스 데이터 추가 되는지 테스트 하였음
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();
//        myRef.setValue("Test1");



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoContent = editText.getText().toString();
                customAdapter.addItem(new Todo(2,todoContent,"알람 해제",false,false));
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
            position = data.getIntExtra("position",0);

            Log.d("아이템 포지션",String.valueOf(position));

            if(hour < 10 && minute < 10) {
                customAdapter.getItems().get(position).setAlarm("0" + hour + ":" + "0" + minute);
                customAdapter.notifyDataSetChanged();
//                customAdapter.alarmSwitch.setText("0" + hour + ":" + "0" + minute);
            } else if (hour < 10 && minute > 10){
                customAdapter.getItems().get(position).setAlarm("0" + hour + ":" + "" + minute);
                customAdapter.notifyDataSetChanged();
            } else if (hour > 10 && minute < 10){
                customAdapter.getItems().get(position).setAlarm("" + hour + ":" + "0" + minute);
                customAdapter.notifyDataSetChanged();
            } else {
                customAdapter.getItems().get(position).setAlarm("" + hour + ":" + "" + minute);
                customAdapter.notifyDataSetChanged();
            }
        }
    }
}