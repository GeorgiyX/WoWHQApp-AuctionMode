package com.example.wowhqapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.databases.entity.Lot;
import com.example.wowhqapp.fragments.auctions.AuctionListFragment;
import com.example.wowhqapp.fragments.auctions.AuctionsFilterFragment;
import com.example.wowhqapp.presenters.AuctionsPresenter;
import com.example.wowhqapp.repositories.SettingRepository;
import com.example.wowhqapp.utils.BaseDrawerActivityWithToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuctionAndDealsActivity extends BaseDrawerActivityWithToolBar implements MainContract.AuctionsView, AuctionListFragment.OnListFragmentInteractionListener, AuctionsFilterFragment.OnFragmentInteractionListener {

    private AuctionsPresenter mAuctionsPresenter;
    private Button mLoadNewDataButton;
    private AuctionListFragment mAuctionsFragment;
    private AuctionsFilterFragment mAuctionsFilterFragment;
    private ContentLoadingProgressBar mContentLoadingProgressBar;
    private ConstraintLayout mConstraintLayout;
    private final Integer[] mNavMenuElements = {R.id.nav_auctions_best , R.id.nav_auctions};
    private MenuItem mSearchMenuItem;
    private MenuItem mFilterMenuItem;
    private SearchView mSearchView;
    private final String AUC_FILTER_FRAGMENT_TAG = "Auction Filter Fragment";
    private final String AUC_FRAGMENT_TAG = "Auction Fragment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_auction_and_deals);
        super.onCreate(savedInstanceState);

        mAuctionsFragment = null;
        mAuctionsFilterFragment = null;

        //Восстановление фрагментов и Presenter'a
        mAuctionsPresenter = (AuctionsPresenter) getLastCustomNonConfigurationInstance();
        mAuctionsFilterFragment = (AuctionsFilterFragment) getSupportFragmentManager().findFragmentByTag(AUC_FILTER_FRAGMENT_TAG);
        mAuctionsFragment = (AuctionListFragment) getSupportFragmentManager().findFragmentByTag(AUC_FRAGMENT_TAG);
        if (mAuctionsFilterFragment != null) {
            mAuctionsFilterFragment.setContextAndListener(this);
        }
        if (mAuctionsFragment != null) {
            mAuctionsFragment.setContextAndListener(this);
        }
        if (mAuctionsPresenter == null){
            Log.v(WowhqApplication.LOG_TAG, "[AuctionAndDealsActivity] Create Presenter");
            mAuctionsPresenter = new AuctionsPresenter(this, new SettingRepository(getSharedPreferences(SettingRepository.APP_PREFERENCES, Context.MODE_PRIVATE)));
        }else {
            //Нужно сменить ссылку на Activity, так как в старом presenter'e ссыль на старую Activity
            mAuctionsPresenter.setAuctionsView(this);
        }

        mAuctionsPresenter.init(getIntent().getExtras().getBoolean(MenuActivity.KEY_TYPE), getResources().getStringArray(R.array.auction_title), mNavMenuElements);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case android.R.id.home:
//                Log.v(WowhqApplication.LOG_TAG, "[AuctionAndDealsActivity] onOptionsItemSelected: home");
//
//                mAuctionsPresenter.onHamburgerClick();
//                //....
//                break;
            case R.id.auctions_menu_filter:
                mAuctionsPresenter.onFilterClick();
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auction_menu, menu);
        mSearchMenuItem = menu.findItem(R.id.auctions_menu_search);
        mFilterMenuItem = menu.findItem(R.id.auctions_menu_filter);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mAuctionsPresenter.onCreateOptionsMenu();

        mSearchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.v(WowhqApplication.LOG_TAG, "onMenuItemActionExpand");
                mAuctionsPresenter.onSearchExpand();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.v(WowhqApplication.LOG_TAG, "onMenuItemActionCollapse");

                mAuctionsPresenter.onSearchCollapse();
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAuctionsPresenter.onQueryTextChange(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setAuctionFragment(Boolean type) {
        if (mAuctionsFragment == null){
            mAuctionsFragment = AuctionListFragment.newInstance(type);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                    .add(R.id.auctions_frame_lay, mAuctionsFragment, AUC_FRAGMENT_TAG).commit();
            Log.v(WowhqApplication.LOG_TAG, "AuctionsAndDealsActivity - add AucListFragment");
        }
        if (!type){
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            mConstraintLayout = (ConstraintLayout) layoutInflater.inflate(R.layout.load_btn, null, false);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.auctions_frame_lay_load_more_btn);
            frameLayout.addView(mConstraintLayout);
            mLoadNewDataButton = (Button) mConstraintLayout.findViewById(R.id.auctions_load_btn);
            mLoadNewDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuctionsPresenter.onLoadNewDataBtnClick();
                }
            });
            Log.v(WowhqApplication.LOG_TAG, "[LOAD NEW AUCS BTN] add constraintLayout");
        }
    }

    @Override
    public void setFilterFragment() {
        if (mAuctionsFilterFragment == null){
            mAuctionsFilterFragment = new AuctionsFilterFragment();
            Log.v(WowhqApplication.LOG_TAG, "[AuctionAndDealsActivity] - new AuctionsFilterFragment");
        }
        if (getSupportFragmentManager().findFragmentById(R.id.auctions_frame_lay).getClass() != AuctionsFilterFragment.class){
            getSupportFragmentManager().beginTransaction().
                    setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).
                    remove(mAuctionsFragment).
                    add(R.id.auctions_frame_lay, mAuctionsFilterFragment, AUC_FILTER_FRAGMENT_TAG).
                    addToBackStack(null).commitAllowingStateLoss();
        }
    }

    @Override
    public void initAdapter(List<Lot> lotList) {
        mAuctionsFragment.initAdapter(lotList);
    }


    @Override
    public void notifyAuctionsChange() {
        mAuctionsFragment.notifyAuctionsChange();
    }

    @Override
    public void showLoadNewDataBtn() {
        mConstraintLayout.animate()
                .alpha(1.0f)
                .setDuration(50)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLoadNewDataButton.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void hideLoadNewDataBtn() {
        mConstraintLayout.animate()
                .alpha(0.0f)
                .setDuration(50)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLoadNewDataButton.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void showErrorView() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) layoutInflater.inflate(R.layout.auction_error_layout, null, false);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.auctions_frame_lay_error);
        frameLayout.addView(constraintLayout);
    }

    @Override
    public void addProgressBar() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) layoutInflater.inflate(R.layout.loading_view, null, false);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.auctions_frame_lay_loading_view);
        frameLayout.addView(constraintLayout);
        mContentLoadingProgressBar = (ContentLoadingProgressBar) constraintLayout.findViewById(R.id.auctions_LoadingProgressBar);
    }

    @Override
    public void showProgressBar() {
        mContentLoadingProgressBar.show();
    }

    @Override
    public void hideProgressBar() {
        mContentLoadingProgressBar.hide();
    }

    @Override
    public void hideToolBarIcons() {
        mSearchMenuItem.setVisible(false);
        mFilterMenuItem.setVisible(false);
    }

    @Override
    public void showToolBarIcons() {
        mSearchMenuItem.setVisible(true);
        mFilterMenuItem.setVisible(true);
    }

    @Override
    public void collapseActionView() {
        mSearchMenuItem.collapseActionView();
    }

    @Override
    public void onScrollDown() {
        Log.v(WowhqApplication.LOG_TAG, "[LOAD NEW AUCS BTN] onScrollDown");
        mAuctionsPresenter.onScrollDown();
    }

    @Override
    public void onScrollUp() {
        mAuctionsPresenter.onScrollUp();
    }

    @Override
    public void onScrollDownNotToEnd() {
        mAuctionsPresenter.onScrollDownNotToEnd();
    }

    @Override
    public String onGlideGetUrl(String icon_name) {
        return mAuctionsPresenter.onGetGlideUrl(icon_name);
    }

    @Override
    public void onListFragmentInteraction(Lot lot) {
        Log.v(WowhqApplication.LOG_TAG, "onListFragmentInteraction - это обработчик нажатия на элемент списка (Лот), AuctionAndDeals Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()){
            Log.v(WowhqApplication.LOG_TAG, "[AuctionsAndDealsActivity] - onDestroy (Реально завершается)");
            mAuctionsPresenter.onDestroy();
        }else {
            Log.v(WowhqApplication.LOG_TAG, "[AuctionsAndDealsActivity] - onDestroy (Вызвался при повороте)");
        }
    }

    @Override
    public void onBackPressedSuper(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mAuctionsPresenter.onBackPressed();
//        super.onBackPressed();
    }

    @Override
    public void setFilterAdapterToExpandableListViewAndSetHeader(ArrayList<Map<String, String>> groupArrayList, ArrayList<ArrayList<Map<String, String>>> childrenArrayList, String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition) {
        if (mAuctionsFilterFragment != null){
            mAuctionsFilterFragment.setFilterAdapterToExpandableListViewAndSetHeader(groupArrayList, childrenArrayList, statLevel, endLevel, starPrice, endPrice, orderPosition);
        }else {
            Log.v(WowhqApplication.LOG_TAG, "[AuctionsAndDealsActivity] Can't setAdapter to ExpandableListView, mAuctionsFilterFragment == null");
        }
    }

    @Override
    public void setFilterResetButtonVisibility(boolean isVisible) {
        mAuctionsFilterFragment.setFilterResetButtonVisibility(isVisible);
    }

    @Override
    public void showFilterInputErrorToast() {
        mAuctionsFilterFragment.showFilterInputErrorToast();
    }

    @Override
    public void hideFilterKeyBoard() {
        mAuctionsFilterFragment.hideFilterKeyBoard();
    }

    @Override
    public void onFragmentAuctionFilterInteraction(Uri uri) {
        Log.v(WowhqApplication.LOG_TAG, "[AuctionsAndDealsActivity] onFragmentAuctionFilterInteraction");

    }

    @Override
    public void inInitFilterFragment() {
        mAuctionsPresenter.initFilterFragment();
    }

    @Override
    public void onFilterChildCategoryClick(int groupPosition, int childPosition, boolean isChecked) {
        Log.v(WowhqApplication.LOG_TAG, "[AuctionsAndDealsActivity] onFilterChildCategoryClick " + isChecked);
        mAuctionsPresenter.onFilterChildCategoryClick(groupPosition, childPosition, isChecked);
    }

    @Override
    public void onApplyButtonClick() {
        mAuctionsPresenter.onFilterApplyButtonClick();
    }

    @Override
    public void onInputViewChange(String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition) {
        mAuctionsPresenter.onInputViewChange(statLevel, endLevel, starPrice, endPrice, orderPosition);
    }

    @Override
    public void onResetButtonClick() {
        mAuctionsPresenter.onFilterResetButtonClick();
    }

    @Override
    public void onBackArrowClick() {
        mAuctionsPresenter.onBackPressed();
    }

    @Override //Сохраним Presenter при повороте:
    public Object onRetainCustomNonConfigurationInstance() {
        return mAuctionsPresenter;
    }
}
