package com.mbelov.android.study.belov_mvp_launcher.core;

import android.app.Activity;
import android.os.Bundle;

import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;

import java.util.ArrayList;

/**
 * Created by Max on 11.10.2016.
 */

public interface Presenter extends Constants {

    Activity getActivity();

    void setActivity(Activity activity);

    Bundle getSavedInstanceState();

    void setSavedInstanceState(Bundle savedInstanceState);

    Model getModel();

    void setModel(Model model);

    MyView getView();

    void setView(MyView view);

    void onButtonClick(int buttonId);

    void onMenuItemClick(int menuItemId);

    ArrayList<MyApplication> getAppsList(String listName);

    void  storeData();

    void readData();
}
