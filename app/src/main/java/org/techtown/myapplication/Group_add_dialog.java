package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Group_add_dialog extends AppCompatActivity {
     Button submitBtn;
     Button cancelBtn;
     EditText tdl_input;

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_add_dialog);
        submitBtn = findViewById(R.id.submit_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        tdl_input = findViewById(R.id.tdl_input);

        Intent intent = getIntent();
        long uuid = intent.getLongExtra("uuid",0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tdl_name = tdl_input.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("name", tdl_name);
                setResult(RESULT_OK, intent);

//                //GroupUsers -> 그룹의 사용자들 관리하는 데이터베이스 테스트구현
//                myRef.child("GroupUsers").child(tdl_name).push().setValue(uuid);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}