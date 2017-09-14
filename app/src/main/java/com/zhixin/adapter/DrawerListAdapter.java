package com.zhixin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhixin.bean.Category;
import com.zhixin.bean.CityBean;
import com.zhixin.bean.Item;
import com.zhixin.weather.R;

public class DrawerListAdapter extends BaseAdapter {
	private static final int ITEM_TYPE = 0;
	private static final int CATEGORY_TYPE = 1;
	public List<Object> mItems;
	private LayoutInflater mLayoutInflater;
	public DrawerListAdapter(Context context) {
		mItems = new ArrayList<Object>();
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void addAllCitys(List<CityBean> tmpCities) {
		mItems.clear();
		mItems.add(new Item(Item.EDIT_ID, "编辑地点", R.mipmap.editloc));
		for (int i = 0; i < tmpCities.size(); i++)
			mItems.add(new Item(i, tmpCities.get(i).getCity(), R.mipmap.loc));
		mItems.add(new Category("工具"));
		mItems.add(new Item(Item.FEEDBACK_ID, "意见反馈",
				R.drawable.send_feedback_selector));
		mItems.add(new Item(Item.SHARE_ID, "分享应用", R.drawable.share_selector));
		mItems.add(new Item(Item.ABOUT_ID, "关于", R.drawable.about_selector));
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position) instanceof Item ? ITEM_TYPE : CATEGORY_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position) instanceof Item;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int type = getItemViewType(position);
		if (convertView == null
				|| convertView.getTag(R.mipmap.heart_logo + type) == null) {
			switch (type) {
			case ITEM_TYPE:
				convertView = mLayoutInflater.inflate(
						R.layout.sidemenu_list_item_item, parent, false);
				break;
			case CATEGORY_TYPE:
				convertView = mLayoutInflater.inflate(
						R.layout.sidemenu_list_item_category, parent, false);
				break;
			default:
				break;
			}
			viewHolder = buildHolder(convertView);
			convertView.setTag(R.mipmap.heart_logo + type, viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView
					.getTag(R.mipmap.heart_logo + type);
		}
		bindViewData(viewHolder, position);
		return convertView;
	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.titleView = (TextView) convertView;
		return holder;
	}

	private void bindViewData(ViewHolder holder, final int position) {
		Object item = getItem(position);
		switch (getItemViewType(position)) {
		case ITEM_TYPE:
			holder.titleView.setText(((Item) item).mTitleStr);
			holder.titleView.setCompoundDrawablesWithIntrinsicBounds(
					((Item) item).mIconRes, 0, 0, 0);
			break;
		case CATEGORY_TYPE:
			holder.titleView.setText(((Category) item).mTitleStr);
			break;
		default:
			break;
		}
	}

	private static final class ViewHolder {
		TextView titleView;
	}
}
