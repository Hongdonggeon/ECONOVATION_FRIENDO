package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class HomeActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    GroupsFragment groupsFragment;
    Button accountAddButton;
    public static String tdl_name;
    private static final int MAIN_ACTIVITY_REQUEST_CODE =100;
    Button toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        groupsFragment =(GroupsFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        accountAddButton = findViewById(R.id.groupAddButton);

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
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAIN_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                tdl_name = data.getStringExtra("name");
                groupsFragment.items.add(new User(tdl_name));
                groupsFragment.userAdapter.addItem(new User(tdl_name));
                groupsFragment.recyclerView.setAdapter(groupsFragment.userAdapter);
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