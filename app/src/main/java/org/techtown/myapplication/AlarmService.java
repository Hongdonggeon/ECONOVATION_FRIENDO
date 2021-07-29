package org.techtown.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AlarmService extends Service {
    String TAG = "TAG+Service";
    String todo;
    String groupKey;
    String pushKey;

    int year;
    int month;
    int dayOfMonth;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"AlarmService 호출");
        todo = intent.getStringExtra("todo");
        groupKey = intent.getStringExtra("groupKey");
        pushKey = intent.getStringExtra("pushKey");
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        dayOfMonth = intent.getIntExtra("dayOfMonth",0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Todos")
                .child(groupKey)
                .child(year+"년")
                .child((month+1)+"월")
                .child(dayOfMonth+"일")
                .child(pushKey)
                .child("alarm");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.getValue().toString().equals("알람해제")) {

                    Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);
                    alarmIntent.putExtra("todo", todo);
                    alarmIntent.putExtra("groupKey", groupKey);
                    alarmIntent.putExtra("pushKey", pushKey);
                    alarmIntent.putExtra("year", year);
                    alarmIntent.putExtra("month", month);
                    alarmIntent.putExtra("dayOfMonth", dayOfMonth);

                    startActivity(alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    stopSelf();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return super.onStartCommand(intent, flags, startId);
    }
}
