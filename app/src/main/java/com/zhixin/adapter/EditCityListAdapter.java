package com.zhixin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhixin.bean.CityBean;
import com.zhixin.bean.WeatherDataBean;
import com.zhixin.myview.MyTextView;
import com.zhixin.myview.WeatherImageView;
import com.zhixin.utils.WeatherInfoUtil;
import com.zhixin.weather.R;
import com.zhixin.weather.WeatherMainActivity;

public class EditCityListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<CityBean> tempCities;
	private Map<String, List<WeatherDataBean>> jweatherDataBean;

	public EditCityListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		tempCities = new ArrayList<CityBean>();
		jweatherDataBean = new HashMap<String, List<WeatherDataBean>>();
		mLayoutInflater = LayoutInflater.from(context);
	}

	private void initData() {
		if (WeatherMainActivity.mWeatherMap.size() != 0)
			for (CityBean city : tempCities) {
				WeatherInfoUtil.JsonParser(WeatherMainActivity.mWeatherMap.get(city
						.getCity()));
				jweatherDataBean.put(city.getCity(),
						WeatherInfoUtil.mWeatherDataBean);
			}
	}

	public void AddAllCitys(List<CityBean> tmpCities) {
		tempCities.clear();
		jweatherDataBean.clear();
		this.tempCities = tmpCities;
		initData();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tempCities.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tempCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(
					R.layout.edit_city_item_normal, parent, false);
			viewHolder = buildHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bindViewData(viewHolder, position);
		return convertView;
	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.mForecastImgDay = (WeatherImageView) convertView
				.findViewById(R.id.forecastday);
		holder.mForecastImgNight = (WeatherImageView) convertView
				.findViewById(R.id.forecastnight);
		holder.mCityName = (MyTextView) convertView
				.findViewById(R.id.city_name);
		holder.mTmp = (MyTextView) convertView
				.findViewById(R.id.temp_id);
		return holder;
	}

	private void bindViewData(ViewHolder holder, int position) {
		List<WeatherDataBean> wdb = new ArrayList<WeatherDataBean>();
		wdb = jweatherDataBean.get(tempCities.get(position).getCity());
		if (wdb != null && wdb.size() > 0) {
			holder.mForecastImgDay.loadImage(wdb.get(0).getDayPictureUrl());
			holder.mForecastImgNight.loadImage(wdb.get(0).getNightPictureUrl());
			String tempDur[] = wdb.get(0).getTemperature().split(" ~");
			int end = tempDur[1].lastIndexOf("℃");
			holder.mTmp.setText(tempDur[1].substring(0, end) + "/" + tempDur[0]
					+ "℃");
		} else {
			holder.mForecastImgDay.loadImage(null);
			holder.mForecastImgNight.loadImage(null);
			holder.mTmp.setText("__/__");
		}
		holder.mCityName.setText(tempCities.get(position).getCity());
	}

	private class ViewHolder {
		private WeatherImageView mForecastImgDay;
		private WeatherImageView mForecastImgNight;
		private MyTextView mCityName;
		private MyTextView  mTmp;
	}
}
