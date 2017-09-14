package com.zhixin.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.zhixin.bean.IndexDataBean;
import com.zhixin.bean.ResponseWeather;
import com.zhixin.bean.WeatherDataBean;
import com.zhixin.bean.WeatherInfo;

public class WeatherInfoUtil {
	public static ResponseWeather mResponseWeather;
	public static List<WeatherInfo> mResults;
	public static List<WeatherDataBean> mWeatherDataBean;
	public static List<IndexDataBean> mIndexDataBean;

	public WeatherInfoUtil() {
	}

	public static ResponseWeather getJresponseWeather() {
		return mResponseWeather;
	}

	public void setJresponseWeather(ResponseWeather jresponseWeather) {
		WeatherInfoUtil.mResponseWeather = jresponseWeather;
	}

	public List<WeatherInfo> getJresults() {
		return mResults;
	}

	public void setJresults(List<WeatherInfo> jresults) {
		WeatherInfoUtil.mResults = jresults;
	}

	public List<WeatherDataBean> getJweatherDataBean() {
		return mWeatherDataBean;
	}

	public void setJweatherDataBean(List<WeatherDataBean> jweatherDataBean) {
		WeatherInfoUtil.mWeatherDataBean = jweatherDataBean;
	}

	public List<IndexDataBean> getJindexDataBean() {
		return mIndexDataBean;
	}

	public void setJindexDataBean(List<IndexDataBean> jindexDataBean) {
		WeatherInfoUtil.mIndexDataBean = jindexDataBean;
	}

	public static void JsonParser(String jsonStr) {
		mResults=null;
		mWeatherDataBean=null; 
		mIndexDataBean=null;
		mResponseWeather = new ResponseWeather();
		mResults = new ArrayList<WeatherInfo>();
		mWeatherDataBean = new ArrayList<WeatherDataBean>();
		mIndexDataBean = new ArrayList<IndexDataBean>();
		if (jsonStr != null) {
			try {
				JSONObject jsonFirst = new JSONObject(jsonStr);
				if (jsonFirst.getString("error").equals("0")) {
					// Log.e("error", jsonFirst.getString("error"));
					mResponseWeather.setError(jsonFirst.getString("error")
							.toString());
					mResponseWeather.setStatus(jsonFirst.getString("status")
							.toString());
					mResponseWeather.setDate(jsonFirst.getString("date")
							.toString());
					/** results */
					JSONArray jsonArrayResults = (JSONArray) jsonFirst
							.get("results");
					for (int i = 0; i < jsonArrayResults.length(); i++) {
						WeatherInfo wb = new WeatherInfo();
						JSONObject jsonObjectWeatherData = (JSONObject) jsonArrayResults
								.get(i);
						wb.setCurrentCity(jsonObjectWeatherData.getString(
								"currentCity").toString());
						wb.setPm25(jsonObjectWeatherData.getString("pm25")
								.toString());
						/* index */
						JSONArray jsonArrayindex = (JSONArray) jsonObjectWeatherData
								.get("index");
						for (int x = 0; x < jsonArrayindex.length(); x++) {
							IndexDataBean idb = new IndexDataBean();
							JSONObject jsonindex = (JSONObject) jsonArrayindex
									.get(x);
							idb.setTitle(jsonindex.getString("title")
									.toString());
							idb.setZs(jsonindex.getString("zs").toString());
							idb.setDes(jsonindex.getString("des").toString());
							idb.setTipt(jsonindex.getString("tipt").toString());
							mIndexDataBean.add(idb);
						}
						/* weather_data */
						JSONArray jsonArrayWeatherDataBean = (JSONArray) jsonObjectWeatherData
								.get("weather_data");
						for (int x = 0; x < jsonArrayWeatherDataBean.length(); x++) {
							WeatherDataBean wdb = new WeatherDataBean();
							JSONObject jsonweatherdata = (JSONObject) jsonArrayWeatherDataBean
									.get(x);
							wdb.setDate(jsonweatherdata.getString("date")
									.toString());
							wdb.setDayPictureUrl(jsonweatherdata.getString(
									"dayPictureUrl").toString());
							wdb.setNightPictureUrl(jsonweatherdata.getString(
									"nightPictureUrl").toString());
							wdb.setTemperature(jsonweatherdata.getString(
									"temperature").toString());
							wdb.setWeather(jsonweatherdata.getString("weather")
									.toString());
							wdb.setWind(jsonweatherdata.getString("wind")
									.toString());
							mWeatherDataBean.add(wdb);
						}
						wb.setIndex(mIndexDataBean);
						wb.setWeatherData(mWeatherDataBean);
						mResults.add(wb);
					}
					mResponseWeather.setResults(mResults);
				} else {
					// Log.e("error", jsonFirst.getString("error"));
					mResponseWeather.setError(jsonFirst.getString("error")
							.toString());
					mResponseWeather.setStatus(jsonFirst.getString("status")
							.toString());
					mResponseWeather.setDate(jsonFirst.getString("date")
							.toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
