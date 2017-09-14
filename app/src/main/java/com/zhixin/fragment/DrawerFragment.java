package com.zhixin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zhixin.adapter.DrawerListAdapter;
import com.zhixin.bean.CityBean;
import com.zhixin.bean.Item;
import com.zhixin.bean.QQUserInfoBean;
import com.zhixin.myview.MyTextView;
import com.zhixin.myview.RoundImageView;
import com.zhixin.qqoauth.GetUserInfo;
import com.zhixin.qqoauth.OauthLogin;

import com.zhixin.qqoauth.GetUserInfo.UserInfoCallBack;
import com.zhixin.qqoauth.OauthLogin.ReturnJson;
import com.zhixin.weather.R;
import java.util.List;
/**
 * Created by v_wenlxiao on 2017/3/7.
 */
public class DrawerFragment extends Fragment {
    /**
     * Remenber the position of the selected item
     */
    private static final String STATE_SELECTED_POSITION = "selected_drawer_position";

    private FragmentCallBack mCallbacks;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private DrawerListAdapter drawerListAdapter;
    //当前选中页
    private int mCurrentSelectedPosition = 0;
    public OauthLogin mOauthLogin;
    private QQUserInfoBean mQQUserInfoBean;
    private RoundImageView mUserIcon;
    private MyTextView mNickName;
    private View headerView;

    public DrawerFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            // Select either the default item (0) or the last selected item.
            if (drawerListAdapter.mItems.size() > 0)
                selectItem(mCurrentSelectedPosition);
        }
    }

    ReturnJson mReturnJson = new ReturnJson() {
        @Override
        public void mSetJson(int state) {
            // TODO Auto-generated method stub
            if (state == 0) {
                GetUserInfo.getUserInfo(getActivity(), mUserInfoCallBack);
            } else {
                // 图片来源于
                String Url = "drawable://" + R.mipmap.oauth_qq_normal;
                mUserIcon.loadImage(Url);
                mNickName.setText("登录");
            }
        }
    };
    UserInfoCallBack mUserInfoCallBack = new UserInfoCallBack() {
        @Override
        public void returnInfoCallBack(String mUserInfo) {
            // TODO Auto-generated method stub
            Gson gson = new Gson();
            mQQUserInfoBean = gson.fromJson(mUserInfo, QQUserInfoBean.class);
            mUserIcon.loadImage(mQQUserInfoBean.getFigureurl_qq_2());
            mNickName.setText(mQQUserInfoBean.getNickname());
        }
    };

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        mFragmentContainerView = getActivity().findViewById(fragmentId);
    }

    public void setDrawerAdapter(List<CityBean> tmpCities) {
        drawerListAdapter.addAllCitys(tmpCities);
    }

    public Item returnItems(int position) {
        if (0 < position
                && drawerListAdapter.mItems.get(position - 1) instanceof Item) {
            return (Item) drawerListAdapter.mItems.get(position - 1);
        }
        return null;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null && position != 0) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            if (position == 0) {
                mOauthLogin.doLogin();
                // 图片来源于
                String Url = "drawable://" + R.mipmap.oauth_qq_pressed;
                mUserIcon.loadImage(Url);
                mNickName.setText("登录");
            } else {
                mCallbacks.drawerSelectPosition(position);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.drawer_layout, container, false);
        headerView = inflater.inflate(R.layout.drawerlist_header, null);
        mDrawerListView.addHeaderView(headerView);
        mUserIcon = (RoundImageView) headerView.findViewById(R.id.user_icon);
        mNickName = (MyTextView) headerView
                .findViewById(R.id.user_nickname);
        drawerListAdapter = new DrawerListAdapter(this.getActivity());
        mDrawerListView.setAdapter(drawerListAdapter);
        mDrawerListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // L.e("onItemClick msg:"+position);
                        selectItem(position);
                    }
                });
        return mDrawerListView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOauthLogin = new OauthLogin(getActivity(), mReturnJson);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement mCallbacks.");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
}
