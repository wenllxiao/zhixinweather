package com.zhixin.fragment;

import com.zhixin.bean.CityBean;

import java.util.Map;

/**
 * Created by v_wenlxiao on 2017/3/7.
 */

public interface FragmentCallBack{
        public void drawerSelectPosition(int position);
        public void responseWeatherInfo(Map<String, String> weatherInfo);
        public void update(CityBean cityBean);
}
