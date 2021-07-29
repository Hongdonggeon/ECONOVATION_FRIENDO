package org.techtown.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class PopupAlarmActivity extends AppCompatActivity {

    TimePicker timePicker;

    Calendar calendar;

    int position;
    String pushKey;
    String todo;
    String groupKey;

    int year;
    int month;
    int dayOfMonth;

    ArrayList<PendingIntent> pendingIntentArrayList;

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(getApplicationContext(),AlarmService.class);
        getApplicationContext().stopService(serviceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_alarm);
        timePicker = findViewById(R.id.timePicker);



        CustomAdapter customAdapter = new CustomAdapter();
        pendingIntentArrayList = new ArrayList<>();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        pushKey = intent.getStringExtra("pushKey");
        todo = intent.getStringExtra("todo");
        groupKey = intent.getStringExtra("groupKey");
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        dayOfMonth = intent.getIntExtra("dayOfMonth",0);

        Log.d("PopupAlarmActivitiy", "Todo내용: " + todo);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("position", position);
                intent.putExtra("hour",timePicker.getHour());
                intent.putExtra("minute",timePicker.getMinute());
                intent.putExtra("todo",todo);
//                setAlarm();

                setResult(102,intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm(){
        Log.d("PopupAlarmActivity", "setAlarm()메소드 호출");
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        Log.d("PopupAlarmActivity", timePicker.getHour()+"시"+timePicker.getMinute()+"분 설정");
        calendar.set(Calendar.SECOND,0);

        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.putExtra("todo",todo);
        alarmIntent.putExtra("groupKey",groupKey);
        alarmIntent.putExtra("year",year);
        alarmIntent.putExtra("month",month);
        alarmIntent.putExtra("dayOfMonth",dayOfMonth);
        alarmIntent.putExtra("pushKey",pushKey);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmIntent.setAction(AlarmReceiver.ACTION_RESTART_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), position, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        Log.d("PopupAlarmActivity", "i값 : " + position);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.M 호출");
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.KITKAT 호출");
        }
    }

}