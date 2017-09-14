package com.zhixin.qqoauth;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhixin.utils.T;
import com.zhixin.weather.WeatherMainActivity;

public class OauthLogin {
    private Context mContext;
    private ReturnJson mReturnJson;
    private static final int LOG_IN = 0;
    private static final int LOG_OUT = 1;

    public interface ReturnJson {
        public void mSetJson(int state);
    }

    public OauthLogin(Context mContext, ReturnJson mReturnJson) {
        this.mReturnJson = mReturnJson;
        this.mContext = mContext;
    }

    public void doLogin() {
        if (WeatherMainActivity.mTencent != null && !WeatherMainActivity.mTencent.isSessionValid()) {
            WeatherMainActivity.mTencent.login((Activity) mContext, "all", loginListener);
        } else {
            WeatherMainActivity.mTencent.logout(mContext);
            mReturnJson.mSetJson(LOG_OUT);
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            mReturnJson.mSetJson(LOG_IN);
        }
    };

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                WeatherMainActivity.mTencent.setAccessToken(token, expires);
                WeatherMainActivity.mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    public IUiListener getLoginListener() {
        return loginListener;
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                T.showShort(mContext, "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                T.showShort(mContext, "登录失败");
                return;
            }
            T.showShort(mContext, "登录成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            T.showShort(mContext, "onError: " + e.errorDetail);
        }

        @Override
        public void onCancel() {
            T.showShort(mContext, "onCancel:");
        }
    }
}
