package org.techtown.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperListener{
    ArrayList<Todo> items = new ArrayList<Todo>();
    String groupName;
    int month;
    int dayOfMonth;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public CustomAdapter(){}

    public CustomAdapter(String groupName, int month, int dayOfMonth) {
        this.groupName = groupName;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public CustomAdapter(int month, int dayOfMonth) {
        this.month = month;
        this.dayOfMonth=dayOfMonth;
    }

    public void setItems(ArrayList<Todo> items) {
        this.items = items;
    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }



    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);

        Log.d("month data test", month+1+"월");
        Log.d("dayOfMonth data test", dayOfMonth+"일");

        myRef.child("Todos").child(groupName).child((month+1)+"월").child(dayOfMonth+"일").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("getKey() 테스트Added",snapshot.getKey());
                if(snapshot.getKey() == "-Mejtv_XSWdfj7uMEXuZ")
                    myRef.child("Todos").child(groupName).child((month+1)+"월").child(dayOfMonth+"일").child("-Mejtv_XSWdfj7uMEXuZ").removeValue();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("getKey() 테스트Changed",snapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("getKey() 테스트Removed",snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Log.d("getKey() 테스트Moved",snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    // 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Switch alarmSwitch;
        CheckBox checkBox;
        public ViewHolder(View view) {
            super(view);
            alarmSwitch = view.findViewById(R.id.alarmSwitch);
            checkBox = view.findViewById(R.id.checkBox);
        }

        // 설정된 알람 시간 텍스트로 표시
        public void setItem(Todo item){
            alarmSwitch.setText(item.getAlarm());
        }
    }

    @NonNull
    @NotNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_todo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomAdapter.ViewHolder holder, int position) {
        final Todo item = items.get(position);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isCheckBoxChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    item.setCheckBoxChecked(true);
                }
            }
        });

        holder.alarmSwitch.setOnCheckedChangeListener(null);
        holder.alarmSwitch.setChecked(item.isAlarmChecked());
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item.setAlarmChecked(true);
                    Intent intent = new Intent(buttonView.getContext(), PopupAlarmActivity.class);
                    intent.putExtra("position",position);
                    startActivityForResult((Activity) buttonView.getContext(), intent, 101,null);
                } else {
                    items.get(position).setAlarm("알람 해제");
                    notifyDataSetChanged();
                }
            }
        });
        // setItem()메소드( 알람 시간 텍스트 설정 )를 사용하여 데이터 바인딩 시킴
        ((ViewHolder)holder).setItem(item);

        holder.checkBox.setText(items.get(position).todo);
    }
    public ArrayList<Todo> getItems() {
        return items;
    }

    public void addItem(Todo item){
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
