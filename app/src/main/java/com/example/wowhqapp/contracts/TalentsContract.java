package com.example.wowhqapp.contracts;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import com.example.wowhqapp.databases.entity.ITalentsEntity;
import com.example.wowhqapp.databases.entity.Talent;
import com.example.wowhqapp.databases.entity.WowClass;
import com.example.wowhqapp.databases.entity.WowSpec;
import com.example.wowhqapp.databases.entity.WowTalents;

import java.io.IOException;
import java.util.List;

public interface TalentsContract {
    public interface TalentsRepository {
        List<WowClass> getWowClasses();
        List<WowSpec> getWowSpecs(int classId);
        WowTalents getWowTalents(int specId);
        WowTalents getWowTalents();
        Talent getWowTalent(int id);
        void refresh() throws IOException;
    }

    public interface TalentsPresenter {
        void loadStage(boolean needAddToBackStack);
        void resetProgress();
        TalentsRepository getTalentsRepository();
        MainContract.SettingRepository getSettingRepository();
        String createImageUrl(ITalentsEntity iTalentsEntity);
    }

    public interface TalentsView {
        Resources getTalentsViewResources();
        View findOnTalentsViewById(int id);
        void loadFragment(Fragment fragment, boolean needAddToBackStack);
        TalentsPresenter getTalentsPresenter();
        String getTalentsTitle();
        void setTalentsTitle(String newTitle);
    }
}
