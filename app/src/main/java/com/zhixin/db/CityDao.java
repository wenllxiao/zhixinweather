package com.zhixin.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhixin.bean.CityBean;

public class CityDao {
	private Dao<CityBean, Integer> cityDaoOpe;

	private DatabaseHelper helper;

	public CityDao(Context context) {

		try {
			helper = DatabaseHelper.getHelper(context);

			cityDaoOpe = helper.getDao(CityBean.class);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取热门城市数组
	 * 
	 * @return
	 */
	public List<CityBean> getHotCities() {
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();

			return builder.query();

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 根据城市名查询城市
	 * 
	 * @param  cityName
	 * @return
	 */
	public CityBean getCityBycityName(String cityName) {

		CityBean city = new CityBean();
		try {

			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();

			builder.where().eq("city", cityName);

			List<CityBean> customerList = builder.query();

			if (customerList.size() > 0) {

				city = customerList.get(0);
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return city;
	}

	/**
	 * 获取当前定位的城市数据
	 * 
	 * @return
	 */
	public CityBean getLocationCityFromDB() {

		CityBean city = new CityBean();
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			builder.where().eq("isLocation", 1);
			List<CityBean> customerList = builder.query();
			if (customerList.size() > 0) {
				city = customerList.get(0);
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return city;
	}

	/**
	 * 获取临时城市数组
	 * 
	 * @return
	 */
	public List<CityBean> getTmpCities() {
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();

			builder.where().eq("isSelected", "1");

			return builder.query();

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 是否包涵在热门城市里
	 * 
	 * @return
	 */
	public boolean InHotCities(String cityName) {
		List<CityBean> list = new ArrayList<CityBean>();
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			builder.where().eq("city", cityName);
			list = builder.query();
			if (list.size() > 0) {
				return true;
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return false;
	}

	/**
	 * 是否包涵在临时城市里
	 * 
	 * @return
	 */
	public CityBean InTmpCities(String cityName) {
		List<CityBean> list = new ArrayList<CityBean>();
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			builder.where().eq("isSelected", "1").and().eq("city", cityName);
			list = builder.query();
			if (list.size() > 0) {

				return list.get(0);
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 是否是定位的城市
	 * 
	 * @return
	 */
	public Boolean IsLocationCity(String cityName) {
		List<CityBean> list = new ArrayList<CityBean>();
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			builder.where().eq("isLocation", "1").and().eq("city", cityName);
			list = builder.query();
			if (list.size() > 0) {
				return true;
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return false;
	}

	/**
	 * 更新城市为临时城市
	 * 
	 * @param city
	 */
	public void UpdateTmpCity(String city) {
		UpdateBuilder<CityBean, Integer> updateBuilder = cityDaoOpe
				.updateBuilder();
		// only update the rows where password is null
		try {
			updateBuilder.updateColumnValue("isSelected", "1");
			updateBuilder.where().eq("city", city);
			updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取热门城市数组
	 * 
	 * @return
	 */
	public List<CityBean> getAllCity() {
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			return builder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 增加一个城市
	 * 
	 * @param city
	 */
	public void add(CityBean city) {
		try {
			cityDaoOpe.create(city);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新定位的城市
	 * 
	 * @return
	 */
	public void UpdateOrAddLodationCity(CityBean city) {
		CityBean cityx = getCityBycityName(city.getCity());
		if (cityx.getCity() != null) {
			UpdateBuilder<CityBean, Integer> updateBuilder = cityDaoOpe
					.updateBuilder();
			// only update the rows where password is null
			try {
				updateBuilder.updateColumnValue("isLocation", "1");
				updateBuilder.updateColumnValue("isSelected", "1");
				updateBuilder.where().eq("city", cityx.getCity());
				updateBuilder.update();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		DeleteBuilder<CityBean, Integer> deleteBuilder = cityDaoOpe.deleteBuilder();
		try {
			deleteBuilder.where().eq("isLocation", "1");
			deleteBuilder.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(city);
	}

	/**
	 * 根据城市名删除城市
	 * 
	 * @param cityName
	 */
	public void deleteCity(String cityName) {
		UpdateBuilder<CityBean, Integer> updateBuilder = cityDaoOpe
				.updateBuilder();
		// only update the rows where password is null
		try {
			updateBuilder.updateColumnValue("isSelected", "0");
			updateBuilder.where().eq("city", cityName.trim());
			updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 未选择的城市
	 * 
	 * @return
	 */
	public String GetFirstNotSelectedCity() {
		List<CityBean> list = new ArrayList<CityBean>();
		try {
			QueryBuilder<CityBean, Integer> builder = cityDaoOpe.queryBuilder();
			builder.where().eq("isSelected", "0");
			if (builder.query() instanceof List)
				list = builder.query();
			if (list.size() > 0) {
				return list.get(list.size() - 1).getCity();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除并添加城市
	 * 
	 * @param cityName
	 */
	public void DeleteAndAddCity(String cityName) {
		if (GetFirstNotSelectedCity() != null) {
			UpdateBuilder<CityBean, Integer> updateBuilder = cityDaoOpe
					.updateBuilder();
			try {
				updateBuilder.updateColumnValue("city", cityName);
				updateBuilder.updateColumnValue("isSelected", 1);
				updateBuilder.where().eq("city", GetFirstNotSelectedCity());
				updateBuilder.update();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}
}
