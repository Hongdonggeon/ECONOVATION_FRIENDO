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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperListener{
    ArrayList<Todo> items = new ArrayList<Todo>();
    String groupKey;
    int year;
    int month;
    int dayOfMonth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public CustomAdapter(){}

    public CustomAdapter(String groupKey,int year, int month, int dayOfMonth) {
        this.year= year;
        this.groupKey = groupKey;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
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
        Log.d("아이템포지션 체크", String.valueOf(position));

        myRef.child("Todos")
                .child(groupKey)
                .child(year+"년")
                .child((month+1)+"월")
                .child(dayOfMonth+"일")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("getKey() 테스트Added",dataSnapshot.getKey());
                    if(dataSnapshot.getKey().equals(items.get(position).getPushKey())) {
                        myRef.child("Todos")
                                .child(groupKey)
                                .child(year+"년")
                                .child((month+1)+"월")
                                .child(dayOfMonth+"일")
                                .child(items.get(position).getPushKey()).removeValue();
                    }
                }
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
                myRef.child("Todos")
                        .child(groupKey)
                        .child(year+"년")
                        .child((month+1)+"월")
                        .child(dayOfMonth+"일")
                        .child(item.getPushKey())
                        .child("checkBoxChecked")
                        .setValue(isChecked);
            }
        });


        holder.alarmSwitch.setOnCheckedChangeListener(null);
        holder.alarmSwitch.setChecked(item.isAlarmChecked());
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //알람 스위치 상태 DB저장 완료, 알람 시간 텍스트뷰에 적용시켜야함
                    myRef.child("Todos")
                            .child(groupKey)
                            .child(year+"년")
                            .child((month+1)+"월")
                            .child(dayOfMonth+"일")
                            .child(item.getPushKey())
                            .child("alarmChecked").setValue(isChecked);

                    Intent intent = new Intent(buttonView.getContext(), PopupAlarmActivity.class);
                    intent.putExtra("todo",item.getTodo());
                    intent.putExtra("groupKey",groupKey);
                    intent.putExtra("year",year);
                    intent.putExtra("month",month);
                    intent.putExtra("dayOfMonth",dayOfMonth);
                    intent.putExtra("position",position);
                    intent.putExtra("pushKey",item.getPushKey());

                    startActivityForResult((Activity) buttonView.getContext(), intent, 101,null);
                } else {
//                    item.setAlarmChecked(isChecked);

                    myRef.child("Todos")
                            .child(groupKey)
                            .child(year+"년")
                            .child((month+1)+"월")
                            .child((dayOfMonth+"일"))
                            .child(item.getPushKey())
                            .child("alarmChecked")
                            .setValue(isChecked);
                    myRef.child("Todos")
                            .child(groupKey)
                            .child(year+"년")
                            .child((month+1)+"월")
                            .child((dayOfMonth+"일"))
                            .child(item.getPushKey())
                            .child("alarm")
                            .setValue("알람해제");
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
