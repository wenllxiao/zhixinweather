package com.zhixin.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.way.ui.swipeback.SwipeBackActivity;
import com.zhixin.myview.MyTextView;
import com.zhixin.utils.T;

import static com.baidu.location.h.j.v;

public class FeedBackActivity extends SwipeBackActivity {
    private EditText mFeedBackEt;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_view);
        initViews();
    }

    private void initViews() {
        ((MyTextView) findViewById(R.id.top_title)).setText("意见反馈");
        findViewById(R.id.back_bar_Button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFeedBackEt = (EditText) findViewById(R.id.fee_back_edit);
        mSendBtn = (Button) findViewById(R.id.feed_back_btn);
        mSendBtn.setOnClickListener(l);
    }

    OnClickListener l = new OnClickListener(){
        @Override
        public void onClick(View v) {
            String content = mFeedBackEt.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "知心天气 - 信息反馈");
                intent.putExtra(Intent.EXTRA_TEXT, content);
                intent.setData(Uri.parse("mailto:1766857923@qq.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                FeedBackActivity.this.startActivity(intent);
            } else {
                T.showShort(FeedBackActivity.this, "亲,内容不能为空哦！");
            }
        }
    };
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }
}
