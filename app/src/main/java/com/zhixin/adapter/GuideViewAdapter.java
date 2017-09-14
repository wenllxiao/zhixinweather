package com.zhixin.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.baidu.location.h.j.v;

public class GuideViewAdapter extends PagerAdapter {
    //界面列表
    private ArrayList<View> views;

    public GuideViewAdapter(ArrayList<View> views) {
        this.views = views;
    }

    /**
     * 初始化position位置的view，并返回界面id
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return position;
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    /**
     * 返回要滑动的view的个数
     *
     * @return
     */
    @Override
    public int getCount() {
        if (views != null)
            return views.size();
        return 0;
    }

    /**
     * 根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (views.get((int) Integer.parseInt(object.toString())));
    }
}
