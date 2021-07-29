package org.techtown.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GroupInfoAdapter extends RecyclerView.Adapter<GroupInfoAdapter.ViewHolder>{

    ArrayList<GroupInfo> items = new ArrayList<GroupInfo>();

    public GroupInfoAdapter(){}

    public GroupInfoAdapter(ArrayList<GroupInfo> items) {
        this.items = items;
    }

    public void setItems(ArrayList<GroupInfo> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }

        public void setItem(GroupInfo item) {
            textView.setText(item.email);
        }
    }

    @NonNull
    @NotNull
    @Override
    public GroupInfoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_info_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        GroupInfo item = items.get(position);
        ((ViewHolder)holder).setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
