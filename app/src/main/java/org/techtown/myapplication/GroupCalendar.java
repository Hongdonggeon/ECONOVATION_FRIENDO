package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GroupCalendar extends AppCompatActivity {
    HashMap<String,String> userTokens = new HashMap<>();

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

        Intent intent = getIntent();
        // 카카오 사용자 정보 받아오기
        String emailKakao = intent.getStringExtra("emailKakao");
        userTokens = (HashMap<String, String>)intent.getSerializableExtra("userTokens");
        // 그룹 이름/난수 받아오기
        String groupName = intent.getStringExtra("groupName");
        String groupKey = intent.getStringExtra("groupKey");
        // 구글 사용자 정보 받아오기
//        String emailGoogle = intent.getStringExtra("emailGoogle");
//        String nameGoogle = intent.getStringExtra("nameGoogle");
//        String uidGoolge = intent.getStringExtra("uidGoogle");

        // Bundle 이용하여 CalendarFragment로 넘겨줌
        Bundle bundle = new Bundle();
        // 카카오 사용자 정보 내보내기
        bundle.putString("emailKakao", emailKakao);
        bundle.putSerializable("userTokens", userTokens);
        // 그룹 이름/난수 내보내기
        bundle.putString("groupName", groupName);
        bundle.putString("groupKey",groupKey);
        // 구글 사용자 정보 내보내기
//        bundle.putString("emailGoolge", emailGoogle);
//        bundle.putString("nameGoolge", nameGoogle);
//        bundle.putString("uidGoogle",uidGoolge);
        calendarFragment.setArguments(bundle);
        groupInfoFragment.setArguments(bundle);


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
