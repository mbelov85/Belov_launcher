package com.mbelov.android.study.belov_mvp_launcher.desktop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbelov.android.study.belov_mvp_launcher.R;
import com.mbelov.android.study.belov_mvp_launcher.core.Presenter;


public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private Presenter mPresenter;

    public GridViewAdapter(Context mContext, Presenter mPresenter) {
        this.mContext = mContext;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getCount() {
        return mPresenter.getAppsList(mPresenter.SELECTED_APPS_LIST).size();
    }

    @Override
    public Object getItem(int position) {
        return mPresenter.getAppsList(mPresenter.SELECTED_APPS_LIST).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            cell = inflater.inflate(R.layout.app_cell, parent, false);
        } else {
            cell = convertView;
        }

        ImageView image = (ImageView) cell.findViewById(R.id.cell_icon);
        image.setImageDrawable(mPresenter.getAppsList(mPresenter.SELECTED_APPS_LIST)
                .get(position).getIcon());
        TextView text = (TextView) cell.findViewById(R.id.cell_text);
        text.setText(mPresenter.getAppsList(mPresenter.SELECTED_APPS_LIST)
                .get(position).getName());

        return cell;
    }
}
