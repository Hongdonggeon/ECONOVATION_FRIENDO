package org.techtown.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_groups,container,false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter();
        for(int i=0; i<13; i++){
            userAdapter.addItem(new User(Integer.toString(i+1)+"번째 그룹"));
        }
        recyclerView.setAdapter(userAdapter);

        return rootView;
    }
}