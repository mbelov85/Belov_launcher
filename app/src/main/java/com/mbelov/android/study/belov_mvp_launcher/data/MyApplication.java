package com.mbelov.android.study.belov_mvp_launcher.data;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by Max on 11.10.2016.
 */

public class MyApplication {

    private String mPackageName;
    private transient String mName;
    private transient Drawable mIcon;
    private transient Intent mIntent;

    public MyApplication(String packageName, String name, Drawable icon, Intent intent) {
        this.mPackageName = packageName;
        this.mName = name;
        this.mIcon = icon;
        this.mIntent = intent;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPpackageName) {
        this.mPackageName = mPpackageName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyApplication that = (MyApplication) o;

        if (!mPackageName.equals(that.mPackageName)) return false;
//        if (!mName.equals(that.mName)) return false;
//        if (!mIcon.equals(that.mIcon)) return false;
//        return mIntent.equals(that.mIntent);
        return mName.equals(that.mName);
    }

    @Override
    public int hashCode() {
        int result = mPackageName.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + mIcon.hashCode();
        result = 31 * result + mIntent.hashCode();
        return result;
    }
}
