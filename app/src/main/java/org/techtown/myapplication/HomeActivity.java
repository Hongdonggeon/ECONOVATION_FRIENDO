package org.techtown.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    GroupsFragment groupsFragment;
    Button accountAddButton;
    public static String tdl_name;
    private static final int MAIN_ACTIVITY_REQUEST_CODE =100;
    private FirebaseDatabase database;
    private DatabaseReference myReference;
    private DatabaseReference myReference2;
    private DatabaseReference myReference3;
    private DatabaseReference myReference4;
    Long uuid;

    private UserModel destinationUserModel;

   

    int position;
    String token;
    String uidGoogle;
    String emailGoogle;

    HashMap<String, String> userTokens = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        groupsFragment =(GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        accountAddButton = findViewById(R.id.groupAddButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        Intent intent = getIntent();
        String emailKakao = intent.getStringExtra("emailKakao");

        //구글 사용자 정보
         emailGoogle = intent.getStringExtra("emailGoogle");
        String nameGoogle = intent.getStringExtra("nameGoogle");
         uidGoogle = user.getUid();

        // fcm 토큰 얻기
        saveTokenToDB();

        // Users 데이터베이스 생성
        database =FirebaseDatabase.getInstance();
        myReference4 = database.getReference();
        myReference4.child("Users").child(uidGoogle).child("Name").setValue(nameGoogle);
        myReference4.child("Users").child(uidGoogle).child("Email").setValue(emailGoogle);
        uuid = intent.getLongExtra("uuid",0);


        // 그룹 추가 버튼
        accountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Group_add_dialog.class);
                startActivityForResult(intent,MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
        groupsFragment.groupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
                //카카오 사용자 정보 내보내기
                intent.putExtra("emailKakao",emailKakao);
                // 그룹 이름/난수 내보내기
                intent.putExtra("groupName",tdl_name);
                intent.putExtra("groupKey",groupsFragment.items.get(pos).getKey());
                //구글 사용자 정보 내보내기
//                intent.putExtra("emailGoolge",emailGoogle);
//                intent.putExtra("nameGoogle",nameGoogle);
//                intent.putExtra("uidGoogle",uidGoogle);
                startActivity(intent);
            }
        });


        myReference = database.getReference("UserGroups").child(uidGoogle);
        myReference2 = database.getReference("UserGroups");
        myReference3 = database.getReference("Todos");


        groupsFragment.groupAdapter.setOnItemLongClickListener(new GroupAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                Toast.makeText(getApplicationContext(),"롱클릭 확인",Toast.LENGTH_SHORT);
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("삭제")
                        .setMessage("해당 그룹을 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"그룹이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                String groupKey = groupsFragment.items.get(pos).getKey();

                                position = pos;
                                groupsFragment.items.remove(pos);
                                myReference.child(groupKey).removeValue();
                                myReference3.child(groupKey).removeValue();

                                Log.d("그룹 키값 확인",groupKey);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                dialog.create().show();
            }
        });

        myReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("HomeActivity","onchildadded"+snapshot.getKey());
                groupsFragment.items.add(new Group(snapshot.getValue().toString(),snapshot.getKey()));
                groupsFragment.groupAdapter.setItems(groupsFragment.items);
                groupsFragment.groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("HomeActivity","onchildchanged"+snapshot.getValue());

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("HomeActivity","onchildremoved"+snapshot.getValue());
                groupsFragment.groupAdapter.setItems(groupsFragment.items);
                groupsFragment.groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("HomeActivity","onchildmoved"+snapshot.getValue());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("HomeActivity","onchildcancelled"+error.getMessage());

            }
        });
    }

    private void saveTokenToDB() {
        // 현재 토큰
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        sendGcm();
                        if (!task.isSuccessful()) {
                            Log.w("HomeActivity", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("HomeActivity", msg);
                        Log.d("HomeActivity", token);
//                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        myReference4 = database.getReference();
                        myReference4.child("FcmID").child(uidGoogle).child(token).setValue(emailGoogle);

                        myReference4.child("FcmID").child(uidGoogle).child(token).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                String token = snapshot.getKey();
                                String email = snapshot.getValue().toString();
                                userTokens.put(email,token);
                                Log.d("homeactivity","here ! "+email);
                                Log.d("homeactivity","here ! "+userTokens.get(email));

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAIN_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                tdl_name = data.getStringExtra("name");
//                groupsFragment.items.add(new User(tdl_name));
//                groupsFragment.recyclerView.setAdapter(groupsFragment.userAdapter);
                myReference.push().setValue(tdl_name);

            }
        }

    }

    void sendGcm(){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = token;
        notificationModel.notification.title ="asdf";
        notificationModel.notification.text="초대 수락하시겠습니까";

        RequestBody requestBody = RequestBody.create(gson.toJson(notificationModel),MediaType.parse("application/json; charset=utf8"));
        Request request = new Request.Builder().header("Content-Type", "application/json")
                .addHeader("Authorization","key=AAAAlzEMvvg:APA91bGHGn5W1uGfO3PKxvn_IGMK41j5b2ArIglH6PG_Py2kRNupE0v0St6YX28St_7ZkOKVs31cjz8psFiHvdMqGgSMnbiUyIvhf0XtbJIhaJ2XsD0X-DHjZAd4LX6BYGjumXUE3Lqh")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient =new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

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