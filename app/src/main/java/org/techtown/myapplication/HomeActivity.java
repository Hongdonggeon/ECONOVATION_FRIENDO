package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
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

                if(groupsFragment.items.size()>0) {
                    toDoList = findViewById(R.id.tdl_name);
                    toDoList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), GroupCalendar.class);
                            startActivity(intent);
                        }
                    });
                }
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
}