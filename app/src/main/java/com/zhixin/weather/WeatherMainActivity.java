package com.zhixin.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.utils.L;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.zhixin.ImageLoader.CutViewCapture;
import com.zhixin.adapter.WeatherPagerAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.bean.Item;
import com.zhixin.db.CityDao;
import com.zhixin.fragment.DrawerFragment;
import com.zhixin.fragment.FragmentCallBack;
import com.zhixin.location.BaiduLocation;
import com.zhixin.myview.MyTextView;
import com.zhixin.qqoauth.ShareToQQ;
import com.zhixin.utils.AsyncTaskWeather;
import com.zhixin.utils.NetUtils;
import com.zhixin.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v_wenlxiao on 2017/3/4.
 */
public class WeatherMainActivity extends Activity implements FragmentCallBack, View.OnClickListener {
    private String TAG = "WeatherMainActivity";
    private ImageView[] points;//顶部小点
    private LinearLayout mLayout;//添加小点的容器
    public static Map<String, String> mWeatherMap;
    private int currentID = 0;//记录当前页面
    /**
     * whether the user is first login the application
     */
    private DrawerFragment mDrawerFragment;
    private View leftDrawer;
    private CityDao cityDao;
    private CityBean cityBean;
    private View locationView;//加载页面视图
    private DrawerLayout mDrawerLayout;
    private ProgressBar mProgressBar;
    private Long mStartTime;//记录开始时间
    private Handler mHandler;
    private ImageView mHomeBarButton;//抽屉按钮
    private ImageView mAddCityButton;//添加城市按钮
    private ImageView mShareButton;//分享
    public ImageView mCurrentLocation;//当前城市的图标
    private MyTextView mLocationCity;//当前页面对应的城市名
    private static int SHOW_TIME = 3000;//过渡页面时间
    private ViewPager mViewPager;//存放每个城市天气信息页面
    private List<CityBean> mTmpCities;//当前已选择的城市
    private View shareView;//
    WeatherPagerAdapter mWeatherPagerAdapter;
    /**
     * 第三方SDK
     */
    private BaiduLocation baiduLocation;
    public static Tencent mTencent;
    public ShareToQQ mShareToQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initDatas() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1105796799", this);
        }
        mWeatherMap = new HashMap<String, String>();
        mTmpCities = new ArrayList<CityBean>();
        mStartTime = System.currentTimeMillis();// 记录开始时间，
        cityDao = new CityDao(this);
        mTmpCities = cityDao.getTmpCities();
        mHandler = new Handler();
        if (!NetUtils.isConnected(this)) {//检查网络是否正常
            T.showShort(this, "请检查网络！");
            mHandler.post(locationViewGone);
        } else {
            baiduLocation = new BaiduLocation(this);
            baiduLocation.startLocation();//开始定位
        }
    }

    private void initViews() {
        mDrawerFragment = (DrawerFragment) getFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        leftDrawer = findViewById(R.id.navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);
        shareView = findViewById(R.id.frame_content);
        mLayout = (LinearLayout) findViewById(R.id.indicator_point);
        // Set up the drawer.
        mDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        locationView = findViewById(R.id.locaiton_view);
        mHomeBarButton = (ImageView) findViewById(R.id.home_bar_Button);
        mHomeBarButton.setOnClickListener(this);
        mAddCityButton = (ImageView) findViewById(R.id.add_city_Button);
        mAddCityButton.setOnClickListener(this);
        mShareButton = (ImageView) findViewById(R.id.share_Button);
        mShareButton.setOnClickListener(this);
        mCurrentLocation = (ImageView) findViewById(R.id.curr_loc_icon);
        mLocationCity = (MyTextView) findViewById(R.id.location_city_textview);
        /*
         * 初始化ViewPager
		 */
        mViewPager = (ViewPager) findViewById(R.id.weather_viewpager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(mListener);
        mWeatherPagerAdapter = new WeatherPagerAdapter(getFragmentManager());
        // 给ViewPager设置适配器
        mViewPager.setAdapter(mWeatherPagerAdapter);
        if (mTmpCities.size() > 0) {
            updateUI();
        }
    }

    // 进入下一个页面
    Runnable locationViewGone = new Runnable() {
        @Override
        public void run() {
            Animation anim = AnimationUtils.loadAnimation(WeatherMainActivity.this,
                    R.anim.push_right_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    locationView.setVisibility(View.GONE);
                    // 如果是第一次进入应用，显示抽屉
                    // if (!mUserLearnedDrawer&&cityBean!=null) {
                    // drawerLayout.openDrawer(leftDrawer);
                    // }
                    if (cityBean != null) {
                        mDrawerLayout.openDrawer(leftDrawer);
                    }
                }
            });
            if (locationView.getVisibility() == View.VISIBLE)
                locationView.startAnimation(anim);
        }
    };

    private void invisibleLocationView() {
        long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
        if (loadingTime < SHOW_TIME) {// 如果比最小显示时间还短，就延时进入WeatherMainActivity，否则直接进入
            mHandler.postDelayed(locationViewGone, SHOW_TIME - loadingTime);
        } else {
            mHandler.post(locationViewGone);
        }
    }

    /**
     * 关联menu键
     */
    private void openleftlayout() {
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer))) {
            mDrawerLayout.closeDrawer(findViewById(R.id.navigation_drawer));
        } else {
            mDrawerLayout.openDrawer(findViewById(R.id.navigation_drawer));
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add_city_Button:
                Intent intent = new Intent();
                intent.setClass(WeatherMainActivity.this, AddCityActivity.class);
                startActivityForResult(intent, 10001);
                break;
            case R.id.home_bar_Button:
                openleftlayout();
                break;
            case R.id.share_Button:
                if (NetUtils.isConnected(getApplicationContext())) {
                    new ShareAsyncTask().execute();
                } else {
                    T.showLong(this, "请检查网络！");
                }
            default:
                break;
        }
    }

    @Override
    public void drawerSelectPosition(int position) {
        if (position > 0) {
            Item mItem = mDrawerFragment.returnItems(position);
            switch (mItem.mId) {
                case Item.EDIT_ID:
                    Intent intentEdit = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("mTmpCities",
                            (ArrayList<? extends Parcelable>) mTmpCities);
                    bundle.putInt("currentID", currentID);
                    intentEdit.putExtras(bundle);
                    intentEdit.setClass(WeatherMainActivity.this, EditCityActivity.class);
                    startActivityForResult(intentEdit, 10002);
                    break;
                case Item.FEEDBACK_ID:
                    Intent intentFeedBack = new Intent();
                    intentFeedBack.setClass(WeatherMainActivity.this,
                            FeedBackActivity.class);
                    startActivity(intentFeedBack);
                    break;
                case Item.SHARE_ID: {
                    if (NetUtils.isConnected(getApplicationContext())) {
                        mShareToQQ = new ShareToQQ(WeatherMainActivity.this);
                        mShareToQQ.shareToQQ(1);
                    } else {
                        T.showLong(this, "请检查网络！");
                    }
                }
                break;
                case Item.ABOUT_ID:
                    Intent intentAbout = new Intent();
                    intentAbout.setClass(WeatherMainActivity.this, AboutActivity.class);
                    startActivity(intentAbout);
                    break;
                default:
                    // L.e(TAG,"item.mId msg:"+item.mId);
                    mViewPager.setCurrentItem(mItem.mId);
            }
        }
    }

    @Override
    public void responseWeatherInfo(Map<String, String> weatherInfo) {
        mWeatherMap = weatherInfo;
        updateUI();
        mViewPager.setCurrentItem(currentID);
    }

    @Override
    public void update(CityBean cityBean) {
// TODO Auto-generated method stub
        this.cityBean = cityBean;
        cityDao.UpdateOrAddLodationCity(cityBean);
        cityDao.getCityBycityName(cityBean.getCity());
        mTmpCities = cityDao.getTmpCities();
        if (cityBean.getCity() != null)
            T.showShort(this, "当前所在位置:" + cityBean.getAddRess());
        new AsyncTaskWeather(this).execute(mTmpCities);
        //处理完成后给handler发送消息
        Message msg = new Message();
        msg.what = 1;
        mHandler2.sendMessage(msg);
    }

    Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            // 处理收到的消息
            mCurrentLocation.setVisibility(View.VISIBLE);
            mLocationCity.setText(cityBean.getCity());
        }
    };

    private void updateUI() {
        initIndicatorPoint();
        mDrawerFragment.setDrawerAdapter(mTmpCities);
        mWeatherPagerAdapter.addAllCitys(mTmpCities);
        if (mTmpCities.size() == 0) {
            mLocationCity.setText("_ _");
            return;
        }
        L.e(TAG, "mTmpCities.size:" + mTmpCities.size());
        mViewPager.setCurrentItem(currentID);
        CityBean city = mTmpCities.get(currentID);
        mLocationCity.setText(city.getCity());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_down_in, R.anim.push_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.pull_down_in, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        L.e(TAG, "onActivityResult msg");
        if (resultCode == 10001) {
            Bundle bundle = new Bundle();
            bundle = data.getExtras();
            String cityName = bundle.getString("city");http://openapi.sparta.html5.qq.com/v3/user/get_info?openid=CCA150B98D4ED2914C51E6689A60130D&openkey=1F0DE6F14B5E425915A8EC606C88150C&pf=qzone&appid=1104742329&format=json&userip=10.0.0.1&sig=ZylfvMcInOrJNOWM6aqtlp%2F%2FShA%3D
            mTmpCities.clear();
            mTmpCities = cityDao.getTmpCities();
            for (int i = 0; i < mTmpCities.size(); i++) {
                if (mTmpCities.get(i).getCity().equals(cityName)) {
                    currentID = i;
                }
            }
            updateUI();
        } else if (resultCode == 1002) {
            Bundle bundle = new Bundle();
            bundle = data.getExtras();
            currentID = bundle.getInt("currentID");
            mTmpCities.clear();
            mTmpCities = cityDao.getTmpCities();
            updateUI();
        } else if (requestCode == Constants.REQUEST_LOGIN
                || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                    mDrawerFragment.mOauthLogin.getLoginListener());
        } else if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                    mShareToQQ.qqShareListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            CityBean city = mTmpCities.get(arg0);
            if (city.getIsLocation() == 1) {
                mCurrentLocation.setVisibility(View.VISIBLE);
            } else {
                mCurrentLocation.setVisibility(View.GONE);
            }
            currentID = arg0;
            mLocationCity.setText(city.getCity());
            initIndicatorPoint();
        }
    };

    //设置顶部指示图标
    private void initIndicatorPoint() {
        points = new ImageView[mTmpCities.size()];
        mLayout.removeAllViews();
        for (int i = 0; i < mTmpCities.size(); i++) {
            points[i] = new ImageView(this);
            points[i].setPadding(5, 0, 5, 0);
            if (i == currentID) {
                points[i].setImageResource(R.mipmap.city_selected);
            } else {
                points[i].setImageResource(R.mipmap.city_normal);
            }
            mLayout.addView(points[i]);
        }

    }

    //异步调用 QQ分享
    private class ShareAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            CutViewCapture.saveBitmapInFile(shareView);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mShareToQQ = new ShareToQQ(WeatherMainActivity.this);
            mShareToQQ.shareToQQ(2);
        }

    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        invisibleLocationView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baiduLocation != null)
            baiduLocation.stopLocation();
    }
}
