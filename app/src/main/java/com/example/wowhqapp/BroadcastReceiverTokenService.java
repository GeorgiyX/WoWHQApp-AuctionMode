package com.example.wowhqapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReceiverTokenService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context.getApplicationContext(), WoWTokenService.class);
//        i.putExtra(WoWTokenService.IS_FROM_ACTIVITY, false);

        Log.v(WowhqApplication.LOG_TAG, "onReceive - Ресивер");
        Util.scheduleWoWTokenJob(context, 10);


//        WoWTokenService.startService(context, false);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(i);
//        } else {
//            context.startJob(i);
//        }
    }
}
