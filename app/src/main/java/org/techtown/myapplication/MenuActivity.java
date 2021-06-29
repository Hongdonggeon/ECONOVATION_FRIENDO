package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MenuActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    GroupsFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mainFragment =(GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);

        //로그인 화면에서 받아온 카카오 이메일 값
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // 그룹캘린더 테스트용 버튼
        Button btn_test = findViewById(R.id.button);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

    }
    // 로그인 후에 다시 로그인 화면 돌아가는거 방지 & 두번 터치시 어플 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}