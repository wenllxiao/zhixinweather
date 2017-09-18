package com.zhixin.weather;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.zhixin.bean.RequestDataBean;
import com.zhixin.bean.ResponseWeather;
import com.zhixin.utils.HttpRequestInfo;
import com.zhixin.utils.NetUtils;
import com.zhixin.utils.T;
import com.zhixin.utils.WeatherInfoUtil;
import com.way.ui.swipeback.SwipeBackActivity;
import com.zhixin.adapter.AddCityAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.db.CityDao;
import com.zhixin.myview.MyTextView;
public class AddCityActivity extends SwipeBackActivity {
    private CityDao cityDao;
    private List<CityBean> hotCitys;
    private Button mSearchButton;
    private AddCityAdapter mAddCityAdapter;
    private MyTextView mCurrentCity;
    private RecyclerView recyclerView;
    private MyTextView selectText;
    private EditText mSearchCity;
    private ImageView mbackbarButton;
    private String citName[];
    private boolean isSerach = false;
    private String selectCityName;
    private ItemTouchHelper itemTouchHelper;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_grid);
        initView();
        initData();
    }

    private void initData() {
        cityDao = new CityDao(this);
        hotCitys = new ArrayList<CityBean>();
        CityBean cityBean = cityDao.getLocationCityFromDB();
        if (cityBean != null) {
            String currentCity = cityBean.getCity();
            mCurrentCity.setText("当前位置:" + currentCity);
        }
    }

    private void initView() {
        mCurrentCity = (MyTextView) findViewById(R.id.current_city);
        mSearchButton = (Button) findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(listener);
        recyclerView = (RecyclerView) findViewById(R.id.grid_recycler);
        mSearchCity = (EditText) findViewById(R.id.search_city);
        mAddCityAdapter = new AddCityAdapter(AddCityActivity.this);
        mLayoutManager=new GridLayoutManager(AddCityActivity.this,3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAddCityAdapter);
        mbackbarButton = (ImageView) findViewById(R.id.back_bar_Button);
        mbackbarButton.setOnClickListener(listener);
        setRecyclerListener();
    }
    private void setRecyclerListener() {
            itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags, 0);
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mAddCityAdapter.setOnItemClickListener(new AddCityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                isSerach = false;
                selectText = (MyTextView) view;
                selectCityName = (String) selectText.getText();
                selectCityName.trim();
                CityBean cityBean = cityDao.InTmpCities(selectCityName);
                if (cityBean == null) {
                    cityDao.UpdateTmpCity(selectCityName);
                    WeatherMainActivity.mWeatherMap.put(selectCityName,null);
                    new MyTaskWeather(AddCityActivity.this).execute(selectCityName);
                } else {
                    T.showShort(AddCityActivity.this, "已经添加过不可重复添加");
                }
            }
            @Override
            public void onItemLongClick(View view) {
                itemTouchHelper.startDrag(recyclerView.getChildViewHolder(view));
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.back_bar_Button:
                    finish();
                    break;
                case R.id.search_btn:
                    isSerach = true;
                    citName = (mSearchCity.getText().toString()).split("市");
                    citName = citName[0].split("县");
                    if (NetUtils.isConnected(AddCityActivity.this)) {
                        new MyTaskWeather(AddCityActivity.this).execute(citName[0].trim());
                    } else {
                        T.showShort(AddCityActivity.this, "请检查网络");
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }

    public static class MyTaskWeather extends AsyncTask<String, Void, String> {
        private WeakReference<Context> weakReference;

        public MyTaskWeather(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            RequestDataBean.setLocation(params[0]);
            HttpRequestInfo entity = new HttpRequestInfo().fun(RequestDataBean
                    .getData());
            if (entity.getMessage().arg1 == 1) {
                try {
                    //	WeatherInfoUtil weatherInfo = new WeatherInfoUtil();
                    String respones = entity.getResponse();
                    WeatherInfoUtil.JsonParser(respones);
                    ResponseWeather rw = new ResponseWeather();
                    rw = WeatherInfoUtil.getJresponseWeather();
                    // L.e("rw.getError" + rw.getError());
                    if (rw.getError() != null && rw.getError().equals("0")) {
                        WeatherMainActivity.mWeatherMap.put(params[0], respones);
                        return "0";
                    } else {
                        return "999";
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
            AddCityActivity activity = (AddCityActivity) weakReference.get();
            if (activity.isSerach == true) {
                if (result.equals("0")) {
                    // 是否包涵在临时城市里
                    CityBean tmpcityBean = activity.cityDao.InTmpCities(activity.citName[0]);
                    // 是否包涵在热门城市里
                    boolean hotcityBean = activity.cityDao.InHotCities(activity.citName[0]);
                    if (tmpcityBean == null) {
                        if (hotcityBean == false) {
                            activity.cityDao.DeleteAndAddCity(activity.citName[0]);
                            new MyTask(activity).execute();
                        } else {
                            activity.cityDao.UpdateTmpCity(activity.citName[0]);
                            new MyTask(activity).execute();
                        }
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("city", activity.citName[0]);
                        intent.putExtras(bundle);
                        activity.setResult(10001, intent);
                        activity.finish();
                    } else {
                        T.showShort(activity, "该城市已经添加过不可重复添加哦!");
                    }
                } else {
                    T.showLong(activity, "输入有误或着无法获取当前城市信息！");
                }
            } else {
                new MyTask(activity).execute();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("city", activity.selectCityName);
                intent.putExtras(bundle);
                activity.setResult(10001, intent);
                T.showShort(activity, "添加成功");
                activity.finish();
            }
        }

    }

    public static class MyTask extends AsyncTask<Void, Void, List<CityBean>> {
        private WeakReference<Context> weakReference;

        public MyTask(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected List<CityBean> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            AddCityActivity activity = (AddCityActivity) weakReference.get();
            activity.hotCitys = activity.cityDao.getHotCities();
            return activity.hotCitys;
        }

        @Override
        protected void onPostExecute(List<CityBean> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            AddCityActivity activity = (AddCityActivity) weakReference.get();
            activity.mAddCityAdapter.SetHotCitys(result);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new MyTask(AddCityActivity.this).execute();
    }

}
