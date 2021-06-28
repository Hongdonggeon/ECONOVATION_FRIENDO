package org.techtown.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    ArrayList<Todo> items = new ArrayList<Todo>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
        }
    }
    @NonNull
    @NotNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomAdapter.ViewHolder holder, int position) {
        CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
        checkBox.setText(items.get(position).todo);
    }

    public ArrayList<Todo> getItems() {
        return items;
    }

    public void setItems(ArrayList<Todo> items) {
        this.items = items;
        notifyDataSetChanged();
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
