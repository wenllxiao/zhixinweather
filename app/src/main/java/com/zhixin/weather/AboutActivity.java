package com.zhixin.weather;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.way.ui.swipeback.SwipeBackActivity;
import com.zhixin.myview.MyTextView;
/**
 * Created by v_wenlxiao on 2017/3/8.
 */

public class AboutActivity extends SwipeBackActivity{
    private ImageView mBackBarButton;
    private MyTextView mTile;
    private MyTextView mVersionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_view);
        initViews();
    }
    private  void initViews(){
        mBackBarButton= (ImageView) findViewById(R.id.back_bar_Button);
        mBackBarButton.setOnClickListener(l);
        mTile= (MyTextView) findViewById(R.id.top_title);
        mTile.setText("关于应用");
        mVersionName= (MyTextView) findViewById(R.id.version_name);
        mVersionName.setText("当前版本："+BuildConfig.VERSION_NAME);
    }
    View.OnClickListener l=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
