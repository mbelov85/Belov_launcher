package com.mbelov.android.study.belov_mvp_launcher.selector;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.Constants;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;
import com.mbelov.android.study.belov_mvp_launcher.data.MyApplication;

import static android.view.Gravity.BOTTOM;

/**
 * Created by Max on 04.10.2016.
 */

public class RecViewAdapter
        extends RecyclerView.Adapter<RecViewAdapter.ViewHolder>
        implements Constants {
    private Presenter mPresenter;
    private int mLayout;

    private boolean mIsFiltered = false;

    public RecViewAdapter(Presenter presenter, int layout){
        this.mPresenter = presenter;
        this.mLayout = layout;
    }

    @Override
    public RecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch(mLayout){
            case 1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_item_list, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_item_grid, parent, false);
                break;
        }
        return new RecViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mIsFiltered){
            holder.text.setText(mPresenter.getAppsList(FILTERED_INSTALLED_APPS_LIST)
                    .get(position).getName());
            holder.icon.setImageDrawable(mPresenter.getAppsList(FILTERED_INSTALLED_APPS_LIST)
                    .get(position).getIcon());
            holder.checkBox.setChecked( mPresenter.getAppsList(SELECTED_APPS_LIST)
                    .contains(mPresenter.getAppsList(FILTERED_INSTALLED_APPS_LIST).get(position)));
        }
        else{
            holder.text.setText(mPresenter.getAppsList(INSTALLED_APPS_LIST)
                    .get(position).getName());
            holder.icon.setImageDrawable(mPresenter.getAppsList(INSTALLED_APPS_LIST)
                    .get(position).getIcon());
            holder.checkBox.setChecked( mPresenter.getAppsList(SELECTED_APPS_LIST)
                    .contains(mPresenter.getAppsList(INSTALLED_APPS_LIST).get(position)));
        }
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public TextView text;
        public ImageView icon;
        public CheckBox checkBox;
        private PopupWindow popup_window;

        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
            icon = (ImageView) v.findViewById(R.id.icon);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            checkBox.setChecked(false);
            checkBox.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.about){
                if(mIsFiltered){
                    mPresenter.getView()
                            .startDataIntent(mPresenter.
                                    getAppsList(FILTERED_INSTALLED_APPS_LIST)
                                    .get(getLayoutPosition())
                                    .getPackageName());
                }
                else{
                    mPresenter.getView()
                            .startDataIntent(mPresenter.
                                    getAppsList(INSTALLED_APPS_LIST)
                                    .get(getLayoutPosition())
                                    .getPackageName());
                }
                popup_window.dismiss();
            }
            else{
                if(mIsFiltered){
                    if(mPresenter.getAppsList(SELECTED_APPS_LIST).contains(
                            mPresenter.getAppsList(FILTERED_INSTALLED_APPS_LIST)
                                    .get(getLayoutPosition()))){
                        checkBox.setChecked(false);
                        ((SelectorPresenter)mPresenter).onAppDeselected(mPresenter
                                .getAppsList(FILTERED_INSTALLED_APPS_LIST)
                                .get(getLayoutPosition()));
                    }
                    else{
                        checkBox.setChecked(true);
                        ((SelectorPresenter)mPresenter).onAppSelected(mPresenter
                                .getAppsList(FILTERED_INSTALLED_APPS_LIST)
                                .get(getLayoutPosition()));
                    }
                }
                else{
                    if(mPresenter.getAppsList(SELECTED_APPS_LIST).contains(
                            mPresenter.getAppsList(INSTALLED_APPS_LIST)
                                    .get(getLayoutPosition()))){
                        checkBox.setChecked(false);
                        ((SelectorPresenter)mPresenter).onAppDeselected(mPresenter
                                .getAppsList(INSTALLED_APPS_LIST)
                                .get(getLayoutPosition()));
                    }
                    else{
                        checkBox.setChecked(true);
                        ((SelectorPresenter)mPresenter).onAppSelected(mPresenter
                                .getAppsList(INSTALLED_APPS_LIST)
                                .get(getLayoutPosition()));
                    }
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mPresenter.getView().showToast("Long Click");
            View inflatedView = v.inflate(v.getContext(), R.layout.selector_popup, null);
            popup_window = new PopupWindow(
                    inflatedView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            popup_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popup_window.setOutsideTouchable(true);
            popup_window.showAsDropDown(checkBox, 0, 0);
            Button aboutApp = (Button) inflatedView.findViewById(R.id.about);
            aboutApp.setOnClickListener(this);
            return true;
        }
    }

    void setLayoutType(int layoutType){
        this.mLayout = layoutType;
    }

    @Override
    public int getItemCount() {
        return mIsFiltered ?
                mPresenter.getAppsList(FILTERED_INSTALLED_APPS_LIST).size() :
                mPresenter.getAppsList(INSTALLED_APPS_LIST).size();
    }

    public void setIsFiltered(boolean filtered) {
        this.mIsFiltered = filtered;
        notifyDataSetChanged();
    }
}
