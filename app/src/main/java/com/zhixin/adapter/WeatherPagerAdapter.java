package com.zhixin.adapter;

import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.zhixin.bean.CityBean;
import com.zhixin.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ItemInfo> mItemInfos;

      static class ItemInfo {
        private CityBean city;
        private Fragment fragment;

        ItemInfo(CityBean city) {
            this.city = city;
        }
    }

    public WeatherPagerAdapter(android.app.FragmentManager fm) {
        super(fm);
        mItemInfos = new ArrayList<ItemInfo>();
    }

    public void addAllCitys(List<CityBean> mTmpCities) {
        mItemInfos.clear();
        for (CityBean ctiy : mTmpCities) {
            ItemInfo info = new ItemInfo(ctiy);
            mItemInfos.add(info);
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        ItemInfo info = mItemInfos.get(position);
        if (info.fragment == null) {
            info.fragment = new WeatherFragment(info.city);
        }
        //L.e("position"+position+"   info.fragment msg:"+info.city.getCity());
        return info.fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mItemInfos != null)
            return mItemInfos.size();
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }
}
