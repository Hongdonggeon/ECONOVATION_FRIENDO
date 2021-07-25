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
    PendingIntent pendingIntent;

    int position;
    String pushKey;
    String todo;
    String groupKey;

    int year;
    int month;
    int dayOfMonth;

    ArrayList<PendingIntent> pendingIntentArrayList;


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
                setAlarm();
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

        // 다중 알람 테스트
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTimeInMillis(System.currentTimeMillis());
//        calendar2.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//        calendar2.set(Calendar.MINUTE, timePicker.getMinute()+1);
//        calendar2.set(Calendar.SECOND,0);

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

        //reqeustCode를 별도로 관리하여야 할 듯? pendingIntent를 ArrayList로 만들어보자
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), position, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        Log.d("PopupAlarmActivity", "i값 : " + position);
        // 다중 알람 테스트
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 2, alarmIntent, PendingIntent.FLAG_ONE_SHOT);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // 다중 알람 테스트
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.M 호출");
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);

            // 다중 알람 테스트
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
            Log.d("PopupAlarmActivity", "Build.VERSION_CODES.KITKAT");
        }
    }

}