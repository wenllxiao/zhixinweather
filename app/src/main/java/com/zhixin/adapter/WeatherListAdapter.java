package com.zhixin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhixin.bean.IndexDataBean;
import com.zhixin.bean.WeatherDataBean;
import com.zhixin.myview.MyTextView;
import com.zhixin.myview.WeatherImageView;
import com.zhixin.utils.L;
import com.zhixin.utils.WeatherInfoUtil;
import com.zhixin.weather.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class WeatherListAdapter extends BaseAdapter {

    private static final int FORECAST_TYPE = 0;
    private static final int WEATHER_DETAILS_TYPE = 1;
    private static final int INDEX_TYPE = 2;
    private List<WeatherDataBean> mWeatherDataBean;
    private List<IndexDataBean> mIndexDataBean;
    private LayoutInflater mLayoutInflater;
    private List<Integer> mTypes;
    private Map<String, Integer> mWeatherImg;
    private int mForeCastId[] = {R.id.forecast_main_item1,
            R.id.forecast_main_item2, R.id.forecast_main_item3,
            R.id.forecast_main_item4};

    private String mWeatherName[] = {"晴", "多云", "阴", "阵雨", "雷阵雨", "雷阵雨并伴有冰雹",
            "雨加雪", "小雨", "中雨", "大雨 ", "暴雨", "大暴雨", "特大暴雨", "阵雪", "小雪", "中雪",
            "大雪", "暴雪", "雾", "冻雨", "沙尘暴", "小雨-中雨", "中雨-大雨", "大雨-暴雨", "暴雨-大暴雨",
            "大暴雨-特大暴雨", "小雪-中雪", "中雪-大雪", "大雪-暴雪 ", "浮尘", "扬沙", "强沙尘暴", "阴天",
            "雾霾", "霾", "未知"};

    private int mIndexId[] = {R.id.index_clothes, R.id.index_carcrash,
            R.id.index_tour, R.id.index_cold, R.id.index_sport, R.id.index_zyx};
    // 指数图标ID
    private int mIndexDrawableId[] = {R.mipmap.index_clothes,
            R.mipmap.index_carwash, R.mipmap.index_tour, R.mipmap.index_cold, R.mipmap.index_sport, R.mipmap.index_zyx};

    private int mIndexDrawableId2[] = {R.mipmap.index_clothes,
            R.mipmap.index_carwash, R.mipmap.index_cold, R.mipmap.index_sport, R.mipmap.index_zyx};

    public WeatherListAdapter(Context context) {
        mTypes = new ArrayList<Integer>();
        mWeatherDataBean = new ArrayList<WeatherDataBean>();
        mIndexDataBean = new ArrayList<IndexDataBean>();
        mTypes.add(FORECAST_TYPE);
        mTypes.add(WEATHER_DETAILS_TYPE);
        mTypes.add(INDEX_TYPE);
        mLayoutInflater = LayoutInflater.from(context);
        mWeatherImg = new HashMap<String, Integer>();
        for (int i = 0; i < mWeatherName.length; i++) {
            mWeatherImg.put(mWeatherName[i], R.mipmap.a_0 + i);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTypes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mTypes.size())
            return mTypes.get(position);
        return super.getItemViewType(position);
    }

    public void setResponseWeather(String city, String weatherStr) {
        // L.e(TAG,city+weatherStr);
        WeatherInfoUtil.JsonParser(weatherStr);
        mWeatherDataBean = WeatherInfoUtil.mWeatherDataBean;
        mIndexDataBean = WeatherInfoUtil.mIndexDataBean;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderForeCast viewHolderForeCast = null;
        ViewHolderDetail viewHolder2 = null;
        ViewHolderIndex viewHolderIndex = null;
        int type = getItemViewType(position);
        if (convertView == null
                || convertView.getTag(R.mipmap.heart_logo + type) == null) {
            switch (type) {
                case FORECAST_TYPE:
                    convertView = mLayoutInflater.inflate(R.layout.forecast_main,
                            parent, false);
                    viewHolderForeCast = buildHolder1(convertView);
                    convertView.setTag(R.mipmap.heart_logo + type, viewHolderForeCast);
                    break;
                case WEATHER_DETAILS_TYPE:
                    convertView = mLayoutInflater.inflate(R.layout.details_main,
                            parent, false);
                    viewHolder2 = buildHolder2(convertView);
                    convertView.setTag(R.mipmap.heart_logo + type, viewHolder2);
                    break;
                case INDEX_TYPE:
                    convertView = mLayoutInflater.inflate(R.layout.index_main,
                            parent, false);
                    viewHolderIndex = buildHolder3(convertView);
                    convertView.setTag(R.mipmap.heart_logo + type, viewHolderIndex);
                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case 0:
                    viewHolderForeCast = (ViewHolderForeCast) convertView.getTag(R.mipmap.heart_logo
                            + type);
                    break;
                case 1:
                    viewHolder2 = (ViewHolderDetail) convertView.getTag(R.mipmap.heart_logo
                            + type);
                    break;
                case 2:
                    viewHolderIndex = (ViewHolderIndex) convertView.getTag(R.mipmap.heart_logo
                            + type);
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case 0:
                bindForeCastData(viewHolderForeCast, position);
                break;
            case 1:
                bindWeatherDetailsData(viewHolder2, position);
                break;
            case 2:
                bindIndexData(viewHolderIndex, position);
                break;
            default:
                break;
        }
        return convertView;
    }

    private ViewHolderForeCast buildHolder1(View convertView) {
        ViewHolderForeCast holder = new ViewHolderForeCast();
        // 未来几天天气
        for (int i = 0; i < 4; i++) {
            holder.mDate[i] = (MyTextView) convertView.findViewById(
                    mForeCastId[i]).findViewById(R.id.datetext);
            holder.mForecastDay[i] = (WeatherImageView) convertView
                    .findViewById(mForeCastId[i])
                    .findViewById(R.id.forecastday);
            holder.mForecastNight[i] = (WeatherImageView) convertView
                    .findViewById(mForeCastId[i]).findViewById(
                            R.id.forecastnight);
            holder.mTempLow[i] = (MyTextView) convertView.findViewById(
                    mForeCastId[i]).findViewById(R.id.temp_low);
            holder.mTempHeight[i] = (MyTextView) convertView
                    .findViewById(mForeCastId[i])
                    .findViewById(R.id.temp_height);
        }
        return holder;
    }

    private ViewHolderDetail buildHolder2(View convertView) {
        ViewHolderDetail holder = new ViewHolderDetail();

        // 详细天气信息
        holder.mForecastimg = (WeatherImageView) convertView
                .findViewById(R.id.forecast_img);
        holder.mWweatherText = (MyTextView) convertView
                .findViewById(R.id.weather_text1);
        holder.mTtempText = (MyTextView) convertView
                .findViewById(R.id.temp_text1);
        holder.mWindText = (MyTextView) convertView
                .findViewById(R.id.wind_text1);
        return holder;
    }

    private ViewHolderIndex buildHolder3(View convertView) {
        ViewHolderIndex holder = new ViewHolderIndex();
        if(mIndexDataBean.size()==5){
            convertView.findViewById(R.id.index_zyx).setVisibility(View.GONE);
        }
        // 指数
        for (int i = 0; i < mIndexDataBean.size(); i++) {
            holder.mIndexImg[i] = (ImageView) convertView.findViewById(
                    mIndexId[i]).findViewById(R.id.index_img);
            holder.mIndexTitle[i] = (MyTextView) convertView
                    .findViewById(mIndexId[i]).findViewById(R.id.index_title);
            holder.mIndexZs[i] = (MyTextView) convertView.findViewById(
                    mIndexId[i]).findViewById(R.id.index_zs);
            holder.mIndexDes[i] = (MyTextView) convertView.findViewById(
                    mIndexId[i]).findViewById(R.id.index_des);
        }
        return holder;
    }

    private void bindForeCastData(ViewHolderForeCast holder, int position) {
        if (mWeatherDataBean != null && mWeatherDataBean.size() > 0) {
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    holder.mDate[i].setText("今天");
                } else {
                    holder.mDate[i].setText(mWeatherDataBean.get(i).getDate());
                }
                holder.mForecastDay[i].loadImage(mWeatherDataBean.get(i)
                        .getDayPictureUrl());
                holder.mForecastNight[i].loadImage(mWeatherDataBean.get(i)
                        .getNightPictureUrl());
                String tempDur[] = mWeatherDataBean.get(i).getTemperature()
                        .split(" ~");
                int end = tempDur[1].lastIndexOf("℃");
                holder.mTempLow[i].setText(tempDur[1].substring(0, end) + "℃");
                holder.mTempHeight[i].setText(tempDur[0] + "℃");
            }
        }
    }

    private void bindWeatherDetailsData(ViewHolderDetail holder, int position) {
        if (mWeatherDataBean != null && mWeatherDataBean.size() > 0) {
            // 图片来源于
            String drawableUrl = "drawable://"
                    + mWeatherImg.get(mWeatherDataBean.get(0).getWeather());
            // L.e("WeatherInfoUtil:"+mWeatherDataBean.get(0).getWeather());
            if (mWeatherImg.get(mWeatherDataBean.get(0).getWeather()) != null) {
                holder.mForecastimg.loadImage(drawableUrl);
            } else {
                String Url = "drawable://" + mWeatherImg.get("未知");
                holder.mForecastimg.loadImage(Url);
            }
            holder.mWweatherText.setText(mWeatherDataBean.get(0).getWeather());
            String temp = mWeatherDataBean.get(0).getDate();
            int start = temp.lastIndexOf("实时");
            int end = temp.lastIndexOf("℃");
            holder.mTtempText.setText(temp.substring(start + 3, end) + "°");
            holder.mWindText.setText(mWeatherDataBean.get(0).getWind());
        }
    }

    private void bindIndexData(ViewHolderIndex holder, int position) {
        if (mIndexDataBean != null && mIndexDataBean.size() > 0) {
            // 指数
            if (mIndexDataBean.size()==5){
                for (int i = 0; i < mIndexDataBean.size(); i++) {
                    holder.mIndexImg[i].setImageResource(mIndexDrawableId2[i]);
                    holder.mIndexTitle[i].setText(mIndexDataBean.get(i).getTipt());
                    holder.mIndexZs[i].setText(mIndexDataBean.get(i).getZs());
                    holder.mIndexDes[i].setText(mIndexDataBean.get(i).getDes());
                }
            }else {
                for (int i = 0; i < mIndexDataBean.size(); i++) {
                    holder.mIndexImg[i].setImageResource(mIndexDrawableId[i]);
                    holder.mIndexTitle[i].setText(mIndexDataBean.get(i).getTipt());
                    holder.mIndexZs[i].setText(mIndexDataBean.get(i).getZs());
                    holder.mIndexDes[i].setText(mIndexDataBean.get(i).getDes());
                }
            }
        }
    }

    private class ViewHolderForeCast {
        // 未来几天天气预报
        MyTextView mDate[] = new MyTextView[4];
        WeatherImageView mForecastDay[] = new WeatherImageView[4];
        WeatherImageView mForecastNight[] = new WeatherImageView[4];
        MyTextView mTempLow[] = new MyTextView[4];
        MyTextView mTempHeight[] = new MyTextView[4];
    }

    private class ViewHolderDetail {
        // 天气详情
        WeatherImageView mForecastimg;
        MyTextView mWweatherText;
        MyTextView mTtempText;
        MyTextView mWindText;
    }

    private class ViewHolderIndex {
        // 指数
        ImageView mIndexImg[] = new ImageView[6];
        MyTextView mIndexTitle[] = new MyTextView[6];
        MyTextView mIndexZs[] = new MyTextView[6];
        MyTextView mIndexDes[] = new MyTextView[6];
    }
}
