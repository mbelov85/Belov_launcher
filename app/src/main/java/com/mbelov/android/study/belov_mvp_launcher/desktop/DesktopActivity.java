package com.mbelov.android.study.belov_mvp_launcher.desktop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.MyView;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;

public class DesktopActivity
        extends AppCompatActivity
        implements MyView, AdapterView.OnItemClickListener {
    private Presenter mPresenter;

    private GridView grView;
    private GridViewAdapter grViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_desktop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new DesktopPresenter(savedInstanceState, this, this);

        grView = (GridView) findViewById(R.id.gridView);
        grViewAdapter = new GridViewAdapter(getApplicationContext() ,mPresenter);
        grView.setAdapter(grViewAdapter);

        grView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }
        });

        grView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_desctop, menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(mPresenter.getAppsList(SELECTED_APPS_LIST).get(position).getIntent());
    }

    public void onPhoneClick(View view){
        mPresenter.onButtonClick(view.getId());
    }

    public void onAppsClick(View view){
        mPresenter.onButtonClick(view.getId());
    }

    public void onSMSClick(View view){
        mPresenter.onButtonClick(view.getId());
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startIntent(String action) {
        startActivity(new Intent(action));
    }

    @Override
    public void startIntent(String action, String type) {
        Intent intent = new Intent(action);
        if( type != null){
            intent.setType(type);
        }
        startActivity(intent);
    }

    @Override
    public void startDataIntent(String packageName) {

    }

    @Override
    public void startIntent(Context context, Class<?> activityClass) {
        startActivity(new Intent(context, activityClass));
    }

    @Override
    public void startIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.readData();
        grViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.storeData();
    }
}
