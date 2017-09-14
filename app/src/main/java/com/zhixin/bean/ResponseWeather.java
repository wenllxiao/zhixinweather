package com.zhixin.bean;
import java.util.List;
public class ResponseWeather{
	private String error; // 返回码
	private String status;// 是否成功
	private String date;// 日期
	private List<WeatherInfo> results;// 返回结果

	public ResponseWeather(){
	}
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<WeatherInfo> getResults() {
		return results;
	}

	public void setResults(List<WeatherInfo> results) {
		this.results = results;
	}
}
