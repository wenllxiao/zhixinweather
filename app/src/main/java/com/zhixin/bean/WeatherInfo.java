package com.zhixin.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherInfo implements Parcelable {
	private String currentCity;// 锟斤拷前锟斤拷锟斤拷
	private List<WeatherDataBean> weatherData;// 锟斤拷锟斤拷预锟斤拷锟斤拷息
	private String pm25;// PM2.5值
	private List<IndexDataBean> index;// 锟斤拷锟斤拷指锟斤拷

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public List<WeatherDataBean> getWeatherData() {
		return weatherData;
	}

	public void setWeatherData(List<WeatherDataBean> weatherData) {
		this.weatherData = weatherData;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public List<IndexDataBean> getIndex() {
		return index;
	}

	public void setIndex(List<IndexDataBean> index) {
		this.index = index;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Creator<WeatherInfo> CREATER = new Creator<WeatherInfo>() {

		@Override
		public WeatherInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new WeatherInfo[size];
		}

		@Override
		public WeatherInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new WeatherInfo(source);
		}
	};
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(currentCity);// 锟斤拷前锟斤拷锟斤拷
		dest.writeList(weatherData);
		dest.writeString(pm25) ;// PM2.5值
		dest.writeList(index);// 锟斤拷锟斤拷指锟斤拷
	}
	public WeatherInfo(Parcel source) {
		currentCity=source.readString();
		weatherData=new ArrayList<WeatherDataBean>();
		source.readList(weatherData,getClass().getClassLoader());
		pm25=source.readString();
		index=new ArrayList<IndexDataBean>();
		source.readList(index,getClass().getClassLoader());
	}
	public WeatherInfo() {

	}
}
