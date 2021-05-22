package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SubActivity extends AppCompatActivity {
    private ImageView iv_profile;
    private TextView tv_nickname;
    private TextView tv_email;
    private Button btn_logout;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        iv_profile = findViewById(R.id.iv_profile);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_email = findViewById(R.id.tv_email);
        btn_logout = findViewById(R.id.btn_logout);

        Intent intent = getIntent();
        String profile = intent.getStringExtra("profile");
        String nickname = intent.getStringExtra("nickname");
        String email = intent.getStringExtra("email");

        Glide.with(this).load(profile).circleCrop().into(iv_profile);
        tv_nickname.setText(nickname);
        tv_email.setText(email);

        logoutButtonClicked();

    }

    // 로그아웃 버튼 클릭했을때 발생하는 이벤트 메소드
    public void logoutButtonClicked(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        if(throwable != null){
                            Log.e("fail logout : ", "로그아웃 실패, SDK에서 토큰 삭제 됨", throwable);
                        }
                        else {
                            finish();
                            Log.e("success logout : ", "로그아웃 성공, SDK에서 토큰 삭제 됨");
                        }
                        return null;
                    }
                });
            }
        });
    }

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