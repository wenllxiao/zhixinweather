package com.zhixin.location;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhixin.bean.CityBean;
import com.zhixin.fragment.FragmentCallBack;
import com.zhixin.utils.L;
import com.zhixin.utils.T;

/**
 * Created by v_wenlxiao on 2017/3/7.
 */

public class BaiduLocation {
    FragmentCallBack mCallBack;

    private LocationClient mLocationClient = null;
    private Context context;
    private static final String TAG="BaiduLocation";
    public BaiduLocation(Context context) {
        this.mCallBack = (FragmentCallBack) context;
        this.context = context;
        initClientOption();
    }

    // 开始定位
    public void startLocation() {
        L.e(TAG,"baiduLocation.startLocation");
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    // 结束定位
    public void stopLocation() {
        mLocationClient.stop();

    }

    public boolean isStarted() {
        return mLocationClient.isStarted();
    }

    private void initClientOption() {
        mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现定位回调监听
     */
    BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || TextUtils.isEmpty(location.getCity()))
                    return;
            if (location.getLocType() != 161) {
                T.showLong(context, "定位失败");
                return;
            }
            String city = location.getCity().replace("市", "");
            String district = location.getDistrict();
            String addRess = location.getAddrStr();
            int isSelected = 1;
            String cityCode = location.getCityCode();
            int isLocation = 1;
            CityBean cityBean = new CityBean(district, city, addRess, cityCode,
                    isSelected, isLocation);
            mLocationClient.stop();// 停止定位
            mCallBack.update(cityBean);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };
}
