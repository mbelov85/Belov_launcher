package com.mbelov.android.study.belov_mvp_launcher.desktop;

import com.mbelov.android.study.belov_mvp_launcher.core.Model;
import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;

import java.util.ArrayList;


/**
 * Created by Max on 11.10.2016.
 */

public class DesktopModel implements Model {
    private ArrayList<MyApplication> mSelectedApps = new ArrayList<>();


    public DesktopModel() {
    }

    @Override
    public ArrayList<MyApplication> getAppsList(String listName) {
        switch (listName){
            case SELECTED_APPS_LIST:
                return mSelectedApps;
            default:
                return null;
        }
    }

    @Override
    public void setAppsList(String listName, ArrayList<MyApplication> list) {
        switch (listName){
            case SELECTED_APPS_LIST:
                this.mSelectedApps = list;
        }
    }
}
