package com.mbelov.android.study.belov_mvp_launcher.core;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Max on 11.10.2016.
 */

public interface MyView extends Constants{

    void showToast(String message);

    void startIntent(String action);

    void startDataIntent(String packageName);

    void startIntent(String action, String type);

    void startIntent(Context context, Class<?> activityClass);

    void startIntent(Intent intent);
}
