package org.techtown.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import java.util.Calendar;
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

    int year;
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

        // 캘린더 월/일 값 받아오기
        year = intent.getIntExtra("year",0);
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
        customAdapter = new CustomAdapter(groupKey,year,month,dayOfMonth);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkBox);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference()
                .child("Todos")
                .child(groupKey)
                .child(year+"년")
                .child((month+1)+"월")
                .child(dayOfMonth+"일");

        // Todo아이템 추가 버튼
        button.setOnClickListener(new View.OnClickListener() {
            String pushKey;
            @Override
            public void onClick(View v) {
                DatabaseReference myRef2 = database.getReference()
                        .child("Todos")
                        .child(groupKey)
                        .child(year+"년")
                        .child((month+1)+"월")
                        .child(dayOfMonth+"일")
                        .push();

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

                    //알람 시간 String으로 빼왔고,
                    // 시간 / 분 따로 인덱싱해서 여기 TodoWriteAcivity에서 캘린더 시간 설정해서 서비스 호출해주면 될 듯?
                    String alarmTime = todo.getAlarm();
                    if(!alarmTime.equals("알람 없음") & !alarmTime.equals("알람해제")) {
                        Log.d("TodoWriteActivity", alarmTime);
                        int hour = Integer.parseInt(alarmTime.substring(0, 2));
                        int minute = Integer.parseInt(alarmTime.substring(3, 5));

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        Log.d("TodoWriteActivity", hour + "시" + minute + "분 설정");
                        calendar.set(Calendar.SECOND, 0);

                        if (calendar.before(Calendar.getInstance())) {
                            calendar.add(Calendar.DATE, 1);
                        }

                        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        alarmIntent.putExtra("todo", todo.getTodo());
                        alarmIntent.putExtra("groupKey", groupKey);
                        alarmIntent.putExtra("year", year);
                        alarmIntent.putExtra("month", month);
                        alarmIntent.putExtra("dayOfMonth", dayOfMonth);
                        alarmIntent.putExtra("pushKey", todo.getPushKey());

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmIntent.setAction(AlarmReceiver.ACTION_RESTART_SERVICE);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), position, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
                        Log.d("PopupAlarmActivity", "i값 : " + position);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.M 호출");
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.KITKAT 호출");
                        }
                    }
                    // 일단 여기까지 테스트임

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
        DatabaseReference myRef = database.getReference("Todos")
                .child(groupKey)
                .child(year+"년")
                .child((month+1)+"월")
                .child(dayOfMonth+"일");

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