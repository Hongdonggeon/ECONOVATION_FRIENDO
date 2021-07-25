package org.techtown.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class SettingFragment extends Fragment {
    Button btn_developer_info;
    Button btn_logout;

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting,container,false);

        mAuth = FirebaseAuth.getInstance();

        btn_developer_info = rootView.findViewById(R.id.btn_developer_info);
        btn_logout = rootView.findViewById(R.id.btn_logout);


        // 개발자 정보 클릭이벤트
        btn_developer_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 로그아웃 클릭이벤트
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.create().show();
            }
        });

        return rootView;
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        Toast.makeText(getContext(),"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
    }
}