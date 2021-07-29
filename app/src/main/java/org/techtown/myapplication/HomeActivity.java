package org.techtown.myapplication;

import android.app.AlertDialog;
import android.app.PendingIntent;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;


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
    private DatabaseReference myReference5;
    private DatabaseReference myReference6;
    Long uuid;

    ArrayList<String> Uids = new ArrayList<>();
    HashMap<String, String> userUids = new HashMap<>();

    ArrayList<PendingIntent> pendingIntentArrayList;

    int position;
    String token;
    String uidGoogle;
    String emailGoogle;

    HashMap<String, String> userTokens = new HashMap<>();

    @Override
    protected void onStart() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uidGoogle = user.getUid();
        super.onStart();
    }

    @Override
    protected void onResume() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uidGoogle = user.getUid();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        pendingIntentArrayList = new ArrayList<>();

        groupsFragment =(GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        accountAddButton = findViewById(R.id.groupAddButton);

        Intent intent = getIntent();
        String emailKakao = intent.getStringExtra("emailKakao");

        //구글 사용자 정보
        emailGoogle = intent.getStringExtra("emailGoogle");
        String nameGoogle = intent.getStringExtra("nameGoogle");

        uidGoogle = user.getUid();


//        Log.d("HomeActivity","emailGoogle : " + user.getEmail());
//        Log.d("HomeActivity","nameGoogle : " + user.getDisplayName());
//        Log.d("HomeActivity","uidGoogle : " + user.getUid());

        // fcm 토큰 얻기
        saveTokenToDB();

        Intent hashIntent = new Intent(getApplicationContext(),Group_add_dialog.class);
        hashIntent.putExtra("userTokens",userTokens);

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
                intent.putExtra("hashIntent",userTokens);
                intent.putExtra("nameGoogle",nameGoogle);
                intent.putExtra("emailGoogle",emailGoogle);
                intent.putExtra("uidGoogle",uidGoogle);

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
                intent.putExtra("userTokens",userTokens);
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
        myReference5 = database.getReference("GroupUsers");


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
                                int numberofmember = groupsFragment.items.get(pos).getNumberofMember();
                                groupsFragment.items.get(pos).setNumberofMember(--numberofmember);
                                Log.d("HomeActivity here",groupsFragment.items.get(pos).getNumberofMember()+"");
                                myReference.child(groupKey).removeValue();
                                myReference5.child(groupKey).child(uidGoogle).removeValue();

                                myReference5.addValueEventListener(new ValueEventListener() {
                                    boolean tempFlag;
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            Log.d("homeactivity",dataSnapshot.toString());
                                            if (groupKey.equals(dataSnapshot.getKey())){
                                                tempFlag = true;
                                                break;
                                            }
                                            else{
                                                tempFlag = false;
                                            }
                                        }
                                        if (!tempFlag)
                                            myReference3.child(groupKey).removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                                groupsFragment.items.remove(pos);

                                Log.d("그룹 키값 확인",groupKey);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dialog.create().show();
            }
        });



        myReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("HomeActivity","onchildadded"+snapshot.getKey());
                Log.d("HomeActivity","onchildadded"+snapshot.getValue());
                Group group = new Group(snapshot.getValue().toString(), snapshot.getKey(),0);
                groupsFragment.items.add(group);
                String groupKey = groupsFragment.items.get(groupsFragment.items.size() - 1).getKey();
                String groupname = groupsFragment.items.get(groupsFragment.items.size() - 1).getName();
                Log.d("HomeActivity","groupkey:"+ groupKey);
                for(String uid : Uids){
                    myReference2.child(uid).child(groupKey).setValue(groupname);
                }
                if(!userUids.isEmpty()) {
                    myReference5.child(groupKey).setValue(userUids);
                    groupsFragment.items.get(groupsFragment.items.size()-1).setNumberofMember(userUids.size());

                }
                groupsFragment.groupAdapter.setItems(groupsFragment.items);


//                myReference5.child(groupsFragment.items.get(groupsFragment.items.size()-1).getKey())
//                        .setValue(groupsFragment.items.get(groupsFragment.items.size()-1).getName());
      //          myReference5.child(groupsFragment.items.get(groupsFragment.items.size() - 1).getKey()).setValue(userUids);

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
                        myReference4.child("FcmID").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String information = dataSnapshot.getValue().toString();
                                    Log.d("homeactivity",information);
                                    information = information.substring(1,information.length()-1);
                                    String[] array = information.split("=");
                                    String token = array[0];
                                    String email = array[1];
                                    userTokens.put(email, token);
                                    Log.d("homeactivity", "here ! " + array[0]);
                                    Log.d("homeactivity", "here ! " + array[1]);
                                }

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
        ArrayList<UserGroups> userItems = new ArrayList<>();

        if(requestCode == MAIN_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                tdl_name = data.getStringExtra("name");
                Uids = (ArrayList<String>) data.getSerializableExtra("uids");
                userUids = (HashMap<String, String>)data.getSerializableExtra("userUids");

                Log.d("HomeActivity","array here!!!!!!!!!!!!!!"+ Uids);
                Log.d("HomeActivity","hash here!!!!!!!!!!"+userUids.keySet());
//                Set<String> keys = userUids.keySet();

//                for (int i =0; i< userUids.size(); i++){
//                    String email = userUids.get(i);
//                    if(userUids.values().contains(email))
//                    myReference4.child("UserGroups").child(userUids.).push();
//                }


              //  myReference6 = database.getReference("UserGroups").child(uidGoogle).push();
               // String groupKey = myReference6.getKey();

              //  for(String uid : userUids.keySet()){
                //    Log.d("HomeActivity", "uid" + uid);
                 //   userItems.add(new UserGroups(uid,groupKey,tdl_name));
               // }
               // for(int i=0; i<userItems.size(); i++){
                 //   myReference4.child("UserGroups").child(userItems.get(i).getUid()).child(userItems.get(i).getGroupKey()).setValue(userItems.get(i).getGroupName());
               // }


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