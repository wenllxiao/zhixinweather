package com.zhixin.utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.os.AsyncTask;

import com.zhixin.bean.CityBean;
import com.zhixin.bean.RequestDataBean;
import com.zhixin.bean.ResponseWeather;
import com.zhixin.fragment.FragmentCallBack;

public class AsyncTaskWeather extends
		AsyncTask<List<CityBean>, Void, Map<String, String>> {
	public Map<String, String> mWeatherMap;
	private FragmentCallBack fActivity;
	private String TAG="AsyncTaskWeather";
	private Context mContext;
	public AsyncTaskWeather(Context mcContext) {
		this.mContext=mcContext;
		if(mWeatherMap!=null){
			mWeatherMap.clear();
			mWeatherMap=null;
		}
		mWeatherMap = new HashMap<String, String>();
		this.fActivity = (FragmentCallBack) mContext;
	}

	@Override
	protected Map<String, String> doInBackground(List<CityBean>... params) {
		// TODO Auto-generated method stub
		List<CityBean> cities = params[0];
			for (CityBean city : cities) {
				RequestDataBean.setLocation(city.getCity());
				HttpRequestInfo response = new HttpRequestInfo().fun(RequestDataBean
						.getData());
				if (response.getMessage().arg1 == 1) {
					try {
						String respones = response.getResponse();
						WeatherInfoUtil.JsonParser(respones);
						ResponseWeather rw = new ResponseWeather();
						rw = WeatherInfoUtil.getJresponseWeather();
						if (rw != null && rw.getStatus() != null && rw.getError().equals("0")) {
							mWeatherMap.put(city.getCity(), respones);
						} else {
							L.e(city.getCity(), "Null");
							mWeatherMap.put(city.getCity(), null);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						L.e(TAG, e.toString());
					}
				}
			}
			if (!mWeatherMap.isEmpty())
				return mWeatherMap;
			return null;
		}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Map<String, String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		fActivity.responseWeatherInfo(result);
	}
}
