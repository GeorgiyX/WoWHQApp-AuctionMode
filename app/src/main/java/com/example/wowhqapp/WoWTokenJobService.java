package com.example.wowhqapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.presenters.WoWTokenServicePresenter;
import com.example.wowhqapp.repositories.SettingRepository;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.currentThread;

public class WoWTokenJobService extends JobService implements MainContract.WoWTokenService {

    public static final int WOWTOKEN_JOB_ID = 1;
    private MainContract.SettingRepository mSettingRepository;
    private BroadcastReceiver mBroadcastReceiver;
    private JobParameters mParams;

    public static final String IS_SUCCESS = "IS_SUCCESS"; //нужен ли перезапуск
    public static final String WOWTOKEN_LOCAL_RECEIVER = "WOWTOKEN_LOCAL_RECEIVER";



    public static final String ACTION = "TOKEN_SERVICE";
    public static final String CHANNEL = "DEF";
    public static final Integer NOTIFICATION_ID = 1;
    private static final long[] VIBRATION_PATTERN = {0, 1000, 50, 100};
    public static final String IS_FROM_ACTIVITY = BuildConfig.APPLICATION_ID + "_IS_FROM_ACTIVITY";
    private MainContract.WoWTokenServicePresenter mWoWTokenServicePresenter;
    private NotificationManager mNotificationManager;
    private boolean mIsSuccess;


    @Override
    public void onCreate() {
        super.onCreate();
        mSettingRepository =  (MainContract.SettingRepository) new SettingRepository(getSharedPreferences(SettingRepository.APP_PREFERENCES, Context.MODE_PRIVATE));
        mIsSuccess = false;
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isSuccess = false;
                if(intent.hasExtra(IS_SUCCESS)) {
                    isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);
                }
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                Log.v(WowhqApplication.LOG_TAG, "jobFinished (из ресивера). - Job Сервис, Поток: " + currentThread().getName());
                jobFinished(mParams, !isSuccess);
            }
        };

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mWoWTokenServicePresenter = new WoWTokenServicePresenter(new SettingRepository(getSharedPreferences(SettingRepository.APP_PREFERENCES, Context.MODE_PRIVATE)),this);
        initChannel();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mParams = params;
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(WOWTOKEN_LOCAL_RECEIVER));
        Log.v(WowhqApplication.LOG_TAG, "onStartJob (до попытки запуска). - Job Сервис, Поток: " + currentThread().getName());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !WoWTokenService.isAppOnForeground(getApplicationContext())) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Log.v(WowhqApplication.LOG_TAG, "Запускаем работу в JobService, Поток: " + currentThread().getName());
                    mIsSuccess = mWoWTokenServicePresenter.init();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {
                    Log.v(WowhqApplication.LOG_TAG, "jobFinished (onStartJob). - Job Сервис, Поток: " + currentThread().getName());
                    jobFinished(mParams, !mIsSuccess);
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }
        else {
            WoWTokenService.startService(getApplicationContext());
        }

        if (WoWTokenService.isAppOnForeground(getApplicationContext()) || mSettingRepository.getWoWTokenServiceEnable()){
            Log.v(WowhqApplication.LOG_TAG, "Запланировали запуск сервиса - Job Сервис, Поток: " + currentThread().getName());
            Util.scheduleWoWTokenJob(getApplicationContext(), 12);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWoWTokenServicePresenter.destroy();
    }

    @Override
    public void makeNotification(long current_price, String region) {
        Log.v(WowhqApplication.LOG_TAG, "==Уведомление [JobService], текущая цена: " + String.valueOf(current_price) +"==");
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


    public void initChannel() {
        if (Build.VERSION.SDK_INT < 26)
            return;

        NotificationChannel defaultChannel = new NotificationChannel(CHANNEL,
                "DEFAULT CHANNEL, WOWTOKEN", NotificationManager.IMPORTANCE_DEFAULT);
        defaultChannel.setVibrationPattern(VIBRATION_PATTERN);
        defaultChannel.enableVibration(true);
        mNotificationManager.createNotificationChannel(defaultChannel);
    }
}
