package org.techtown.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GroupInfoFragment extends Fragment {
    RecyclerView recyclerView;

    String groupKey;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_group_info,container,false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        if(bundle != null){
            groupKey = bundle.getString("groupKey");
        }

        ArrayList<GroupInfo> groupInfoItems = new ArrayList<GroupInfo>();
        GroupInfoAdapter groupInfoAdapter = new GroupInfoAdapter();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GroupUsers").child(groupKey);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                groupInfoItems.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String groupInfo = dataSnapshot.getValue(String.class);
                    groupInfoItems.add(new GroupInfo(groupInfo));
                    groupInfoAdapter.setItems(groupInfoItems);
                    Log.d("GroupInfoFragment", groupInfo);
                }
                groupInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        recyclerView.setAdapter(groupInfoAdapter);
        return rootView;
    }
}