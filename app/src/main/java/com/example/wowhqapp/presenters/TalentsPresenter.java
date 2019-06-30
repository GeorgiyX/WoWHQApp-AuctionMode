package com.example.wowhqapp.presenters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.example.wowhqapp.R;
import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.contracts.TalentsContract;
import com.example.wowhqapp.databases.entity.ITalentsEntity;
import com.example.wowhqapp.fragments.talents.TalentsWowClassesFragment;
import com.example.wowhqapp.fragments.talents.TalentsWowSpecsFragment;
import com.example.wowhqapp.fragments.talents.TalentsWowTalentInfoFragment;
import com.example.wowhqapp.fragments.talents.TalentsWowTalentsFragment;
import com.example.wowhqapp.repositories.TalentsRepository;

import java.io.IOException;
import java.util.Random;

public class TalentsPresenter implements TalentsContract.TalentsPresenter {

    MainContract.SettingRepository mSettingRepository;
    TalentsContract.TalentsRepository mTalentsRepository;
    TalentsContract.TalentsView mTalentsView;

    public TalentsPresenter(MainContract.SettingRepository settingRepository, Context talentsView) {
        mSettingRepository = settingRepository;
        mTalentsRepository = new TalentsRepository(talentsView, mSettingRepository);
        mTalentsView = (TalentsContract.TalentsView) talentsView; // TODO
    }

    @Override
    public void loadStage(boolean needAddToBackStack) {
        new TalentsPresenterAsyncLoader().execute(needAddToBackStack);
    }

    public Fragment selectFragment() {
        if (mSettingRepository.getTalentsWowTalentId() != -1) {
            return new TalentsWowTalentInfoFragment();

        } else if (mSettingRepository.getTalentsWowSpecId() != -1 &&
            mSettingRepository.getTalentsWowSpecOrder() != -1
        ) {
            return new TalentsWowTalentsFragment();

        } else if (mSettingRepository.getTalentsWowClassId() != -1) {
            return new TalentsWowSpecsFragment();
        }

        return new TalentsWowClassesFragment();
    }

    @Override
    public void resetProgress() {
        mSettingRepository.setTalentsWowClassId(-1);
        mSettingRepository.setTalentsWowSpecId(-1);
        mSettingRepository.setTalentsWowSpecOrder(-1);
        mSettingRepository.setTalentsWowTalentId(-1);

        String defaultTitle = mTalentsView.getTalentsViewResources().getString(R.string.talents_a_bar_title_text);
        mTalentsView.setTalentsTitle(defaultTitle);
    }

    @Override
    public TalentsContract.TalentsRepository getTalentsRepository() {
        return mTalentsRepository;
    }

    @Override
    public MainContract.SettingRepository getSettingRepository() {
        return mSettingRepository;
    }

    @Override
    public String createImageUrl(ITalentsEntity iTalentsEntity) {
        return "http://media.blizzard.com/wow/icons/56/" + iTalentsEntity.getIcon() + ".jpg";
    }

    class TalentsPresenterAsyncLoader extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            String oldTalentLang = mSettingRepository.getTalentsLang();
            String currentLang = mSettingRepository.getLang();
            if (!currentLang.equals("no lang") &&
                    !currentLang.equals(oldTalentLang)) {
                mSettingRepository.setTalentsLang(currentLang);
                try {
                    mTalentsRepository.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                    mSettingRepository.setTalentsLang(oldTalentLang);
                }
            }
            return booleans[0];
        }

        @Override
        protected void onPostExecute(Boolean needAddToBackStack) {
            super.onPostExecute(needAddToBackStack);
            mTalentsView.loadFragment(selectFragment(), needAddToBackStack);
        }

    }
}
