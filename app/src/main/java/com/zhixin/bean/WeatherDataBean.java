package com.zhixin.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherDataBean implements Parcelable{
	private String date;// 天气预报时间
	private String dayPictureUrl;// 白天的天气预报图片url
	private String nightPictureUrl;// 晚上的天气预报图片url
	private String weather;// 天气状况
	private String wind;// 风力
	private String temperature;// 温度

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayPictureUrl() {
		return dayPictureUrl;
	}

	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}

	public String getNightPictureUrl() {
		return nightPictureUrl;
	}

	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static final Creator<WeatherDataBean> CREATOR =new Creator<WeatherDataBean>(){
		@Override
		public WeatherDataBean[] newArray(int size) {
			return new WeatherDataBean[size];
		}

		@Override
		public WeatherDataBean createFromParcel(Parcel in) {

			return new WeatherDataBean(in);
		}
	};
	public WeatherDataBean(){}
	public WeatherDataBean(Parcel in){
		date=in.readString();
		dayPictureUrl=in.readString();
		nightPictureUrl=in.readString();
		weather=in.readString();
		wind=in.readString();
		temperature=in.readString();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(date);// 天气预报时间
		dest.writeString(dayPictureUrl);// 白天的天气预报图片url
		dest.writeString(nightPictureUrl);// 晚上的天气预报图片url
		dest.writeString(weather);// 天气状况
		dest.writeString(wind);// 风力
		dest.writeString(temperature);// 温度
	}

}
