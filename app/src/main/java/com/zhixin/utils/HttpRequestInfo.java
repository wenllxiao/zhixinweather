package com.zhixin.utils;
import java.util.concurrent.TimeUnit;
import android.os.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequestInfo {
    private Message message;
    private String mResponse;
    public static final int succeed = 1;
    public static final int fail = 2;
    public HttpRequestInfo() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getResponse() {
        return  mResponse;
    }

    public void setResponse(String mResponse) {
        this. mResponse = mResponse;
    }

    public HttpRequestInfo fun(String param) {
        HttpRequestInfo mhe = new HttpRequestInfo();
        // TODO Auto-generated method stub
        Message Mesg = Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();
        Request request=new Request.Builder().url(param).build();
        String mResponse=null;
        try {
            Response response=okHttpClient.newCall(request).execute();// 执行一个请求
            if (response.isSuccessful()) {
                mResponse=response.body().string();
                Mesg.arg1 = succeed;// 保存网络状态
            } else {
                Mesg.arg1 = fail;// 保存网络状态
            }
        } catch (Exception e) {
            Mesg.arg1 = fail;
            e.printStackTrace();
        }
        mhe.setMessage(Mesg);
        mhe.setResponse(mResponse);
        return mhe;
    }
}
