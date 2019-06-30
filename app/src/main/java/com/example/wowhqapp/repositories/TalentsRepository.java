package com.example.wowhqapp.repositories;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.contracts.TalentsContract;
import com.example.wowhqapp.databases.dao.WowClassDao;
import com.example.wowhqapp.databases.dao.WowSpecDao;
import com.example.wowhqapp.databases.dao.WowTalentDao;
import com.example.wowhqapp.databases.database.TalentsCalculatorDatabase;
import com.example.wowhqapp.databases.entity.Talent;
import com.example.wowhqapp.databases.entity.WowClass;
import com.example.wowhqapp.databases.entity.WowSpec;
import com.example.wowhqapp.databases.entity.WowTalents;
import com.example.wowhqapp.network.ApiTalentsRepository;
import com.example.wowhqapp.network.WowClassApi;
import com.example.wowhqapp.network.WowSpecApi;
import com.example.wowhqapp.network.WowTalentApi;
import com.example.wowhqapp.repositories.SettingRepository;


import java.io.IOException;
import java.util.List;

public class TalentsRepository implements TalentsContract.TalentsRepository {
    private final Context mContext;
    private final MainContract.SettingRepository mSettingRepository;

    private final WowClassApi mWowClassApi;
    private final WowSpecApi mWowSpecApi;
    private final WowTalentApi mWowTalentApi;

    private final WowClassDao mWowClassDao;
    private final WowSpecDao mWowSpecDao;
    private final WowTalentDao mWowTalentDao;


    public TalentsRepository(Context context, @NonNull MainContract.SettingRepository settingRepository) {
        mContext = context;
        mSettingRepository = settingRepository;
        ApiTalentsRepository apiTalentsRepository = ApiTalentsRepository.from(mContext);
        TalentsCalculatorDatabase database = TalentsCalculatorDatabase.from(mContext);

        mWowClassApi = apiTalentsRepository.getWowClassApi();
        mWowSpecApi = apiTalentsRepository.getWowSpecApi();
        mWowTalentApi = apiTalentsRepository.getWowTalentApi();

        mWowClassDao = database.getWowClassesDao();
        mWowSpecDao = database.getWowSpecsDao();
        mWowTalentDao = database.getWowTalentsDao();
    }

    @Override
    public List<WowClass> getWowClasses() {
        List<WowClass> wowClasses = mWowClassDao.getAll();
        if (wowClasses.isEmpty()) {
            try {
                wowClasses = loadWowClasses();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wowClasses;
    }

    @Override
    public List<WowSpec> getWowSpecs(int classId) {
        List<WowSpec> wowSpecs = mWowSpecDao.getByClassId(classId);
        if (wowSpecs.isEmpty()) {
            try {
                wowSpecs = loadWowSpecs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wowSpecs;
    }

    @Override
    public WowTalents getWowTalents(int specId) {
        WowTalents wowTalents = new WowTalents(mWowTalentDao.getBySpecId(specId));
        if (wowTalents.getTalents().isEmpty()) {
            try {
                wowTalents = loadWowTalents(specId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wowTalents;
    }

    @Override
    public WowTalents getWowTalents() {
        return getWowTalents(mSettingRepository.getTalentsWowSpecId());
    }

        @Override
    public void refresh() throws IOException {
        loadWowClasses();
        loadWowSpecs();
        mWowTalentDao.deleteAll();

        int savedWowSpecId = mSettingRepository.getTalentsWowSpecId();
        if (savedWowSpecId != -1) {
            loadWowTalents(savedWowSpecId);
        }

    }


    private List<WowClass> loadWowClasses() throws IOException {
        List<WowClass> wowClasses = mWowClassApi.getAll(mSettingRepository.getTalentsLang()).execute().body();
        mWowClassDao.insert(wowClasses);
        return wowClasses;
    }

    private List<WowSpec> loadWowSpecs() throws IOException {
        List<WowSpec> wowSpecs = mWowSpecApi.getAll(mSettingRepository.getTalentsLang()).execute().body();
        mWowSpecDao.insert(wowSpecs);
        return wowSpecs;
    }

    private WowTalents loadWowTalents(int specId) throws IOException, NullPointerException {
        WowSpec wowSpec = mWowSpecDao.getById(specId);
        if (wowSpec == null) {
            throw new NullPointerException("Can't find wowSpec by id");
        }
        WowTalents wowTalents = mWowTalentApi.getWowTalents(
                    mSettingRepository.getTalentsWowClassId(),
                    wowSpec.getSpecOrder(),
                    mSettingRepository.getTalentsLang())
                .execute().body();
        if (wowTalents == null || wowTalents.getTalents() == null) {
            throw new NullPointerException("Can't load wowSpec with this id or spec order");
        }

        mWowTalentDao.insert(wowTalents.getTalents());
        return wowTalents;
    }

    @Override
    public Talent getWowTalent(int id) {
        return mWowTalentDao.getById(id);
    }
}
