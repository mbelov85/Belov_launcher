package com.mbelov.android.study.belov_mvp_launcher.selector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.MyView;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;

public class SelectorActivity
        extends AppCompatActivity
        implements MyView, SearchView.OnQueryTextListener {
    private Presenter mPresenter;

    private RecyclerView mRecView;
    private RecyclerView.LayoutManager mRecViewLayoutManager;
    private RecViewAdapter mRecViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new SelectorPresenter(savedInstanceState, this, this);
        ((SelectorPresenter)mPresenter).readInstalledApps();

        this.mRecView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecView.setHasFixedSize(true);

        mRecViewLayoutManager = new LinearLayoutManager(this);
        mRecView.setLayoutManager(mRecViewLayoutManager);
        mRecViewAdapter = new RecViewAdapter(mPresenter, LINEAR_LAYOUT);
        mRecView.setAdapter(mRecViewAdapter);

        ((SearchView)findViewById(R.id.searchField)).setOnQueryTextListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        mPresenter.onMenuItemClick(id);
        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            mPresenter.storeData();
            super.onBackPressed();
            return true;
        }
        else if (id == R.id.action_list) {
            return true;
        }
        else if (id == R.id.action_grid) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLayoutManager(int itemID) {
        switch (itemID){
            case R.id.action_list:
                mRecView.setLayoutManager(new LinearLayoutManager(this));
                mRecViewAdapter.setLayoutType(LINEAR_LAYOUT);
                mRecView.setAdapter(mRecViewAdapter);
                break;
            case R.id.action_grid:
                mRecView.setLayoutManager(new GridLayoutManager(this, 3));
                mRecViewAdapter.setLayoutType(GRID_LAYOUT);
                mRecView.setAdapter(mRecViewAdapter);
                break;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startIntent(String action) {

    }

    @Override
    public void startDataIntent(String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    @Override
    public void startIntent(String action, String type) {

    }

    @Override
    public void startIntent(Context context, Class<?> activityClass) {

    }

    @Override
    public void startIntent(Intent intent) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ((SelectorPresenter) mPresenter).filterList(INSTALLED_APPS_LIST, newText);
        return true;
    }

    public void setIsFiltered(boolean isFiltered){
        mRecViewAdapter.setIsFiltered(isFiltered);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.readData();
        mRecViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.storeData();
    }
}
