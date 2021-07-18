package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class GroupCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ViewPager pager = findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(3);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        CalendarFragment calendarFragment = new CalendarFragment();
        adapter.addItem(calendarFragment);
        GroupInfoFragment groupInfoFragment = new GroupInfoFragment();
        adapter.addItem(groupInfoFragment);
        SettingFragment settingFragment = new SettingFragment();
        adapter.addItem(settingFragment);

        pager.setAdapter(adapter);

        // 로그인 화면에서 받아온 카카오 이메일 값
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String groupName = intent.getStringExtra("groupName");
        String groupKey = intent.getStringExtra("groupKey");

        // Bundle 이용하여 CalendarFragment로 넘겨줌
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("groupName", groupName);
        bundle.putString("groupKey",groupKey);
        calendarFragment.setArguments(bundle);

        // BottomNavigationView를 직접 터치했을때 화면 전환 이벤트
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.calendar:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.group:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.setting:
                        pager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        // 화면을 스크롤하여 전환시켰을때 BottomNavigationView의 탭 전환 이벤트
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

}
