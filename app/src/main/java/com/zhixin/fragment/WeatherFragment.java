package com.zhixin.fragment;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zhixin.adapter.WeatherListAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.bean.RequestDataBean;
import com.zhixin.bean.ResponseWeather;
import com.zhixin.bean.WeatherDataBean;
import com.zhixin.bean.WeatherInfo;
import com.zhixin.myview.MyTextView;
import com.zhixin.utils.HttpRequestInfo;
import com.zhixin.utils.NetUtils;
import com.zhixin.utils.ScreenUtils;
import com.zhixin.utils.T;
import com.zhixin.utils.TimeUtil;
import com.zhixin.utils.WeatherInfoUtil;
import com.zhixin.weather.R;
import static com.zhixin.weather.WeatherMainActivity.mWeatherMap;

public class WeatherFragment extends Fragment implements OnRefreshListener,
        OnPullEventListener {
    private String city;
    private ListView mWeatherList;
    private View mListHeaderView;
    private MyTextView tempText;
    private MyTextView tempDuring;
    private MyTextView updateTime;
    private MyTextView forecast;
    private MyTextView pm;
    private WeatherListAdapter weatherListAdapter;
    private List<WeatherDataBean> jweatherDataBean;
    private List<WeatherInfo> mResults;
    private PullToRefreshScrollView mPullRefreshScroll;
    private MyTaskWeather mRefreshWeather;
    private Activity activity;
    public WeatherFragment() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public WeatherFragment(CityBean city) {
        this.city = city.getCity();
        jweatherDataBean = new ArrayList<WeatherDataBean>();
        mResults = new ArrayList<WeatherInfo>();
    }
    @Override
    @Deprecated
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        // L.e(TAG, "msg: onAttach" + city);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initData();
        // L.e(TAG, "msg: onCreate" + city);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // L.e(TAG, "msg: onCreateView" + city);
        return inflater
                .inflate(R.layout.wether_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        // L.e(TAG, "msg: onViewCreated" + city);
    }

    /**
     * 初始化所有view
     *
     * @param view
     */
    @SuppressLint("InlinedApi")
    @SuppressWarnings("unchecked")
    private void initViews(View view) {
        mPullRefreshScroll = (PullToRefreshScrollView) view
                .findViewById(R.id.refresh_scrollview);
        initIndicator();
        // 添加下拉刷新事件
        mPullRefreshScroll.setOnRefreshListener(this);
        // 添加下拉刷新状态事件，以便及时刷新时间
        mPullRefreshScroll.setOnPullEventListener(this);
        mWeatherList = (ListView) view.findViewById(R.id.weather_list);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mListHeaderView = mLayoutInflater.inflate(R.layout.forecast_header,
                new LinearLayout(getActivity()), false);
        // 获取屏幕高度
        int screenHeight = ScreenUtils.getScreenHeight(getActivity());
        // HeaderView高度=屏幕高度-标题栏高度-状态栏高度
        int headerHeight = screenHeight - 10
                - ScreenUtils.getStatusHeight(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, headerHeight);
        mListHeaderView.setLayoutParams(params);
        mWeatherList.addHeaderView(mListHeaderView);
        mWeatherList.setOnScrollListener(mOnScrollListener);// ListView 滑动监听
        weatherListAdapter = new WeatherListAdapter(getActivity());
        mWeatherList.setAdapter(weatherListAdapter);
        initHeaderViews(view);
        setHeaderData();
    }

    private void initIndicator() {
        ILoadingLayout startLabels = mPullRefreshScroll.getLoadingLayoutProxy(
                true, false);
        startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新");// 刷新时
        startLabels.setReleaseLabel("放开刷新");// 下来达到一定距离时，显示的提示
    }

    OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            View mFirstChild = view.getChildAt(0);// 获取可见Item的第一个View
            if (mFirstChild == mListHeaderView && mFirstChild.getTop() == 0) {
                mPullRefreshScroll.setMode(Mode.PULL_FROM_START);// 设置为下拉刷新
            } else {
                if (mPullRefreshScroll.getState() == State.RESET) {
                    mPullRefreshScroll.setMode(Mode.DISABLED);
                } else if (mFirstChild == mListHeaderView
                        && (-mFirstChild.getTop()) > mListHeaderView
                        .getHeight() / 2) {
                    // 上滑距离超过HeaderView高度一半时，取消刷新,并停止异步任务
                    mPullRefreshScroll.onRefreshComplete();
                    if (mRefreshWeather != null)
                        mRefreshWeather.cancel(true);
                }
            }
        }

    };

    private void initData() {
        if (city != null &&mWeatherMap!=null&&mWeatherMap.size() != 0)
            WeatherInfoUtil.JsonParser(mWeatherMap.get(city));
        jweatherDataBean = WeatherInfoUtil.mWeatherDataBean;
        mResults = WeatherInfoUtil.mResults;
    }

    /**
     * 初始化当前天气的view
     *
     * @param view
     */
    private void initHeaderViews(View view) {
        tempText = (MyTextView) view.findViewById(R.id.temp_text);
        tempDuring = (MyTextView) view.findViewById(R.id.temp_during);
        updateTime = (MyTextView) view.findViewById(R.id.time_text);
        forecast = (MyTextView) view.findViewById(R.id.forecast);
        pm = (MyTextView) view.findViewById(R.id.pm);
        setHeaderData();
    }

    private void setHeaderData() {
        if (jweatherDataBean != null && jweatherDataBean.size() > 0) {
            String temp = jweatherDataBean.get(0).getDate();
            int start = temp.lastIndexOf("实时");
            int end = temp.lastIndexOf("℃");
            tempText.setText(temp.substring(start + 3, end));
            String tempDur[] = jweatherDataBean.get(0).getTemperature()
                    .split(" ~");
            tempDuring.setText(tempDur[1] + "/" + tempDur[0].trim() + "℃");
            updateTime.setText("[" + TimeUtil.getTime() + "更新]");
            forecast.setText(jweatherDataBean.get(0).getWeather());
            pm.setText("PM2.5:" + mResults.get(0).getPm25());
            weatherListAdapter.setResponseWeather(city,
                    mWeatherMap.get(city));
        }
    }

    public class MyTaskWeather extends AsyncTask<String, Void, String> {
        public MyTaskWeather() {
            mPullRefreshScroll.setRefreshing(true);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            RequestDataBean.setLocation(params[0]);
            HttpRequestInfo respones = new HttpRequestInfo().fun(RequestDataBean
                    .getData());
            if (respones.getMessage().arg1 == 1) {
                try {
                    //WeatherInfoUtil weatherInfo = new WeatherInfoUtil();
                    String weather = respones.getResponse();
                    WeatherInfoUtil.JsonParser(weather);
                    ResponseWeather rw = new ResponseWeather();
                    rw = WeatherInfoUtil.getJresponseWeather();
                    if (rw.getError()!=null&&rw.getError().equals("0")) {
                        mWeatherMap.put(city, weather);
                        return "0";
                    }else{
                        return "11110";
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return "999";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.equals("0")) {
                T.showShort(getActivity(), "刷新成功");
                initData();
                mPullRefreshScroll.onRefreshComplete();
                setHeaderData();
            }else{
                T.showShort(getActivity(), "刷新失败，暂无数据");
                mPullRefreshScroll.onRefreshComplete();
            }

        }

    }

    @Override
    public void onPullEvent(PullToRefreshBase refreshView, State state,
                            Mode direction) {
        // TODO Auto-generated method stub

    }

    @SuppressLint("NewApi")
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        // TODO Auto-generated method stub
        if (NetUtils.isConnected(getActivity())) {
            if (mRefreshWeather != null
                    && mRefreshWeather.getStatus() == Status.RUNNING) {
                return;
            }
            String label = DateUtils.formatDateTime(getActivity(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            // 显示最后更新的时间
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            initData();
            mRefreshWeather = new MyTaskWeather();
            mRefreshWeather.execute(city);
        } else {
            T.showShort(getActivity(), "刷新失败，请检查网络!");
            mPullRefreshScroll.onRefreshComplete();
        }
    }
}
