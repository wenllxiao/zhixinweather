package com.zhixin.weather;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.way.ui.swipeback.SwipeBackActivity;
import com.way.ui.swipeback.SwipeBackLayout;
import com.zhixin.adapter.EditCityListAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.db.CityDao;
import com.zhixin.fragment.FragmentCallBack;
import com.zhixin.myview.EditCityListView;
import com.zhixin.utils.AsyncTaskWeather;
import com.zhixin.utils.L;
import com.zhixin.utils.NetUtils;
import com.zhixin.utils.T;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by v_wenlxiao on 2017/3/8.
 */

public class EditCityActivity extends SwipeBackActivity{
    private String TAG = "EditCityActivity";
    private SwipeBackLayout mSwipeBackLayout;
    private EditCityListAdapter editCityListAdapter;
    private List<CityBean> mTmpCities;
    private CityDao cityDao;
    private EditCityListView listView;
    private ImageView mHomeBackButton;
    private ImageView mAddCityButton;
    private ImageView progressImg;
    private ProgressBar progressBar;
    private int currentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_city_list);
        initView();
        initData();
    }
    private void initView() {
        mHomeBackButton = (ImageView) findViewById(R.id.home_bar_Button);
        mHomeBackButton.setImageResource(R.drawable.navagation_back_selector);
        mAddCityButton = (ImageView) findViewById(R.id.add_city_Button);
        mAddCityButton.setOnClickListener(l);
        progressImg = (ImageView) findViewById(R.id.progressImg);
        progressImg.setOnClickListener(l);
        listView = (EditCityListView) findViewById(R.id.edit_city_listid);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.addSwipeListener(slistener);
        mHomeBackButton.setOnClickListener(l);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        editCityListAdapter = new EditCityListAdapter(this);
        listView.setAdapter(editCityListAdapter);
        listView.setOnItemClickListener(listener);
        listView.setOnDeleteListener(deletelistener);
    }

    SwipeBackLayout.SwipeListener slistener = new SwipeBackLayout.SwipeListener() {

        @Override
        public void onScrollStateChange(int state, float scrollPercent) {
            // TODO Auto-generated method stub
             Log.e("state:"+state,"scrollPercent:"+scrollPercent);
        }

        @Override
        public void onScrollOverThreshold() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onEdgeTouch(int edgeFlag) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("currentID", currentID);
            intent.putExtras(bundle);
            setResult(1002, intent);
        }
    };
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            currentID = position;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            L.e(TAG + currentID);
            bundle.putInt("currentID", currentID);
            intent.putExtras(bundle);
            setResult(1002, intent);
            finish();
            // L.e("position msg" + position);
        }

    };

    EditCityListView.OnDeleteListener deletelistener = new EditCityListView.OnDeleteListener() {
        @Override
        public void onDelete(int position, boolean deleteNot) {
            // TODO Auto-generated method stub
            if (deleteNot) {
                ViewGroup itemLayout = (ViewGroup) listView
                        .getChildAt(position);
                View v = itemLayout.getChildAt(itemLayout.getChildCount() - 1);
                if (cityDao.IsLocationCity(mTmpCities.get(position).getCity())) {
                    T.showShort(EditCityActivity.this, "不可删除本地城市");
                    v.setVisibility(View.VISIBLE);
                    return;
                }
                cityDao.deleteCity(mTmpCities.get(position).getCity());
                WeatherMainActivity.mWeatherMap.remove(mTmpCities.get(position)
                        .getCity());
                currentID = currentID - 1;
                listView.setItemChecked(currentID, true);
                v.setVisibility(View.VISIBLE);
                mTmpCities = cityDao.getTmpCities();
                editCityListAdapter.AddAllCitys(mTmpCities);
            } else {
                Intent intentx = new Intent();
                Bundle bundle = new Bundle();
                currentID = position;
                bundle.putInt("currentID", currentID);
                intentx.putExtras(bundle);
                setResult(1002, intentx);
                finish();
            }
        }
    };
    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.home_bar_Button:
                    Intent intentx = new Intent();
                    Bundle bundle = new Bundle();
                    L.e(TAG + currentID);
                    bundle.putInt("currentID", currentID);
                    intentx.putExtras(bundle);
                    setResult(1002, intentx);
                    finish();
                    break;
                case R.id.add_city_Button:
                    Intent intent = new Intent();
                    intent.setClass(EditCityActivity.this, AddCityActivity.class);
                    startActivityForResult(intent, 10001);
                    break;
                case R.id.progressImg:
                    if (NetUtils.isConnected(EditCityActivity.this)) {
                        new AsyncTaskWeather(EditCityActivity.this)
                                .execute(mTmpCities);
                        progressImg.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        T.showShort(EditCityActivity.this, "请检查网络!");
                    }
                    break;
            }
        }
    };
    private void initData() {
        mHomeBackButton.setImageResource(R.drawable.navagation_back_selector);
        cityDao = new CityDao(this);
        mTmpCities = new ArrayList<CityBean>();
        Bundle bundle = getIntent().getExtras();
        mTmpCities = bundle.getParcelableArrayList("mTmpCities");
        currentID = bundle.getInt("currentID");
        editCityListAdapter.AddAllCitys(mTmpCities);
        listView.setItemChecked(currentID, true);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.pull_down_in, R.anim.push_right_out);
    }

    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            Bundle bundle = new Bundle();
            bundle = data.getExtras();
            String cityName = bundle.getString("city");
            new MyTaskWeather().execute(cityName);
        }
    }

    public class MyTaskWeather extends AsyncTask<String, Void, Void> {
        public MyTaskWeather() {
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            mTmpCities = cityDao.getTmpCities();
            for (int i = 0; i < mTmpCities.size(); i++) {
                if (mTmpCities.get(i).getCity().equals(params[0])) {
                    currentID = i;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            editCityListAdapter.AddAllCitys(mTmpCities);
            listView.setItemChecked(currentID, true);
        }

    }
}
