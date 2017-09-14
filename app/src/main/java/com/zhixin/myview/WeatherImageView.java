package com.zhixin.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.baidu.location.h.j.p;

/**
 * Created by v_wenlxiao on 2017/3/9.
 * 继承自ImageView，用于异步加载图片，在下载图片时使用设置的loading图片占位，图片下载好后刷新View
 */
public class WeatherImageView extends ImageView{
    /**
     * 用于记录默认下载中状态的图片
     */
    private int downLoadingImageId=0;
    private int downLoadingImagefailureId=0;

    private boolean loadSuccess=false;//图片是否加载成功
    public WeatherImageView(Context context) {
        super(context);
    }
    public WeatherImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 设置下载中的图片 与下载失败的图片，不设置将使用默认图片
     * @param downlding
     * @param failureId
     */
    public void setDefaultDownLoadAndFailureImage(int downlding, int failureId) {
        downLoadingImageId = downlding;
        downLoadingImagefailureId = failureId;
    }

    /**
     * 对外接口,用于调用ImageView的异步下载图片
     * @param url 图片的URL
     */
    public void loadImage(String url){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(downLoadingImageId)
                .showImageForEmptyUri(downLoadingImagefailureId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageOnFail(downLoadingImagefailureId)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(url, this, options);
    }
    public boolean isLoadSuccess() {
        return loadSuccess;
    }
}
