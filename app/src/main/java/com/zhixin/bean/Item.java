package com.zhixin.bean;

public class Item {
	public static final int FEEDBACK_ID = 100001;
	public static final int SHARE_ID =100002;
	public static final int ABOUT_ID = 100003;
	public static final int EDIT_ID=100004;
	public int mId;
	public String mTitleStr;
	public int mTitleRes;
	public int mIconRes;

	public Item(int id, String title, int iconRes) {
		mId = id;
		mTitleStr = title;
		mIconRes = iconRes;
	}

	public Item(int id, int title, int iconRes) {
		mId = id;
		mTitleRes = title;
		mIconRes = iconRes;
	}
}
