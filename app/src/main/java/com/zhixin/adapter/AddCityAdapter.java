package com.zhixin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhixin.bean.CityBean;
import com.zhixin.weather.R;

import java.util.ArrayList;
import java.util.List;

public class AddCityAdapter extends BaseAdapter {
    private List<CityBean> mHotCities;
    private LayoutInflater mLayoutInflater;

    public AddCityAdapter(Context mContext) {
        mHotCities = new ArrayList<CityBean>();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void SetHotCitys(List<CityBean> list) {
        mHotCities.clear();
        mHotCities = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHotCities.size();
    }

    @Override
    public Object getItem(int i) {
        return mHotCities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        int type = getItemViewType(position);
        if (convertView == null
                || convertView.getTag(R.mipmap.heart_logo + type) == null) {
            switch (type) {
                case 1:
                    convertView = mLayoutInflater.inflate(
                            R.layout.hot_city_selected_item, parent, false);
                    break;
                case 0:
                    convertView = mLayoutInflater.inflate(
                            R.layout.hot_city_normal_item, parent, false);
                    break;
                default:
                    break;
            }
            mViewHolder = buildHolder(convertView);
            convertView.setTag(R.mipmap.heart_logo + type, mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView
                    .getTag(R.mipmap.heart_logo + type);
        }
        bindViewData(mViewHolder, position);
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mHotCities.get(position).getIsSelected() == 1 ? 1 : 0;
    }

    private class ViewHolder {
        TextView cityView;
    }

    private ViewHolder buildHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.cityView = (TextView) convertView;
        return holder;
    }

    private void bindViewData(ViewHolder holder, int position) {
        holder.cityView.setText(mHotCities.get(position).getCity());
    }
}
