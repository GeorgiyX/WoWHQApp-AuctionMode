package com.example.wowhqapp.utils;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.wowhqapp.MenuActivity;
import com.example.wowhqapp.R;

public class DrawerMenu implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private AppCompatActivity mAppCompatActivity;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    public DrawerMenu(AppCompatActivity appCompatActivity, int checked_id){
        mAppCompatActivity = appCompatActivity;
        mDrawerLayout = (DrawerLayout) appCompatActivity.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().findItem(checked_id).setChecked(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(appCompatActivity,mDrawerLayout, (Toolbar) mAppCompatActivity.findViewById(R.id.toolbar),
                R.string.drawer_open, R.string.drawer_close);
    }

    public void toggleDrawer(){
        if(!mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }else{
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //mAppCompatActivity.finish(); - везде
        Intent intent = null;
        switch (menuItem.getItemId()){
            case R.id.nav_auctions:
                intent = new Intent(MenuActivity.LAUNCH_AUCANDDEALS).putExtra(MenuActivity.KEY_TYPE, false);
                break;
            case R.id.nav_auctions_best:
                intent = new Intent(MenuActivity.LAUNCH_AUCANDDEALS).putExtra(MenuActivity.KEY_TYPE, true);
                break;
            case R.id.nav_auctions_tracing:
                intent = new Intent(MenuActivity.LAUNCH_SAVEDLOTS);
                break;
            case R.id.nav_wowtoken:
                intent = new Intent(MenuActivity.LAUNCH_WOWTOKEN);
                break;
            case R.id.nav_characters:
                break;
            case R.id.nav_talents:
                intent = new Intent(MenuActivity.LAUNCH_TALANTS).putExtra(MenuActivity.KEY_TYPE, true);
                break;
            case R.id.nav_talents_builds:
//                intent = new Intent(MenuActivity.LAUNCH_TALANTS).putExtra(MenuActivity.KEY_TYPE, false)
                break;
            case R.id.nav_home:
                mAppCompatActivity.finish();
                break;
            case R.id.nav_setting:
                mAppCompatActivity.startActivity(new Intent(MenuActivity.LAUNCH_SETTINGS));
                break;
            case R.id.nav_exit:
                break;
            default:
        }
        mDrawerLayout.closeDrawers();
        if (intent != null){
            mAppCompatActivity.finish();
            mAppCompatActivity.startActivity(intent);
        }
        return false;
    }

    public void syncState(){
        mActionBarDrawerToggle.syncState();
    }
    public void onConfigurationChanged(Configuration newConfig){
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}
