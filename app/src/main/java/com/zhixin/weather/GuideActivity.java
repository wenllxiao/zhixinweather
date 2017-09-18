package com.zhixin.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhixin.adapter.GuideViewAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.db.CityDao;
import com.zhixin.utils.SPUtils;

import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static com.baidu.location.h.j.p;

/**
 * @author v_wenlxiao 程序入口
 */
public class GuideActivity extends Activity implements OnClickListener,OnPageChangeListener {
    private static final int mGuideNum = 3;
    private CityDao cityDao;
    //定义ViewPager对象
    private ViewPager viewPager;
    //定义ViewPager适配器
    private GuideViewAdapter gvAdapter;
    //定义一个ArrayList存放view对象
    private ArrayList<View> viewArayLists;
    //定义View数组放各个界面view对象
    private View[] views = new View[mGuideNum];
    //底部小点的图片
    private ImageView[] points;
    //记录当前选中的位置
    private int currentIndex;
    //存放用户是否第一次登录应用
    private static final String USER_LAUNCH_FINST = "USER_LUANCH";
    private boolean launch = false;
    private static final String[] cityname = { "深圳", "北京", "上海", "广州", "南京",
            "成都", "武汉", "杭州", "西安", "济南", "长春", "天津", "长沙", "呼和浩特", "石家庄",
            "福州", "海口", "乌鲁木齐", "台北", "香港", "澳门" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置界面无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        launch = (Boolean) SPUtils.get(GuideActivity.this, USER_LAUNCH_FINST, false);
//        if(launch){
//            Intent intent=new Intent();
//            intent.setClass(GuideActivity.this,WeatherMainActivity.class);
//            startActivity(intent);
//            finish();
//        }
        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        //实例化各个界面的布局
        LayoutInflater inf = LayoutInflater.from(this);
        views[0] = inf.inflate(R.layout.guide_page1, null);
        views[1] = inf.inflate(R.layout.guide_page2, null);
        views[2] = inf.inflate(R.layout.guide_page3, null);
        //实例化ArrayList对象
        viewArayLists = new ArrayList<View>();
        //实例化ViewPager适配器
        gvAdapter = new GuideViewAdapter(viewArayLists);
    }

    private void initData() {
        cityDao=new CityDao(this);
        if (!launch) {
            for (int i = 0; i < cityname.length; i++) {
                CityBean city;
                city = new CityBean(null, cityname[i], null, null, 0, 0);
                if (cityDao.getCityBycityName(cityname[i]).getCity() == null) {
                    cityDao.add(city);
                }
            }
        }
        // 设置监听
        viewPager.setOnPageChangeListener(this);
        //将要分页显示的view装入数组
        for (View v : views)
            viewArayLists.add(v);
        //设置适配器数据
        viewPager.setAdapter(gvAdapter);
        //标记已登录过
        SPUtils.put(getApplicationContext(), USER_LAUNCH_FINST, true);
        //初始化底部小点
        initPoint(viewArayLists.size());
    }

    public void startProgram(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this, WeatherMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 300);
    }

    /**
     * 通过点击事件来切换当前的页面
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        setCurView(position);
        setCurDot(position);
    }

    private void setCurView(int position) {
        if (position < 0 || position >= 4) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前页面的位置
     * @param size
     */
    private void initPoint(int size) {
        // TODO Auto-generated method stub
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.point_layout);
        points = new ImageView[size];
        // 循环取得小点图片
        for (int i = 0; i <size; i++) {
            points[i]=new ImageView(this);
            points[i].setClickable(true);
            points[i].setPadding(10,10,10,10);
            points[i].setImageResource(R.drawable.pointable);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置tag，方便取出与当前位置对应
            points[i].setTag(i);
            linearLayout.addView(points[i]);
        }

        //设置当前默认的位置
        currentIndex = 0;
        //设置选中状态,不可点击
        points[currentIndex].setEnabled(false);
    }

    /**
     * 设置当前的小点位置
     * @param position
     */
    private void setCurDot(int position) {
        if (position<0||position>3||currentIndex==position) {
          return;
        }
        points[position].setEnabled(false);
        points[currentIndex].setEnabled(true);
        currentIndex=position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    /**
     * 当新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int position) {
        setCurView(position);
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        points=null;
    }
}
