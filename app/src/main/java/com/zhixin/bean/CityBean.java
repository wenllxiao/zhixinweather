package com.zhixin.bean;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class CityBean implements Parcelable {
	@DatabaseField(generatedId = true)
	private int id;  
	@DatabaseField(columnName = "cityCode")
	private String cityCode;
	@DatabaseField(columnName = "district")
	private String district;
	@DatabaseField(columnName = "city")
	private String city;
	@DatabaseField(columnName = "addRess")
	private String addRess;
	@DatabaseField(columnName = "isSelected")
	private int isSelected;
	@DatabaseField(columnName = "isLocation")
	private int isLocation;
	public CityBean() {
	}
	public CityBean(String district, String city, String addRess, String cityCode,
			int isSelected,int isLocation) {
		super();
		this.district = district;
		this.city = city;
		this.addRess = addRess;
		this.isSelected = isSelected;
		this.cityCode = cityCode;
		this.isLocation=isLocation;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the addRess
	 */
	public String getAddRess() {
		return addRess;
	}
	/**
	 * @param addRess the addRess to set
	 */
	public void setAddRess(String addRess) {
		this.addRess = addRess;
	}
	/**
	 * @return the isSelected
	 */
	public int getIsSelected() {
		return isSelected;
	}
	/**
	 * @param isSelected the isSelected to set
	 */
	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the isLocation
	 */
	public int getIsLocation() {
		return isLocation;
	}
	/**
	 * @param isLocation the isLocation to set
	 */
	public void setIsLocation(int isLocation) {
		this.isLocation = isLocation;
	}
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof CityBean) {
			CityBean item = (CityBean) o;
			if (item.getCityCode().equals(this.cityCode))
				return true;
		}
		return false;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(city);
		dest.writeString(addRess);
		dest.writeString(cityCode);
		dest.writeInt(isSelected);
		dest.writeInt(isLocation);
		dest.writeString(district);
	}

	public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
		@Override
		public CityBean createFromParcel(Parcel source) {
			return new CityBean(source);
		}

		@Override
		public CityBean[] newArray(int size) {
			return new CityBean[size];
		}
	};

	public CityBean(Parcel source) {
		id=source.readInt();
		city = source.readString();
		addRess = source.readString();
		cityCode = source.readString();
		isSelected = source.readInt();
		isLocation = source.readInt();
		district = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
