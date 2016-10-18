package com.mbelov.android.study.belov_mvp_launcher.core;

import android.content.Context;
import android.content.pm.PackageManager;

import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;

import java.util.ArrayList;

/**
 * Created by Max on 11.10.2016.
 */

public interface Model extends Constants{

    ArrayList<MyApplication> getAppsList(String listName);

    void setAppsList(String listName, ArrayList<MyApplication> list);
}
