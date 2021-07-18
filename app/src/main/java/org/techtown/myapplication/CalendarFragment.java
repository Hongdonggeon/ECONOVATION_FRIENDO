package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {
    String email;
    String groupName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        CalendarView calendarView = (CalendarView)view.findViewById(R.id.calendarView);
        TextView textView = (TextView)view.findViewById(R.id.textView);

        // MainActivity에서 카카오API로부터 사용자의 이메일 값 가져옴
        Bundle bundle = getArguments();
        if(bundle != null) {
            email = bundle.getString("email","이메일 값 안넘어옴");
            groupName = bundle.getString("groupName", "그룹이름 값 안넘어옴");
            Log.d("카카오 이메일 정보 : ", email);
            Log.d("그룹이름 정보 : ", groupName);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Intent intent = new Intent(getContext(), TodoWriteActivity.class);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                intent.putExtra("email", email);
                intent.putExtra("groupName",groupName);
                startActivity(intent);
            }
        });
        return view;
    }
}