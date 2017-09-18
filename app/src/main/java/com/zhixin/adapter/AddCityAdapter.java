package com.zhixin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhixin.bean.CityBean;
import com.zhixin.weather.R;

import java.util.ArrayList;
import java.util.List;

public class AddCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<CityBean> mHotCities;
    private LayoutInflater mLayoutInflater;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (mHotCities.size() > 0)
            return mHotCities.size();
        else return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder mViewHolder;
        View convertView;
        if (viewType == 0) {
            convertView = mLayoutInflater.inflate(
                    R.layout.hot_city_normal_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);

        } else {
            convertView = mLayoutInflater.inflate(
                    R.layout.hot_city_selected_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);

        }
        //给布局设置点击和长点击监听
        convertView .setOnClickListener(this);
        convertView .setOnLongClickListener(this);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MyViewHolder)holder).cityView.setText(mHotCities.get(position).getCity());
    }

    @Override
    public int getItemViewType(int position) {
        return mHotCities.get(position).getIsSelected() == 1 ? 1 : 0;
    }

    //点击事件回调
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cityView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityView = (TextView) itemView;
        }
    }
}
