package org.techtown.myapplication;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GroupCalendar extends AppCompatActivity {
    TextView textView;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        calendarView = findViewById(R.id.calendarView);
        textView = findViewById(R.id.textView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month+1) + "/" + dayOfMonth;
                textView.setText(date);

            }
        });
    }
}