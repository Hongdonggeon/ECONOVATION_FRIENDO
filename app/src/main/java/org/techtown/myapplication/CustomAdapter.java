package org.techtown.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    ArrayList<Todo> items = new ArrayList<Todo>();
    Switch alarmSwitch;
    CheckBox checkBox;

    public void setItems(ArrayList<Todo> items) {
        this.items = items;
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
