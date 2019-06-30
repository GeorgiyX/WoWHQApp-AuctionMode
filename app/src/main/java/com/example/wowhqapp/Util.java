package com.example.wowhqapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class Util {

    public static void scheduleWoWTokenJob (Context context, int time_to_start){
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(WoWTokenJobService.WOWTOKEN_JOB_ID);
        ComponentName tokenJobService = new ComponentName(context, WoWTokenJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(WoWTokenJobService.WOWTOKEN_JOB_ID, tokenJobService);
        builder.setMinimumLatency(time_to_start * 1000 + 100);
        builder.setOverrideDeadline((time_to_start * 1000 + 100) * 2);
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobScheduler.schedule(builder.build());
    }
}
