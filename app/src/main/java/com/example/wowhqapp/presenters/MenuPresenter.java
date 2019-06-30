package com.example.wowhqapp.presenters;

import com.example.wowhqapp.contracts.MainContract;

public class MenuPresenter implements MainContract.MenuPresenter {

    private final MainContract.MenuView mMenuView;

    public MenuPresenter(MainContract.MenuView menuView){
        mMenuView = menuView;
    }

    public void openAuctions() {
        mMenuView.openAuctionsActivity();
    }

    public void openDeals() {
        mMenuView.openDealsActivity();
    }

    public void openSavedLots() {
        mMenuView.openSavedLotsActivity();
    }

    public void openWoWToken() {
        mMenuView.openWoWTokenActivity();
    }

    public void openSetting() {
        mMenuView.openSettingActivity();
    }

    public void closeMenu() {
        mMenuView.closeActivity();
    }

    public void openTalants(){
        mMenuView.openTalantsActivity();
    }

}
