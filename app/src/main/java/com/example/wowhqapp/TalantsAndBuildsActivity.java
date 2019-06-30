package com.example.wowhqapp;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wowhqapp.contracts.TalentsContract;
import com.example.wowhqapp.presenters.TalentsPresenter;
import com.example.wowhqapp.repositories.SettingRepository;

public class TalantsAndBuildsActivity extends AppCompatActivity implements TalentsContract.TalentsView {

    // public String TYPE;
    private final String SAVED_ACTIVITY_TITLE = "talents_and_build_activity_title";
    private Toolbar mToolbar;
    private Button mResetButton;
    private FragmentManager mFragmentManager;
    private TalentsContract.TalentsPresenter mTalentsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talants_and_builds);
        // TYPE = getIntent().getExtras().getBoolean(MenuActivity.KEY_TYPE) ? "Таблица талантов" : "Сборки талантов";

        mToolbar = findViewById(R.id.talents_toolbar);
        mResetButton = findViewById(R.id.talents_button_reset);
        mFragmentManager = getSupportFragmentManager();
        mTalentsPresenter = new TalentsPresenter(
                new SettingRepository(getSharedPreferences(SettingRepository.APP_PREFERENCES, Context.MODE_PRIVATE)),
                this);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTalentsPresenter.resetProgress();
                mTalentsPresenter.loadStage(false);
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.talents_a_bar);

        String title = mTalentsPresenter.getSettingRepository().getTalentsActivityTitle();
        if (title != "none") {
            setTalentsTitle(title);
        } else {
            ((TextView) findViewById(R.id.talents_a_bar_title)).setText(R.string.talents_a_bar_title_text);
        }

        mTalentsPresenter.loadStage(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_ACTIVITY_TITLE, getTalentsTitle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedTitle = savedInstanceState.getString(SAVED_ACTIVITY_TITLE);
        setTalentsTitle(savedTitle);
    }

    @Override
    public void loadFragment(Fragment fragment, boolean needAddToBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_talents, fragment);
        if (needAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public TalentsContract.TalentsPresenter getTalentsPresenter() {
        return mTalentsPresenter;
    }

    @Override
    public View findOnTalentsViewById(int id) {
        return findViewById(id);
    }

    @Override
    public Resources getTalentsViewResources() {
        return getResources();
    }

    @Override
    public String getTalentsTitle() {
        TextView  title =  findViewById(R.id.talents_a_bar_title);
        return title.getText().toString();
    }

    @Override
    public void setTalentsTitle(String newTitle) {
        TextView  title =  findViewById(R.id.talents_a_bar_title);
        title.setText(newTitle);
    }

    @Override
    protected void onDestroy() {
        String title = getTalentsTitle();
        mTalentsPresenter.getSettingRepository().setTalentsActivityTitle(title);
        super.onDestroy();
    }
}
