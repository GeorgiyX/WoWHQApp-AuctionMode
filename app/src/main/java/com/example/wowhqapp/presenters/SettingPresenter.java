package com.example.wowhqapp.presenters;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.repositories.SettingRepository;

public class SettingPresenter implements MainContract.SettingPresenter {

    private SettingRepository mSettingRepository;
    private MainContract.SettingView mSettingView;

    public SettingPresenter(SettingRepository repository, MainContract.SettingView settingView){
        mSettingRepository = repository;
        mSettingView = settingView;
    }

    @Override
    public void init() {
        mSettingView.SetSpinnerAdapter(mSettingRepository.getRegion());
        mSettingView.SetSpinnerValues(mSettingRepository.getSlug(), mSettingRepository.getRegion(), mSettingRepository.getLang());
    }

    @Override
    public void onSlugSelect(String value) {
        if (!mSettingRepository.getSlug().equals(value)){
            mSettingRepository.deleteAllSimpleLots();
        }
        mSettingRepository.setSlug(value);
    }

    @Override
    public void onRegionSelect(String value) {
        if (!mSettingRepository.getRegion().equals(value)){
            mSettingRepository.deleteAllSimpleLots();
        }
        mSettingRepository.setRegion(value);
    }

    @Override
    public void onLangSelect(String value) {
        if (!mSettingRepository.getLang().equals(value)){
            mSettingRepository.deleteAllSimpleLots();
        }
        mSettingRepository.setLang(value);
    }

    @Override
    public void onMenuItemSelected() {
        mSettingView.CloseSetting();
    }


}
