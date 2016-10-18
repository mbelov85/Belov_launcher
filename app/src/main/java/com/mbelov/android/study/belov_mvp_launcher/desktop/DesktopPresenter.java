package com.mbelov.android.study.belov_mvp_launcher.desktop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.Model;
import com.mbelov.android.study.belov_mvp_launcher.core.MyView;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;
import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;
import com.mbelov.android.study.belov_mvp_launcher.selector.SelectorActivity;

import java.util.ArrayList;

/**
 * Created by Max on 11.10.2016.
 */

public class DesktopPresenter implements Presenter {
    private Activity mActivity;
    private Bundle mSavedInstanceState;

    private Model mModel;
    private MyView mView;

    public DesktopPresenter(Bundle savedInstanceState, Activity activity, MyView view) {
        this.mSavedInstanceState = savedInstanceState;
        this.mActivity = activity;
        this.mView = view;

        this.mModel = new DesktopModel();

        readData();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public void setSavedInstanceState(Bundle savedInstanceState) {
        mSavedInstanceState= savedInstanceState;
    }

    @Override
    public Model getModel() {
        return mModel;
    }

    @Override
    public void setModel(Model model) {
        mModel = model;
    }

    @Override
    public MyView getView() {
        return mView;
    }

    @Override
    public void setView(MyView view) {
        mView = view;
    }

    @Override
    public void onButtonClick(int buttonId) {
        switch (buttonId){
            case R.id.btn_phone:
                mView.startIntent(Intent.ACTION_DIAL);
                break;
            case R.id.btn_apps:
                storeData();
                mView.startIntent(mActivity, SelectorActivity.class);
                break;
            case R.id.btn_sms:
                mView.startIntent(Intent.ACTION_VIEW, "vnd.android-dir/mms-sms");
                break;
        }
    }

    @Override
    public void onMenuItemClick(int menuItemId) {
        switch (menuItemId){
            case R.id.action_settings:
                mView.startIntent(Settings.ACTION_SETTINGS);
                break;
        }
    }

    @Override
    public ArrayList<MyApplication> getAppsList(String listName) {
        return mModel.getAppsList(listName);
    }

    @Override
    public void storeData() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(getAppsList(SELECTED_APPS_LIST));

        SharedPreferences sharedPref = mActivity.getApplicationContext()
                                .getSharedPreferences(JSON_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(JSON_NAME, json);
        editor.apply();
    }

    @Override
    public void readData() {
        ArrayList<MyApplication> list;

        SharedPreferences sharedPref = mActivity.getApplicationContext()
                                        .getSharedPreferences(JSON_NAME, Context.MODE_PRIVATE);
        String json = sharedPref.getString(JSON_NAME, "");

        Gson gson = new GsonBuilder().create();

        list = gson.fromJson(json, new TypeToken<ArrayList<MyApplication>>(){}.getType());

        for (MyApplication app : list) {
            try {
                app.setName((String)mActivity.getPackageManager()
                        .getApplicationLabel(mActivity.getPackageManager()
                                .getApplicationInfo(app.getPackageName(),
                                        PackageManager.GET_META_DATA)));
                app.setIcon(mActivity.getPackageManager().getApplicationIcon(app.getPackageName()));
                app.setIntent(mActivity.getPackageManager()
                        .getLaunchIntentForPackage(app.getPackageName()));


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        mModel.setAppsList(SELECTED_APPS_LIST, list);
    }
}
