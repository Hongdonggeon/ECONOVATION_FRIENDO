package org.techtown.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<User> items = new ArrayList<User>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.user_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int position) {
        User item  = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(User item) {
        items.add(item);
    }

    public void setItems(ArrayList<User> items) {
        this.items = items;
    }

    public User getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, User item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.tdl_name);
        }

        public void setItem(User item) {
            textView.setText(item.getName());
        }
    }

}
