package com.example.wowhqapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wowhqapp.presenters.MenuPresenter;
import com.example.wowhqapp.contracts.MainContract;

public class MenuActivity extends AppCompatActivity implements MainContract.MenuView {

    public static final String LAUNCH_AUCANDDEALS = "LAUNCH_AUCANDDEALS";
    public static final String LAUNCH_SAVEDLOTS = "LAUNCH_SAVEDLOTS";
    public static final String LAUNCH_SETTINGS = "LAUNCH_SETTINGS";
    public static final String LAUNCH_WOWTOKEN = "LAUNCH_WOWTOKEN";
    public static final String LAUNCH_TALANTS = "LAUNCH_TALANTS";
    public static final String KEY_TYPE = "TYPE";
    private MenuPresenter mMenuPresenter;
    TextView textViewAuc;
    TextView textViewDeals;
    TextView textViewToken;
    TextView textViewSetting;
    TextView textViewSavedLots;
    TextView textViewExit;
    TextView textViewTalants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mMenuPresenter = new MenuPresenter(this);

        textViewAuc= (TextView) findViewById(R.id.menu_auc);
        textViewDeals = (TextView) findViewById(R.id.menu_deals);
        textViewSavedLots = (TextView) findViewById(R.id.menu_lots);
        textViewToken = (TextView) findViewById(R.id.menu_wowtoken);
        textViewSetting = (TextView) findViewById(R.id.menu_setting);
        textViewExit = (TextView) findViewById(R.id.menu_exit);
        textViewTalants = (TextView) findViewById(R.id.menu_calc);

        textViewDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openDeals();
            }
        });
        textViewAuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openAuctions();
            }
        });
        textViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openSetting();
            }
        });
        textViewToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openWoWToken();
            }
        });
        textViewSavedLots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openSavedLots();
            }
        });
        textViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.closeMenu();
            }
        });
        textViewTalants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPresenter.openTalants();
            }
        });
    }

    @Override
    public void openAuctionsActivity() {
        Intent intent = new Intent(LAUNCH_AUCANDDEALS);
        intent.putExtra(KEY_TYPE, false);
        startActivity(intent);
    }

    @Override
    public void openDealsActivity() {
        Intent intent = new Intent(LAUNCH_AUCANDDEALS);
        intent.putExtra(KEY_TYPE, true);
        startActivity(intent);
    }

    @Override
    public void openSavedLotsActivity() {
        Intent intent = new Intent(LAUNCH_SAVEDLOTS);
        startActivity(intent);
    }

    @Override
    public void openWoWTokenActivity() {
        Intent intent = new Intent(LAUNCH_WOWTOKEN);
        startActivity(intent);
    }

    @Override
    public void openTalantsActivity() {
        Intent intent = new Intent(LAUNCH_TALANTS);
        intent.putExtra(KEY_TYPE, true);
        startActivity(intent);
    }

    @Override
    public void openSettingActivity() {
        Intent intent = new Intent(LAUNCH_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
