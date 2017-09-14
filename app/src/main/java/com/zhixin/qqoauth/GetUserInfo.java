package com.zhixin.qqoauth;
import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhixin.weather.WeatherMainActivity;
import com.zhixin.utils.T;
public class GetUserInfo {
	private static UserInfo mInfo;

	public static interface UserInfoCallBack {
		void returnInfoCallBack(String mUserInfo);
	}

	public static void getUserInfo(final Context mContext,
			final UserInfoCallBack mUserInfoCallBack) {
		if (WeatherMainActivity.mTencent != null && WeatherMainActivity.mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {
					// mUserInfoCallBack.returnInfoCallBack("fail");
					T.showShort(mContext, e.toString());
				}

				@Override
				public void onComplete(final Object response) {
					String mInfox = response.toString();
					if (response != null)
						mUserInfoCallBack.returnInfoCallBack(mInfox);
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(mContext, WeatherMainActivity.mTencent.getQQToken());
			mInfo.getUserInfo(listener);
		}
	}
}
