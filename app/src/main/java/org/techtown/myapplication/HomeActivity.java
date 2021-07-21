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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.user.UserApiClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
    ArrayList<User> groupsNames;
    String gid;

    Button kakaoLogoutButton;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        groupsFragment =(GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        accountAddButton = findViewById(R.id.groupAddButton);
        kakaoLogoutButton = findViewById(R.id.kakaoLogoutButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        Intent intent = getIntent();
        String emailKakao = intent.getStringExtra("emailKakao");

        //구글 사용자 정보
        String emailGoogle = intent.getStringExtra("emailGoogle");
        String nameGoogle = intent.getStringExtra("nameGoogle");
        String uidGoogle = user.getUid();


        // Users 데이터베이스 생성
        database =FirebaseDatabase.getInstance();
        myReference4 = database.getReference();
        myReference4.child("Users").child(uidGoogle).child("Name").setValue(nameGoogle);
        myReference4.child("Users").child(uidGoogle).child("Email").setValue(emailGoogle);

        uuid = intent.getLongExtra("uuid",0);


        // 임시 카카오 로그아웃 버튼
        kakaoLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        if(throwable != null){
                            Log.e("로그아웃 실패", "로그아웃 실패, SDK에서 토큰 삭제됨",throwable);
                        }else {
                            Log.i("로그아웃 성공","로그아웃 성공, SDK에서 토큰 삭제됨");
                            finish();
                        }
                        return null;
                    }
                });
            }
        });

        // 그룹 추가 버튼
        accountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Group_add_dialog.class);
                startActivityForResult(intent,MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
        groupsFragment.userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
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

        groupsFragment.userAdapter.setOnItemLongClickListener(new UserAdapter.OnItemLongClickListener() {
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
                groupsFragment.items.add(new User(snapshot.getValue().toString(),snapshot.getKey()));
                groupsFragment.userAdapter.setItems(groupsFragment.items);
                groupsFragment.userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("HomeActivity","onchildchanged"+snapshot.getValue());

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("HomeActivity","onchildremoved"+snapshot.getValue());
                groupsFragment.userAdapter.setItems(groupsFragment.items);
                groupsFragment.userAdapter.notifyDataSetChanged();
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

//        myReference.child(gid).child("그룹명").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                groupsFragment.items.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User user = dataSnapshot.getValue(User.class);
//                    groupsFragment.items.add(user);
//                    groupsFragment.userAdapter.setItems(groupsFragment.items);
//                }

//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Log.e("HomeActivity",String.valueOf(error.toException()));
//            }
//        });
//        groupsFragment.userAdapter = new UserAdapter(groupsFragment.items,this);
//        groupsFragment.recyclerView.setAdapter(groupsFragment.userAdapter);

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