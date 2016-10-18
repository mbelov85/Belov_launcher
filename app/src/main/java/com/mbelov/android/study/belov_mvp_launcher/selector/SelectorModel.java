package com.mbelov.android.study.belov_mvp_launcher.selector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.mbelov.android.study.belov_mvp_launcher.core.Model;
import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 11.10.2016.
 */

public class SelectorModel implements Model {
    private ArrayList<MyApplication> mSelectedApps = new ArrayList<>();
    private ArrayList<MyApplication> mInstalledApps = new ArrayList<>();
    private ArrayList<MyApplication> mFiltereddApps = new ArrayList<>();


    public SelectorModel() {
    }

    @Override
    public ArrayList<MyApplication> getAppsList(String listName) {
        switch (listName){
            case SELECTED_APPS_LIST:
                return mSelectedApps;
            case INSTALLED_APPS_LIST:
                return mInstalledApps;
            case FILTERED_INSTALLED_APPS_LIST:
                return mFiltereddApps;
            default:
                return null;
        }
    }

    @Override
    public void setAppsList(String listName, ArrayList<MyApplication> list) {
        switch (listName){
            case SELECTED_APPS_LIST:
                this.mSelectedApps = list;
                break;
            case INSTALLED_APPS_LIST:
                this.mInstalledApps = list;
                break;
            case FILTERED_INSTALLED_APPS_LIST:
                this.mFiltereddApps = list;
        }
    }

    public void readInstalledApps(String selfName, PackageManager packageManager){
        final String mSelfName = selfName;
        final PackageManager mPm = packageManager;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> temp = mPm.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : temp){
            if(!resolveInfo.activityInfo.packageName.equals(mSelfName)){
                mInstalledApps.add(new MyApplication(resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.loadLabel(mPm).toString(),
                    resolveInfo.activityInfo.loadIcon(mPm),
                    mPm.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName)));
            }
        }
    }

    public ArrayList<MyApplication> filter(String listName, String filterText) {
        ArrayList<MyApplication> filteredList = new ArrayList<>();
        for (MyApplication tempApp : getAppsList(listName)) {
            if (tempApp.getName().toLowerCase().contains(filterText)) {
                filteredList.add(tempApp);
            }
        }
        return filteredList;
    }
}
