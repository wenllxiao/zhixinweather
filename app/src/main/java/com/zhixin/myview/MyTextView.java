package com.zhixin.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhixin.weather.R;

import java.util.Hashtable;

/**
 * Created by v_wenlxiao on 2017/3/6.
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode())
            return;
        /*获取自定义属性数组*/
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String font = a.getString(R.styleable.TypefacedTextView_typeface);
        if (font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if (tf != null) {
            this.setTypeface(tf);
        }
        a.recycle();
        ;
    }

    private static class FontCache {
        private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

        public static Typeface get(String name, Context context) {
            Typeface tf = fontCache.get(name);
            if (tf == null) {
                try {
                    tf = Typeface.createFromAsset(context.getAssets(), name);
                } catch (Exception e) {
                    return null;
                }
                fontCache.put(name, tf);
            }
            return tf;
        }
    }
}
