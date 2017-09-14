package com.zhixin.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhixin.weather.R;

/**
 * Created by v_wenlxiao on 2017/3/9.
 * ListView滑动添加删除Item按钮
 */

public class EditCityListView extends ListView implements View.OnTouchListener, GestureDetector.OnGestureListener {
    //首先需要定义一个GestureDetector：
    private GestureDetector gestureDetector;
    //为删除按钮提供一个回调接口
    private OnDeleteListener listener;

    private View deleteButton;

    private ViewGroup itemLayout;

    private int selectedItem;

    private boolean isDeleteShown;
    private View v;

    public EditCityListView(Context context) {
        super(context);
    }

    public EditCityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    public EditCityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnDeleteListener(OnDeleteListener l) {
        listener = l;
    }

    public interface OnDeleteListener {
        void onDelete(int position, boolean deleteNot);
    }

    //OnTouchListener只能监听到三种触摸事件，即按下，移动，松开
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // 将MotionEvent事件传到GestureDetector
        return gestureDetector.onTouchEvent(motionEvent);
    }

    /*GestureDetector.OnGestureListener：用来通知普通的手势事件，该接口有如下六个回调函数：
            1.   onDown(MotionEvent e)：down事件；
            2.   onSingleTapUp(MotionEvent e)：一次点击up事件；
            3.   onShowPress(MotionEvent e)：down事件发生而move或则up还没发生前触发该事件；
            4.   onLongPress(MotionEvent e)：长按事件；
            5.   onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)：滑动手势事件；
            6.   onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)：在屏幕上拖动事件。
  */
    @Override
    public boolean onDown(MotionEvent e) {
        //L.e(TAG, "onDown msg");
        if (this.v != null)
            this.v.setVisibility(VISIBLE);
        //当前所在位置
        int mCurrentPosition = pointToPosition(0, (int) e.getY());
        if (mCurrentPosition == INVALID_POSITION) {
            return true;
        }
        if (isDeleteShown) {
            // L.e("isDeleteShown msg" + isDeleteShown);
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShown = false;
        }
        selectedItem = pointToPosition((int) e.getX(), (int) e.getY());
        setItemChecked(selectedItem, true);
        // L.e("selectedItem:" + selectedItem);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        //L.e("onFling msg");
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)
                && velocityX < 0 & Math.abs(velocityX) > 80) {
            deleteButton = LayoutInflater.from(getContext()).inflate(
                    R.layout.delete_button, new LinearLayout(getContext()),false);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    listener.onDelete(selectedItem, true);
                }
            });
            //（从当前页面可见的开始）获取一行的布局
            itemLayout = (ViewGroup) getChildAt(selectedItem
                    - getFirstVisiblePosition());
            //获取布局的最后一个子组件
            v = itemLayout.getChildAt(itemLayout.getChildCount() - 1);
            //获取布局的第二个子组件
            View vImg = itemLayout.getChildAt(1);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_TOP, vImg.getId());
            params.addRule(RelativeLayout.ALIGN_BOTTOM, vImg.getId());
            params.setMargins(0, 0, 15, 0);
            v.setVisibility(View.GONE);
            //（将Delete按钮添加到当前点击的Item上
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        }
        return false;
    }
}
