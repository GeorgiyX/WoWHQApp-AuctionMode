package com.example.wowhqapp.utils;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.wowhqapp.R;
import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.contracts.MainContract;

public abstract class BaseDrawerActivityWithToolBar extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, MainContract.BaseDrawerView {

    public static final String LAUNCH_AUCANDDEALS = "LAUNCH_AUCANDDEALS";
    public static final String LAUNCH_SAVEDLOTS = "LAUNCH_SAVEDLOTS";
    public static final String LAUNCH_SETTINGS = "LAUNCH_SETTINGS";
    public static final String LAUNCH_WOWTOKEN = "LAUNCH_WOWTOKEN";
    public static final String LAUNCH_TALANTS = "LAUNCH_TALANTS";
    public static final String KEY_TYPE = "TYPE";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mActionBarDrawerToggle.isDrawerIndicatorEnabled()){
                    onBackArrowClick();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Log.v(WowhqApplication.LOG_TAG, "BaseDrawerActivityWithToolBar, onKeyDown [KeyEvent.KEYCODE_MENU]" );
                toggleDrawer();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void toggleDrawer(){
        if(!mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.openDrawer(Gravity.START);
        }else{
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void setCheckedMenuItem(int id){
        mNavigationView.getMenu().findItem(id).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()){
            case R.id.nav_auctions:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_AUCANDDEALS).putExtra(BaseDrawerActivityWithToolBar.KEY_TYPE, false);
                break;
            case R.id.nav_auctions_best:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_AUCANDDEALS).putExtra(BaseDrawerActivityWithToolBar.KEY_TYPE, true);
                break;
            case R.id.nav_auctions_tracing:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_SAVEDLOTS);
                break;
            case R.id.nav_wowtoken:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_WOWTOKEN);
                break;
            case R.id.nav_characters:
                break;
            case R.id.nav_talents:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_TALANTS).putExtra(BaseDrawerActivityWithToolBar.KEY_TYPE, true);
                break;
            case R.id.nav_talents_builds:
//                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_TALANTS).putExtra(MenuActivity.KEY_TYPE, false)
                break;
            case R.id.nav_home:
                this.finish();
                break;
            case R.id.nav_setting:
                intent = new Intent(BaseDrawerActivityWithToolBar.LAUNCH_SETTINGS);
                break;
            case R.id.nav_exit:
                break;
            default:
        }
        mDrawerLayout.closeDrawers();
        if (intent != null){
            this.finish();
            this.startActivity(intent);
        }
        return false;
    }

    @Override
    public void setTitle(String txt) {
        getSupportActionBar().setTitle(txt);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void enableDrawer() {
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mActionBarDrawerToggle.setHomeAsUpIndicator(null);
        mDrawerLayout.setEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void disableDrawer() {
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mActionBarDrawerToggle.setHomeAsUpIndicator(null);
        mDrawerLayout.setEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void showArrowBack() {
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle.syncState();
    }
}
