package com.mbelov.android.study.belov_mvp_launcher.selector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.Model;
import com.mbelov.android.study.belov_mvp_launcher.core.MyView;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;
import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;
import com.mbelov.android.study.belov_mvp_launcher.desktop.DesktopModel;

import java.util.ArrayList;

/**
 * Created by Max on 11.10.2016.
 */

public class SelectorPresenter implements Presenter {
    private Activity mActivity;
    private Bundle mSavedInstanceState;

    private Model mModel;
    private MyView mView;

    public SelectorPresenter(Bundle savedInstanceState, Activity activity, MyView view) {
        this.mSavedInstanceState = savedInstanceState;
        this.mActivity = activity;
        this.mView = view;

        this.mModel = new SelectorModel();

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
    public void onButtonClick(int buttonId) {}

    @Override
    public void onMenuItemClick(int menuItemId) {
        switch (menuItemId){
            case android.R.id.home:
                storeData();
                break;
            case R.id.action_list:
                ((SelectorActivity)mView).setLayoutManager(menuItemId);
                break;
            case R.id.action_grid:
                ((SelectorActivity)mView).setLayoutManager(menuItemId);
                break;
        }
    }

    public void readInstalledApps(){
        ((SelectorModel)mModel).readInstalledApps(mActivity.getPackageName(),
                mActivity.getPackageManager());
    }

    @Override
    public ArrayList<MyApplication> getAppsList(String listName) {
        return mModel.getAppsList(listName);
    }

    public void filterList(String name, String text){
        mModel.setAppsList(FILTERED_INSTALLED_APPS_LIST,
                ((SelectorModel)mModel).filter(name, text));
        ((SelectorActivity)mView).setIsFiltered(true);
    }

    public void onAppSelected(MyApplication selectedApp){
        if(mModel.getAppsList(SELECTED_APPS_LIST).size() < MAX_SELECTED_APPS_QTY){
            mModel.getAppsList(SELECTED_APPS_LIST).add(selectedApp);
        }
        else{
            mView.showToast("MAX APPS QUANTITY REACHED");
        }
    }

    public void onAppDeselected(MyApplication selectedApp){
        mModel.getAppsList(SELECTED_APPS_LIST).remove(selectedApp);
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


    //app info screen
//    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//    intent.setData(Uri.parse("package:" + getPackageName()));
//    startActivity(intent);


    /**
     * <p>Intent to show an applications details page in (Settings) com.android.settings</p>
     *
     * @param context       The context associated to the application
     * @param packageName   The package name of the application
     * @return the intent to open the application info screen.
     */
//    public static Intent newAppDetailsIntent(Context context, String packageName) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse("package:" + packageName));
//            return intent;
//        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setClassName("com.android.settings",
//                    "com.android.settings.InstalledAppDetails");
//            intent.putExtra("pkg", packageName);
//            return intent;
//        }
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClassName("com.android.settings",
//                "com.android.settings.InstalledAppDetails");
//        intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
//        return intent;
//    }

//    packageName = "your.package.name.here"
//
//            try {
//        //Open the specific App Info page:
//        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + packageName));
//        startActivity(intent);
//
//    } catch ( ActivityNotFoundException e ) {
//        //e.printStackTrace();
//
//        //Open the generic Apps page:
//        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
//        startActivity(intent);
//
//    }

//    The current approach
//
//    Install APK using Intent
//
//    Intent intent = new Intent(Intent.ACTION_VIEW);
//    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//    startActivity(intent);
//    Uninstall APK using Intent:
//
//    Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
//            getPackageManager().getPackageArchiveInfo(apkUri.getPath(), 0).packageName,null));
//    startActivity(intent);
//
//
//    My application installs other applications, and it needs to keep track of what applications it has installed. Of course, this could be achieved by simply keeping a list of installed applications. But this should not be necessary! It should be the responsibility of the PackageManager to maintain the installedBy(a, b) relationship. In fact, according to the API it is:
//
//    public abstract String getInstallerPackageName(String packageName) - Retrieve the package name of the application that installed a package. This identifies which market the package came from.
//
//    The current approach
//
//    Install APK using Intent
//
//    Intent intent = new Intent(Intent.ACTION_VIEW);
//    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//    startActivity(intent);
//    Uninstall APK using Intent:
//
//    Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
//            getPackageManager().getPackageArchiveInfo(apkUri.getPath(), 0).packageName,null));
//    startActivity(intent);
//    This is obviously not the way e.g. Android Market installs / uninstalls packages. They use a richer version of the PackageManager. This can bee seen by downloading the Android source code from the Android Git repository. Below are the two hidden methods that corresponds to the Intent approach. Unfortunately they are not available to external developers. But perhaps they will be in the future?
//
//    The better approach
//
//    Installing APK using the PackageManager
//
//    /**
//     * @hide
//     *
//     * Install a package. Since this may take a little while, the result will
//     * be posted back to the given observer.  An installation will fail if the calling context
//     * lacks the {@link android.Manifest.permission#INSTALL_PACKAGES} permission, if the
//     * package named in the package file's manifest is already installed, or if there's no space
//     * available on the device.
//     *
//     * @param packageURI The location of the package file to install.  This can be a 'file:' or a
//     * 'content:' URI.
//     * @param observer An observer callback to get notified when the package installation is
//     * complete. {@link IPackageInstallObserver#packageInstalled(String, int)} will be
//     * called when that happens.  observer may be null to indicate that no callback is desired.
//     * @param flags - possible values: {@link #INSTALL_FORWARD_LOCK},
//     * {@link #INSTALL_REPLACE_EXISTING}, {@link #INSTALL_ALLOW_TEST}.
//     * @param installerPackageName Optional package name of the application that is performing the
//     * installation. This identifies which market the package came from.
//     */
//    public abstract void installPackage(
//            Uri packageURI, IPackageInstallObserver observer, int flags,
//            String installerPackageName);
//    );
//
//    Uninstalling APK using the PackageManager
//
//    /**
//     * Attempts to delete a package.  Since this may take a little while, the result will
//     * be posted back to the given observer.  A deletion will fail if the calling context
//     * lacks the {@link android.Manifest.permission#DELETE_PACKAGES} permission, if the
//     * named package cannot be found, or if the named package is a "system package".
//     * (TODO: include pointer to documentation on "system packages")
//     *
//     * @param packageName The name of the package to delete
//     * @param observer An observer callback to get notified when the package deletion is
//     * complete. {@link android.content.pm.IPackageDeleteObserver#packageDeleted(boolean)} will be
//     * called when that happens.  observer may be null to indicate that no callback is desired.
//     * @param flags - possible values: {@link #DONT_DELETE_DATA}
//     *
//     * @hide
//     */
//    public abstract void deletePackage(
//            String packageName, IPackageDeleteObserver observer, int flags);
//    );

}
