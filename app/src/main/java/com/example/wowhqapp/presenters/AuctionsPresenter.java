package com.example.wowhqapp.presenters;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.repositories.AuctionsRepo;
import com.example.wowhqapp.repositories.SettingRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AuctionsPresenter implements MainContract.AuctionsPresenter {

    private String[] mActivityTitles;

    private enum Mode {NORMAL, SEARCH, FILTER}
    public static final int SEARCH_FREEZE_TIME_MIlLS = 800;

    private SettingRepository mSettingRepository;
    private Boolean mTypeOfActivity; //False - для аукциона
    private Boolean mIsNetQueryEnable;
    private Boolean mIsRecyclerAdapterEnable;
    private Boolean mIsAllAuctionsDownload;
    private Boolean mIsProgressBarInit;
    private Boolean mIsProgressBarVisible;
    private Boolean mIsFilterApply;
    private Boolean mIsFilterNotDefault;


    private Integer mCurrentPage;
    private MainContract.AuctionsRepo mAuctionsRepo;
    private MainContract.AuctionsView mAuctionsView;
    private Mode mMode;

    private StringBuilder mSearchString;
    private ScheduledExecutorService mScheduledExecutorServiceSearch;
    private ScheduledFuture<?> mScheduledFutureSearch;
    //private MainContract.AuctionsListFragView mAuctionsListFragView;

    //FilterFragment - не нужно
    private ArrayList<ArrayList<Map<String, String>>> mChildrenArrayList; //Все коллекции дочерних элементов
    private ArrayList<Map<String, String>> mGroupArrayList; //Все группы
    private ArrayList<Map<String, String>> mChildrenTempArrayList; //Хранит ссылку (временно) на одну коллекцию дочерних элементов
    private Map<String, String> mTempAttrMap; //Хранит ссылку (временно) на словарь атрибутов (группы или дочернего элемента)
    private String mLevelStart;
    private String mLevelEnd;
    private String mPriceStart;
    private String mPriceEnd;
    private Integer mOrderPosition;



    public AuctionsPresenter(MainContract.AuctionsView auctionsView, SettingRepository settingRepository){
        mCurrentPage = 1;
        mAuctionsView = auctionsView;
        mSettingRepository = settingRepository;
        mIsNetQueryEnable = false;
        mIsRecyclerAdapterEnable = false;
        mIsAllAuctionsDownload = false;
        mIsProgressBarVisible=mIsProgressBarInit = false;
        mIsFilterApply = false;
        mIsFilterNotDefault = false;
        mMode = Mode.NORMAL;

        mSearchString = new StringBuilder();
        mScheduledExecutorServiceSearch =  Executors.newSingleThreadScheduledExecutor();
        mScheduledFutureSearch = null;

        mLevelStart = mLevelEnd = mPriceStart = mPriceEnd = null;
        mOrderPosition = null;
        mAuctionsRepo = null;
    }


    /**
     * 
     *
     * @param type тип - сделки/аукцион
     * @param titles заголовки в appbar из ресурсов
     * @param mNavMenuElements navdrawer элементы (устанавливаются в checked)
     */
    @Override
    public void init(Boolean type, String[] titles, Integer[] mNavMenuElements) {
        mTypeOfActivity = type;
        mActivityTitles = titles;
        mAuctionsView.setCheckedMenuItem(type ? mNavMenuElements[0] : mNavMenuElements[1]);
        if (mAuctionsRepo == null){
            mAuctionsRepo = new AuctionsRepo(mSettingRepository, mTypeOfActivity, this); //инициализация репо происходит в этом методе, т.к.
        }
        if (mMode == Mode.NORMAL){ // Normal = аук и выгодные лоты.
            mAuctionsView.setTitle(mTypeOfActivity ? mActivityTitles[0] : mActivityTitles[1] + " - " + mSettingRepository.getSlug());
            mAuctionsView.setAuctionFragment(type);
            //Проверим, если разрешены сетевые запросы (нажали "обновить" или репо заметил что данных нет, докачал и разблокировал сетевые запросы (нужно ДОДЕЛАТЬ))
            // - значит метод init() вызывается повторно. Будем добавялть progBar который исчесзает при повороте.
            if (mIsNetQueryEnable){
                mAuctionsView.addProgressBar();
                mAuctionsView.hideProgressBar();
                if (mIsProgressBarVisible){ //включим видимость progbar'a если положенно
                    mAuctionsView.showProgressBar();
                }
            }
        }
        if (mMode == Mode.FILTER){
            mAuctionsView.disableDrawer();
            mAuctionsView.setTitle(mActivityTitles[2]);
            mAuctionsView.showArrowBack();
        }
    }

    @Override
    public void setAuctionsView(MainContract.AuctionsView auctionsView) {
        mAuctionsView = auctionsView;
    }

    //TODO по большей части этот код не актуален (можно дописать чтобы срабатывал он на  onKeyDown - KEYCODE_MENU, похоже для устройств вроде S3 с копкой меню )
    @Override
    public void onHamburgerClick() {
        mAuctionsView.toggleDrawer();
    }

    @Override
    public void notifyUpdatedData() {
        if (!mIsRecyclerAdapterEnable){
            mAuctionsView.initAdapter(mAuctionsRepo.getLots());
            mIsRecyclerAdapterEnable = true;
        }
        else {
            mAuctionsView.notifyAuctionsChange();
            if (mIsProgressBarVisible){
                mAuctionsView.hideProgressBar();
                mIsProgressBarVisible=false;
            }
        }

    }

    @Override
    public void notifyLittleData() { //repo уведомляет presenter, мб стоит его
        if (!mIsRecyclerAdapterEnable || mAuctionsRepo.getLots().size() == 0) {
            mAuctionsRepo.deleteAllLots();
            mAuctionsRepo.downloadLots(1);
            if (mIsProgressBarVisible) {
                mAuctionsView.hideProgressBar();
                mIsProgressBarVisible = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        mAuctionsRepo.destroy();
    }

    @Override
    public void onScrollDown() {
        if (mIsNetQueryEnable){
            mCurrentPage++;
            if(!mIsAllAuctionsDownload){
                mAuctionsView.showProgressBar();
                mIsProgressBarVisible = true;
            }
            mAuctionsRepo.downloadLots(mCurrentPage);
        }
    }

    @Override
    public void onScrollDownNotToEnd() {
        mAuctionsView.hideLoadNewDataBtn();
    }

    @Override
    public void onScrollUp() {
        if (!mIsNetQueryEnable){
            mAuctionsView.showLoadNewDataBtn();
        }
    }

    @Override
    public void onLoadNewDataBtnClick() {
        mIsNetQueryEnable = true;
        mAuctionsRepo.deleteAllLots();
        mAuctionsRepo.clearListOfLots();
        mAuctionsView.hideLoadNewDataBtn();
        mAuctionsView.notifyAuctionsChange();
        if (!mIsProgressBarInit){
            mAuctionsView.addProgressBar();
            mIsProgressBarInit = true;
        }
        if (!mIsProgressBarVisible){
            mAuctionsView.showProgressBar();
            mIsProgressBarVisible =true; //!!!
        }
    }

    @Override
    public void onGetFirstPageError() {
        mAuctionsView.showErrorView();
    }

    @Override
    public void onAllAuctionsDownload() {
        mIsAllAuctionsDownload = true;
        if (mIsProgressBarVisible){
            mAuctionsView.hideProgressBar();
            mIsProgressBarVisible = false;
        }
    }

    @Override
    public String onGetGlideUrl(String icon_name) {
        return "http://media.blizzard.com/wow/icons/56/" + icon_name + ".jpg";
    }

    @Override
    public void onSearchExpand() {
        mAuctionsView.disableDrawer();
        mAuctionsView.hideToolBarIcons();
        mMode = Mode.SEARCH;
    }

    @Override
    public void onSearchCollapse() {
        mAuctionsView.enableDrawer();
        mAuctionsView.showToolBarIcons();
        mMode = Mode.NORMAL;
    }

    @Override
    public void onQueryTextChange(String text) {
        if (text.equals(mSearchString.toString())){
            return;
        }else {
            mSearchString.delete(0, mSearchString.length());
            mSearchString.append(text);
            if (!mIsNetQueryEnable){
                mIsNetQueryEnable = true;
                if (!mIsProgressBarInit){
                    mAuctionsView.addProgressBar();
                    mAuctionsView.hideProgressBar();
                    mIsProgressBarInit = true;
                }
            }
            if (mSearchString.length() > 3)
                if(mScheduledFutureSearch == null || mScheduledFutureSearch.isDone()) {
                    mScheduledFutureSearch = mScheduledExecutorServiceSearch.schedule(new Runnable() {
                        @Override
                        public void run() {
                            mAuctionsRepo.deleteAllLots();
                            mAuctionsRepo.downloadLots((int)(Math.random()*6+1));
                        }
                    }, SEARCH_FREEZE_TIME_MIlLS, TimeUnit.MILLISECONDS);
                    mCurrentPage = 1;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == Mode.SEARCH){
            mAuctionsView.collapseActionView();
            mMode = Mode.NORMAL;
        }else if(mMode == Mode.FILTER){
            mAuctionsView.enableDrawer();
            mAuctionsView.showToolBarIcons();
            mAuctionsView.onBackPressedSuper();
//            mAuctionsView.initAdapter(mAuctionsRepo.getLots()); //Связано с lifecycle Fragment'a, т.к. инициазация адаптера вынесена из этих методов....
                                                                //Т.к. при возврате с backstack перевызывается onCreateView, похоже стоит использовать какие-либо методы для сохранения состояния  [https://medium.com/@jacquesgiraudel/problem-with-restoring-fragments-from-the-backstack-945dc3f7f5a5]
            mAuctionsView.setTitle(mTypeOfActivity ? mActivityTitles[0] : mActivityTitles[1] + " - " + mSettingRepository.getSlug());
            mMode = Mode.NORMAL;
        }
        else {
            mAuctionsView.onBackPressedSuper();
        }
    }


    /**
     * Эта функция вызывается в одноменной функции Activity. Выполняет код связанный с отображением значков AppBar'a, (ранее он был в init,
     * проблема - init() вызывается в onCreate() до инициализации AppBar'a (а вызвать init() позже onCreate() также приводит к трудностям (НУЖНО ИЗУЧИТЬ)
     * ИЗУЧЕННО - фрагмент создается, но вызываются методы его ЖЦ, и соответственно, мы натыкаемся на NullPointer при попытке установить адаптер
     * в RV)
     */
    @Override
    public void onCreateOptionsMenu() {
        if (mMode == Mode.FILTER){
            mAuctionsView.hideToolBarIcons();
        }
    }

    @Override
    public void onFilterClick() {
        mAuctionsView.disableDrawer();
        mAuctionsView.hideToolBarIcons();
        mAuctionsView.setTitle(mActivityTitles[2]);
        mAuctionsView.showArrowBack();
        mAuctionsView.setFilterFragment();
        mMode = Mode.FILTER;
    }

    @Override
    public void onFilterApplyButtonClick() {
        //TODO  загрузить данные с новыми фильтром, если надо  - переключить isNetQueryEnable, установить счетчик страниц = 1.
    }

    @Override
    public void initFilterFragment() {
        mAuctionsView.setFilterAdapterToExpandableListViewAndSetHeader(mAuctionsRepo.getGroupArrayList(), mAuctionsRepo.getChildrenArrayList(),mLevelStart, mLevelEnd, mPriceStart, mPriceEnd, mOrderPosition);
//        mAuctionsView.setFilterResetButtonVisibility(mIsFilterNotDefault);
    }

    @Override
    public void onFilterChildCategoryClick(int groupPosition, int childPosition, boolean isChecked) {
        mIsFilterNotDefault = true;
        mAuctionsView.setFilterResetButtonVisibility(mIsFilterNotDefault);
        mAuctionsRepo.setExpandableChildrenCheckBox(groupPosition, childPosition, isChecked);
    }

    @Override
    public void onInputViewChange(String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition) {
        if(endLevel.equals("")){
            endLevel = null;
        }else {
            try {
                Integer.valueOf(endLevel);
            }catch (Exception e){
                mAuctionsView.showFilterInputErrorToast();
                endLevel = null;
            }
        }
        if(endPrice.equals("")){
            endPrice = null;
        }else {
            try {
                Integer.valueOf(endPrice);
            }catch (Exception e){
                mAuctionsView.showFilterInputErrorToast();
                endPrice = null;
            }
        }
        if(statLevel.equals("")){
            statLevel = null;
        }else {
            try {
                Integer.valueOf(statLevel);
            }catch (Exception e){
                mAuctionsView.showFilterInputErrorToast();
                statLevel = null;
            }
        }
        if(starPrice.equals("")){
            starPrice = null;
        }else {
            try {
                Integer.valueOf(starPrice);
            }catch (Exception e){
                mAuctionsView.showFilterInputErrorToast();
                starPrice = null;
            }
        }

        mLevelStart = statLevel;
        mLevelEnd = endLevel;
        mPriceStart = starPrice;
        mPriceEnd = endPrice;
        mOrderPosition = orderPosition;
//        mAuctionsView.setFilterResetButtonVisibility(mIsFilterNotDefault);
    }

    @Override
    public void onFilterResetButtonClick() {
        mIsFilterApply = mIsFilterNotDefault = false;
        mCurrentPage = 1;
        mAuctionsRepo.resetExpandableListElements();
        mLevelStart = mLevelEnd = mPriceStart = mPriceEnd = null;
        mOrderPosition = null;
        mAuctionsView.setFilterAdapterToExpandableListViewAndSetHeader(mAuctionsRepo.getGroupArrayList(), mAuctionsRepo.getChildrenArrayList(),mLevelStart, mLevelEnd, mPriceStart, mPriceEnd, mOrderPosition);
        mAuctionsView.setFilterResetButtonVisibility(mIsFilterNotDefault);
    }
}
