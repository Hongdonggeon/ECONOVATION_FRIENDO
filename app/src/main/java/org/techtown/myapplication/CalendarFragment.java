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
    String emailKakao;
    String groupName;
    String groupKey;
    String emailGoogle;
    String nameGoogle;
    String uidGoogle;
    String TAG = "CalendarFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        CalendarView calendarView = (CalendarView)view.findViewById(R.id.calendarView);
        TextView textView = (TextView)view.findViewById(R.id.textView);

        // MainActivity에서 카카오API로부터 사용자의 이메일 값 가져옴
        Bundle bundle = getArguments();
        if(bundle != null) {
            // 카카오 사용자 정보 받아오기
            emailKakao = bundle.getString("emailKakao","카카오 이메일 값 안넘어옴");
            // 그룹 이름/난수 받아오기
            groupName = bundle.getString("groupName", "그룹이름 값 안넘어옴");
            groupKey = bundle.getString("groupKey","카카오 그룹 키 값 안넘어옴");
            // 구글 사용자 정보 받아오기
//            emailGoogle = bundle.getString("emailGoogle", "구글 이메일 값 안넘어옴");
//            nameGoogle = bundle.getString("nameGoogle", "구글 사용자명 값 안넘어옴");
//            uidGoogle = bundle.getString("uidGoogle", "구글 uid 값 안넘어옴");

            Log.d(TAG,"카카오 이메일 " + emailKakao);
            Log.d(TAG, "그룹 명 " + groupName);
            Log.d(TAG, "groupKey " + groupKey);
//            Log.d(TAG,"구글 이메일 " + emailGoogle);
//            Log.d(TAG, "구글 사용자명 " + nameGoogle);
//            Log.d(TAG, "구글 uid " + uidGoogle);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Intent intent = new Intent(getContext(), TodoWriteActivity.class);
                // 캘린더 월/일 값 내보내기
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                // 카카오 사용자 정보 내보내기
                intent.putExtra("emailKakao", emailKakao);
                // 그룹 이름/키값 내보내기
                intent.putExtra("groupName",groupName);
                intent.putExtra("groupKey",groupKey);
                // 구글 사용자 정보 내보내기
//                intent.putExtra("emailGoogle",emailGoogle);
//                intent.putExtra("nameGoogle",nameGoogle);
//                intent.putExtra("uidGoogle",uidGoogle);
                startActivity(intent);
            }
        });
        return view;
    }
}