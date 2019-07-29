package com.example.wowhqapp.repositories;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.example.wowhqapp.R;
import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.databases.dao.AuctionsDao;
import com.example.wowhqapp.databases.dao.BestLotsDao;
import com.example.wowhqapp.databases.entity.Auctions;
import com.example.wowhqapp.databases.entity.BestLot;
import com.example.wowhqapp.databases.entity.Lot;
import com.example.wowhqapp.databases.entity.SimpleLot;
import com.example.wowhqapp.network.AucAndTokenNetwork;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AuctionsRepo implements MainContract.AuctionsRepo {

    private AuctionsDao mAuctionsDao;
    private BestLotsDao mBestLotsDao;
    private List<Lot> mListOfLot;
    private MainContract.SettingRepository mSettingRepository;
    private CompositeDisposable mCompositeDisposable;
    private MainContract.AuctionsPresenter mAuctionsPresenter;
    private Boolean mTypeOfSource; //False - для аукциона
    //Удалены приватные переменные настроек, т.к. если выйти из аукционной активити (но не из приложения), то repo не уничтожится, и когда мы поменяем язык, то он сразуже обновит БД с лотами (что хорошо), но значения настроек для него останутся прежними

    //FilterFragment
    private ArrayList<ArrayList<Map<String, String>>> mChildrenArrayList; //Все коллекции дочерних элементов
    private ArrayList<Map<String, String>> mGropArrayList; //Все группы
    private ArrayList<Map<String, String>> mChildrenTempArrayList; //Хранит ссылку (временно) на одну коллекцию дочерних элементов
    private Map<String, String> mTempAttrMap; //Хранит ссылку (временно) на словарь атрибутов (группы или дочернего элемента)
    private String[][] mSchildrenNames = {{""},{},{}};

    public AuctionsRepo(MainContract.SettingRepository settingRepository ,Boolean is_bestlot_source ,MainContract.AuctionsPresenter auctionsPresenter){
        mAuctionsPresenter = auctionsPresenter;
        mCompositeDisposable = new CompositeDisposable();
        mAuctionsDao = WowhqApplication.getInstance().getAucAndTokenDatabase().getAuctionDao();
        mBestLotsDao = WowhqApplication.getInstance().getAucAndTokenDatabase().getBestLotsDao();
        mSettingRepository = settingRepository;
        mChildrenArrayList = null;
        mGropArrayList = null;

        mListOfLot = new ArrayList<>();
        mTypeOfSource = is_bestlot_source;

        if (!mTypeOfSource) {

            mCompositeDisposable.add(
                    mAuctionsDao.getAll().observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<SimpleLot>>() {
                                @Override
                                public void accept(List<SimpleLot> simpleLots) throws Exception {
                                    if (simpleLots.size() < 10) {
                                        mAuctionsPresenter.notifyLittleData();
                                    } else {
                                        mListOfLot.clear();
                                        for (SimpleLot simpleLot : simpleLots){
                                            mListOfLot.add((Lot) simpleLot);
                                        }
                                        mAuctionsPresenter.notifyUpdatedData();
                                    }
                                }
                            })
            );
        }else {
            mCompositeDisposable.add(
                    mBestLotsDao.getAll().observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<BestLot>>() {
                                @Override
                                public void accept(List<BestLot> bestLots) throws Exception {
                                    if (mListOfLot != null){
                                        mListOfLot.clear();
                                    }
                                    for (BestLot simpleLot : bestLots){
                                        mListOfLot.add((Lot) simpleLot);
                                    }
                                    mAuctionsPresenter.notifyUpdatedData();
                                }
                            })
            );
        }
    }

    @Override
    public List<Lot> getLots() {
        return mListOfLot;
    }

    @Override
    public void clearListOfLots() {
        mListOfLot.clear();
    }

    @Override
    public void downloadLots(final int page) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Auctions auctions = AucAndTokenNetwork.getInstance().getAuctionsAPI().getAuctionsByPage(mSettingRepository.getSlug(), mSettingRepository.getRegion(), mSettingRepository.getLang(), String.valueOf(page)).execute().body();
                Log.v(WowhqApplication.LOG_TAG,"[AuctionsRepo] Загруженны лоты в количестве: " +  String.valueOf(auctions.getAuctions().size()));
                Thread.sleep(300); //Задержка, чтобы крутился PB
                mAuctionsDao.insertList(auctions.getAuctions());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo]Успешно загружены и сохранены лоты " + mSettingRepository.getSlug() + ".Страница - " + String.valueOf(page));
            }

            @Override
            public void onError(Throwable e) {
                if (e.getClass() == ConnectException.class){
                    Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo][ОШИБКА] При загр. или сохр. лотов  ConnectException" );
                    if (page == 1) {
                        mAuctionsPresenter.onGetFirstPageError();
                    }
                }
                if (e.getClass() == NullPointerException.class || e.getCause() == null){
                    Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo][ОШИБКА] При загр. или сохр. лотов  NullPointerException" );
                    if (page >= 2){
                        mAuctionsPresenter.onAllAuctionsDownload();
                    }
                }
                Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo][ОШИБКА] При загр. или сохр. лотов " + mSettingRepository.getSlug() + ".Страница - " + String.valueOf(page) + "Cause: " + String.valueOf(e.getCause()));
                e.printStackTrace();
            }
        });
    }

    /**
     * Загрузка лотов с ключами фильтра. Также прямо из репозитория берутся категории (также в ключ).*/
    @Override
    public void downloadLots(int page, String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

            }
        }).observeOn()
    }

    @Override
    public void deleteAllLots() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAuctionsDao.deleteAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo][УСПЕХ] Удалены все лоты");
            }

            @Override
            public void onError(Throwable e) {
                Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo][ОШИБКА] При попытке удалить все лоты" + " Cause: " + String.valueOf(e.getMessage()));
            }
        });
    }

    @Override
    //Какак оказалось, совершенно
    public void destroy() {
        mCompositeDisposable.clear();
    }

    @Override
    public ArrayList<ArrayList<Map<String, String>>> getChildrenArrayList() {
        if (mChildrenArrayList == null){
            initChildrenArrayList();
        }
        return mChildrenArrayList;
    }

    private void initChildrenArrayList(){
        mChildrenArrayList = new ArrayList<ArrayList<Map<String, String>>>();
        Resources resources = WowhqApplication.getInstance().getResources();
        for (int i= 0; i<=15; i++){
            mChildrenTempArrayList = new ArrayList<Map<String,String>>();
            int id = resources.getIdentifier(String.format("filter_group%s_children_array_of_reference", String.valueOf(i)), "array", WowhqApplication.getInstance().getPackageName());
            for (String[] attr : getAttributeArray(id)){
                mTempAttrMap = new HashMap<String, String>();
                mTempAttrMap.put("subclass_name", attr[0]);
                mTempAttrMap.put("is_checked", "false");
                mTempAttrMap.put("subclass_id", attr[1]);
                mChildrenTempArrayList.add(mTempAttrMap);
            }
            mChildrenArrayList.add(mChildrenTempArrayList);
        }
    }

    @Override
    public ArrayList<Map<String, String>> getGroupArrayList() {
        if (mGropArrayList == null){
            initGroupArrayList();
        }
        return mGropArrayList;
    }

    private void initGroupArrayList(){
        mGropArrayList = new ArrayList<Map<String,String>>();
        for (String[] attr : getAttributeArray(R.array.filter_group_array_of_reference)){
            mTempAttrMap = new HashMap<String, String>();
            mTempAttrMap.put("class_name", attr[0]);
            mTempAttrMap.put("class_id", attr[1]);
            mGropArrayList.add(mTempAttrMap);
        }
    }

    @Override
    public void setExpandableChildrenCheckBox(int groupPosition, int childPosition, boolean isChecked) {
       mChildrenArrayList.get(groupPosition).get(childPosition).put("is_checked", String.valueOf(isChecked));
    }

    @Override
    public void resetExpandableListElements() {
        initGroupArrayList();
        initChildrenArrayList();
    }

    private String[][] getAttributeArray(int array_of_reference_id){
        Resources resources = WowhqApplication.getInstance().getResources();
        TypedArray typedArray;
        typedArray = resources.obtainTypedArray(array_of_reference_id);
        int array_of_reference_length = typedArray.length();
        String[][] array = new String[array_of_reference_length][];
        for (int i = 0; i<array_of_reference_length; i++){
            int element_id = typedArray.getResourceId(i, -1);
            if (element_id!=-1){
                array[i] = resources.getStringArray(element_id);
            }else {
                Log.v(WowhqApplication.LOG_TAG, "[AuctionsRepo] Can't get inner array");
            }
        }
        typedArray.recycle();
        return array;
    }
}
