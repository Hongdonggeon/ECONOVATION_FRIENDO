package org.techtown.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_RESTART_SERVICE = "Restart";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "AlarmReceiver 호출");
        String todo = intent.getStringExtra("todo");
        String groupKey = intent.getStringExtra("groupKey");
        String pushKey = intent.getStringExtra("pushKey");
        int year = intent.getIntExtra("year",0);
        int month = intent.getIntExtra("month",0);
        int dayOfMonth = intent.getIntExtra("dayOfMonth",0);

        if(intent.getAction().equals(ACTION_RESTART_SERVICE)){
            Intent in = new Intent(context, AlarmService.class);
            in.putExtra("todo",todo);
            in.putExtra("groupKey",groupKey);
            in.putExtra("pushKey",pushKey);
            in.putExtra("year",year);
            in.putExtra("month",month);
            in.putExtra("dayOfMonth",dayOfMonth);

            context.startService(in);
        }
    }
}
