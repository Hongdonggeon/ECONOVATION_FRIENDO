package org.techtown.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    Calendar calendar;
    MediaPlayer mediaPlayer;

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        calendar = Calendar.getInstance();

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        // 잠금 화면 위로 Activity 띄움
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beepbeepalarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        Intent intent = getIntent();
        String todo = intent.getStringExtra("todo");
        Log.d("AlarmActivity", "todo내용" + todo);
        textView.setText(todo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
    }
}