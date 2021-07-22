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
        if(intent.getAction().equals(ACTION_RESTART_SERVICE)){
            Intent in = new Intent(context, AlarmService.class);
            context.startService(in);
        }
    }
}
