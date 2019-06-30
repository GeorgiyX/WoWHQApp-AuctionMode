package com.example.wowhqapp.repositories;

import android.util.Log;

import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.databases.dao.WoWTokenDao;
import com.example.wowhqapp.databases.database.AucAndTokenDataBase;
import com.example.wowhqapp.databases.entity.WoWToken;
import com.example.wowhqapp.network.AucAndTokenNetwork;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.currentThread;

public class WoWTokenServiceRepo implements MainContract.TokenServiceRepository {

    private WoWTokenDao mWoWTokenDao;
    private String mRegion;
    private long current_price;


    public WoWTokenServiceRepo(String region){
        mRegion = region;
        mWoWTokenDao = AucAndTokenDataBase.getDB().getWoWTokenDao();
//        current_price = 0;
    }

    @Override
    public long saveWoWTokenAndGetCurrentPrice() {
        WoWToken woWToken = null;
//        Log.v(WowhqApplication.LOG_TAG, "saveWoWTokenAndGetCurrentPrice");
        try {
             woWToken = AucAndTokenNetwork.getInstance().getWoWTokenAPI().getWoWTokenByRegion(mRegion).execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert woWToken != null;
            woWToken.setCurrentPriceBlizzardApi(Double.valueOf(woWToken.getCurrentPriceBlizzardApi().doubleValue()*(Math.random()+0.7)).longValue());
            if (Math.random() < 0.5){
                woWToken.setLastChange(-woWToken.getLastChange());
            }
            woWToken.setLastChange(Double.valueOf(woWToken.getLastChange().doubleValue()*(Math.random()+0.8)).longValue());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        mWoWTokenDao.insert(woWToken);
        return woWToken.getCurrentPriceBlizzardApi();

//        AucAndTokenNetwork.getInstance().getWoWTokenAPI().getWoWTokenByRegion(mRegion).enqueue(
//                new Callback<WoWToken>() {
//                    @Override
//                    public void onResponse(Call<WoWToken> call, Response<WoWToken> response) {
//                        Log.v(WowhqApplication.LOG_TAG, "onResponse - получаем WoWToken");
//
//                        WoWToken woWToken = response.body();
//                        Log.v(WowhqApplication.LOG_TAG, "Данные полученные ИЗ СЕТИ: Регион: " + woWToken.getRegion() + "Текущая цена: " + woWToken.getCurrentPriceBlizzardApi());
//
//                        //Test code::
//                        try {
//                            assert woWToken != null;
//                            woWToken.setCurrentPriceBlizzardApi(Double.valueOf(woWToken.getCurrentPriceBlizzardApi().doubleValue()*(Math.random()+0.7)).longValue());
//                            if (Math.random() < 0.5){
//                                woWToken.setLastChange(-woWToken.getLastChange());
//                            }
//                            woWToken.setLastChange(Double.valueOf(woWToken.getLastChange().doubleValue()*(Math.random()+0.8)).longValue());
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        Log.v(WowhqApplication.LOG_TAG, "Данные токена, МОДИФИЦИРОВАННЫЕ: Регион: " + woWToken.getRegion() + "Текущая цена: " + woWToken.getCurrentPriceBlizzardApi());
//                        insertWoWToken(woWToken);
//                        setCurrentPice(woWToken.getCurrentPriceBlizzardApi());
//                    }
//
//                    @Override
//                    public void onFailure(Call<WoWToken> call, Throwable t) {
//                        Log.v(WowhqApplication.LOG_TAG, "Ошибка при запросе токена по API соеинения");
//                    }
//                }
//        );
//        //test_code:
////        Random random = new Random();
////        int i = random.nextInt(100 + 1) +1;
////        long num = 112*i;
////        WoWToken woWToken = new WoWToken("eu", "2019", num,num,num,num);
////        mWoWTokenDao.insert(woWToken);
////        long current_price = woWToken.getCurrentPriceBlizzardApi();
//        return current_price;


    }
    private void setCurrentPice(long currentPice){
        current_price = currentPice;
        Log.v(WowhqApplication.LOG_TAG, "[NOTYFICATION_DEBUG]Текущая цена по версии Repo, при попытке установить ее в поле (Service): "+String.valueOf(current_price));

    }
    private void insertWoWToken(final WoWToken woWToken){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mWoWTokenDao.insert(woWToken);
                Log.v(WowhqApplication.LOG_TAG, "ДобавлЯЕМ в БД WoWToken, поток: " + currentThread().getName());

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.v(WowhqApplication.LOG_TAG, "ДобавлИЛИ в БД WoWToken, поток: " + currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
