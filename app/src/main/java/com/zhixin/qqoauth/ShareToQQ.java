package com.zhixin.qqoauth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhixin.ImageLoader.CutViewCapture;
import com.zhixin.weather.WeatherMainActivity;

public class ShareToQQ {
	private Context mContext;

	public ShareToQQ(Context mContext) {
		this.mContext = mContext;
	}

	public void shareToQQ(int shareType) {
		if (WeatherMainActivity.mTencent != null) {
			final Bundle params = new Bundle();
			switch (shareType) {
			case 1:
				params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
						QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
				params.putString(QQShare.SHARE_TO_QQ_TITLE, "知心天气");
				params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "关注天气，呵护健康!");
				params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
						"http://a.app.qq.com/o/simple.jsp?pkgname=com.zhixin.weather");
				params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
						CutViewCapture.getBitmapFromFile());
				break;
			case 2:
				params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
						QQShare.SHARE_TO_QQ_TYPE_IMAGE);
				params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
						CutViewCapture.getBitmapFromFile());
				break;
			default:
				break;
			}
			WeatherMainActivity.mTencent.shareToQQ((Activity) mContext, params,
					qqShareListener);
		}
	}

	public IUiListener getShareListener() {
		return qqShareListener;
	}

	public IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			Toast.makeText(mContext, "分享成功", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(mContext, "分享失败", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "取消分享", Toast.LENGTH_LONG).show();
		}
	};
}
