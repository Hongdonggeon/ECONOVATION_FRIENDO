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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;


public class TodoWriteActivity extends AppCompatActivity {

    ArrayList<Todo> items = new ArrayList<Todo>();


    TextView textView;
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    CheckBox checkBox;
    CustomAdapter customAdapter;

    String todoContent;
    int hour;
    int minute;
    int position;

    int month;
    int dayOfMonth;
    String groupName;
    String groupKey;
    String emailGoogle;
    String nameGoogle;
    String uidGoogle;

    HashMap<String,Object> map = new HashMap<>();

    ItemTouchHelper itemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_write);


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        editText = findViewById(R.id.editText);

        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
//        month = intent.getIntExtra("month", 0);
//        dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
//        textView.setText((month + 1) + "월" + " " + dayOfMonth + "일");
        // 캘린더 월/일 값 받아오기
        month = intent.getIntExtra("month", 0);
        dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        // 그룹 이름/키값 받아오기
        groupName = intent.getStringExtra("groupName");
        groupKey = intent.getStringExtra("groupKey");
        // 구글 사용자 정보 받아오기
//        emailGoogle = intent.getStringExtra("emailGoogle");
//        nameGoogle = intent.getStringExtra("nameGoogle");
//        uidGoogle = intent.getStringExtra("uidGoogle");

        textView.setText(month+1 + "월" + " " + dayOfMonth + "일");
        customAdapter = new CustomAdapter(groupKey,month,dayOfMonth);


        // 카카오API로부터 이메일 값, 그룹이름 값 받아 왔음, 데이터베이스 만들때 UID로 사용하면 됨
        String emailKakao = intent.getStringExtra("email");
        String groupName = intent.getStringExtra("groupName");

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkBox);


        // 파이어베이스 데이터베이스 데이터 추가 되는지 테스트 하였음
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Todos").child(groupKey).child((month+1)+"월").child(dayOfMonth+"일");


        // Todo아이템 추가 버튼
        button.setOnClickListener(new View.OnClickListener() {
            String pushKey;
            @Override
            public void onClick(View v) {
                DatabaseReference myRef2 = database.getReference().child("Todos").child(groupKey).child((month+1)+"월").child(dayOfMonth+"일").push();
                todoContent = editText.getText().toString();
                pushKey = myRef2.getKey();

                map.put("pushKey",pushKey);
                map.put("todo", todoContent);
                map.put("alarm", "알람 없음");
                map.put("checkBoxChecked", false);
                map.put("alarmChecked", false);

                Log.d("pushKey Test", pushKey);

                myRef2.setValue(map);

                editText.setText(null);
                Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Todo todo = dataSnapshot.getValue(Todo.class);
                    items.add(todo);
                    customAdapter.setItems(items);
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("TodoWriteActivity: ", String.valueOf(error.toException()));
            }
        });

        recyclerView.setAdapter(customAdapter);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(customAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Todos").child(groupKey).child((month+1)+"월").child(dayOfMonth+"일");

        if(requestCode==101 && resultCode==102) {
            hour = data.getIntExtra("hour",0);
            minute = data.getIntExtra("minute",0);
            position = data.getIntExtra("position",0);

            Log.d("아이템 포지션",String.valueOf(position));
            if(hour < 10 && minute < 10) {
//                customAdapter.getItems().get(position).setAlarm("0" + hour + ":" + "0" + minute);
                myRef.child(items.get(position).getPushKey()).child("alarm").setValue("0" + hour + ":" + "0" + minute);
                customAdapter.notifyDataSetChanged();
            } else if (hour < 10 && minute > 10){
//                customAdapter.getItems().get(position).setAlarm("0" + hour + ":" + "" + minute);
                myRef.child(items.get(position).getPushKey()).child("alarm").setValue("0" + hour + ":" + "" + minute);
                customAdapter.notifyDataSetChanged();
            } else if (hour > 10 && minute < 10){
//                customAdapter.getItems().get(position).setAlarm("" + hour + ":" + "0" + minute);
                myRef.child(items.get(position).getPushKey()).child("alarm").setValue("" + hour + ":" + "0" + minute);
                customAdapter.notifyDataSetChanged();
            } else {
//                customAdapter.getItems().get(position).setAlarm("" + hour + ":" + "" + minute);
                myRef.child(items.get(position).getPushKey()).child("alarm").setValue("" + hour + ":" + "" + minute);
                customAdapter.notifyDataSetChanged();
            }
        }
    }
}