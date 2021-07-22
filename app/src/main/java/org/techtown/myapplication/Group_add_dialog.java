package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Group_add_dialog extends AppCompatActivity {
     Button submitBtn;
     Button cancelBtn;
     EditText tdl_input;
     RecyclerView recyclerView;
     GroupMemberAdapter adapter;
     EditText email_input;
     private HashMap<String,String> userTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        userTokens = (HashMap<String,String>)intent.getSerializableExtra("userTokens");

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

        recyclerView = findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GroupMemberAdapter();
        Button plusBtn = findViewById(R.id.member_plus_btn);
        email_input = findViewById(R.id.email_input);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String member_email = email_input.getText().toString();
                adapter.addItem(new GroupMember(member_email));
//                if(!userTokens.isEmpty() && userTokens.containsKey(member_email)) {
//                    Gson gson = new Gson();
//                    NotificationModel notificationModel = new NotificationModel();
//                    notificationModel.to = userTokens.get(member_email);
//                    notificationModel.notification.title = "FRIENDO";
//                    notificationModel.notification.text = "초대 수락하시겠습니까";
//
//                    RequestBody requestBody = RequestBody.create(gson.toJson(notificationModel), MediaType.parse("application/json; charset=utf8"));
//                    Request request = new Request.Builder().header("Content-Type", "application/json")
//                            .addHeader("Authorization", "key=AAAAlzEMvvg:APA91bGHGn5W1uGfO3PKxvn_IGMK41j5b2ArIglH6PG_Py2kRNupE0v0St6YX28St_7ZkOKVs31cjz8psFiHvdMqGgSMnbiUyIvhf0XtbJIhaJ2XsD0X-DHjZAd4LX6BYGjumXUE3Lqh")
//                            .url("https://gcm-http.googleapis.com/gcm/send")
//                            .post(requestBody)
//                            .build();
//                    OkHttpClient okHttpClient = new OkHttpClient();
//                    okHttpClient.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                        }
//                    });
//                }
//                else {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(Group_add_dialog.this).setMessage("FRIENDO의 사용자가 아닙니다.");
//                    dialog.create().show();
//                }

                email_input.setText(null);

            }
        });
        recyclerView.setAdapter(adapter);
    }
}