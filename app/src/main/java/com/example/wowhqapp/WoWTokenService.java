package com.example.wowhqapp;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.presenters.WoWTokenServicePresenter;
import com.example.wowhqapp.repositories.SettingRepository;

import java.util.List;

import static java.lang.Thread.currentThread;


public class WoWTokenService extends IntentService implements MainContract.WoWTokenService {

    public static final String ACTION = "TOKEN_SERVICE";
    public static final String CHANNEL = "DEF";
    public static final Integer NOTIFICATION_ID = 1;
    private static final long[] VIBRATION_PATTERN = {0, 1000, 50, 100};

    public static final String IS_FROM_ACTIVITY = BuildConfig.APPLICATION_ID + "_IS_FROM_ACTIVITY";

    private MainContract.WoWTokenServicePresenter mWoWTokenServicePresenter;
    private NotificationManager mNotificationManager;
    private boolean mIsSuccess;


    public WoWTokenService() {
        super("WoWToken IntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(WowhqApplication.LOG_TAG, "onCreate - Сервис, Поток: " + currentThread().getName());
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mWoWTokenServicePresenter = new WoWTokenServicePresenter(new SettingRepository(getSharedPreferences(SettingRepository.APP_PREFERENCES, Context.MODE_PRIVATE)),this);
        initChannel();
    }

//    @Override
//    // onStartCommand дожена выполняться в основном потоке, в отличии от onHandleIntent
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v(WowhqApplication.LOG_TAG, "onStartCommand - Сервис, Поток: " + currentThread().getName());
//        return Service.START_REDELIVER_INTENT; //В случае прерывания должен перезапуститься
//    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Могуть быть траблы с перезапуском
        mIsSuccess = false;
        if (intent != null){
            final String action = intent.getAction();
            if (action != null && action.equals(ACTION)){
                //Делаем работу
                Log.v(WowhqApplication.LOG_TAG, "Запускаем работу в Intent Service, Поток: " + currentThread().getName());
                //Обработка в presenter?
                try {
                    mIsSuccess = mWoWTokenServicePresenter.init();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        sendSuccess();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWoWTokenServicePresenter.destroy();
        Log.v(WowhqApplication.LOG_TAG, "onDestroy - Сервис");

    }

    @Override
    public void makeNotification(long current_price, String region) {
        Log.v(WowhqApplication.LOG_TAG, "==Уведомление [Intent Service], текущая цена: " + String.valueOf(current_price) +"==");

        Intent intent = new Intent(this, WowTokenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_arrow_up_24)
                .setContentTitle(getResources().getText(R.string.token_notification_title))
                .setContentText(getResources().getText(R.string.token_notification_text) + region + " - " + String.valueOf(current_price))
                .setTicker(getResources().getText(R.string.token_notification_text) + region + " - " + String.valueOf(current_price))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID,builder.build());

    }

    /**
     * Если Android 8.0+ то нужно устанавливать канал
     */
    public void initChannel() {
        if (Build.VERSION.SDK_INT < 26)
            return;

        NotificationChannel defaultChannel = new NotificationChannel(CHANNEL,
                "DEFAULT CHANNEL, WOWTOKEN", NotificationManager.IMPORTANCE_DEFAULT);
        defaultChannel.setVibrationPattern(VIBRATION_PATTERN);
        defaultChannel.enableVibration(true);
        mNotificationManager.createNotificationChannel(defaultChannel);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, WoWTokenService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            Log.v(WowhqApplication.LOG_TAG, "Importance: "+appProcess.importance);
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND  ) && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void stopService() {
        stopSelf();
    }

    public void sendSuccess() {
        //Делаем рассылку что задача окончена  для норм завершения задачи
        Intent intentBroadcast = new Intent(WoWTokenJobService.WOWTOKEN_LOCAL_RECEIVER);
        intentBroadcast.putExtra(WoWTokenJobService.IS_SUCCESS, mIsSuccess);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
    }
}
