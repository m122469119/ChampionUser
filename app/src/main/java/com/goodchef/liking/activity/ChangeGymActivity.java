package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ChangeGymCityAdapter;
import com.goodchef.liking.fragment.ChangeGymFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.List;

/**
 * 说明: 切换场馆
 * Author shaozucheng
 * Time:16/9/14 下午3:24
 */
public class ChangeGymActivity extends BaseActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private PullToRefreshRecyclerView mDrawerRecyclerView;

    private View mCityHeadView;
    private TextView mRightTitleTextView;
    private ImageView mRightIconArrow;
    private TextView mTitleTextView;


    private String selectCityName;//选择的城市名称
    private String selectCityId;//选择的城市id
    private boolean isLoaction;//是否定位
    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡


    private ChangeGymCityAdapter mChangeGymCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_gym);
        initView();
        initData();
        setViewOnClickListener();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.right_drawer);
        mRightTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_right_title);
        mRightIconArrow = (ImageView) findViewById(R.id.change_gym_toolbar_right_icon);
        mTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_title);

        mDrawerLayout.setDrawerShadow(R.drawable.lesson_title_down_arrow, GravityCompat.END);
        mDrawerRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private void setViewOnClickListener() {
        mRightTitleTextView.setOnClickListener(this);
        mRightIconArrow.setOnClickListener(this);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                LogUtils.i(TAG, "drawleyer open");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                LogUtils.i(TAG, "drawleyer close");
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


    }

    private void initCityHeadView() {
        mCityHeadView = LayoutInflater.from(this).inflate(R.layout.item_city_head_view, mDrawerRecyclerView, false);
        TextView headText = (TextView) mCityHeadView.findViewById(R.id.city_head_test);
        headText.setText("当前城市：" + "上海市");
    }


    private void initData() {
        //  selectCityName = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY);
        selectCityId = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getIntent().getBooleanExtra(LikingHomeActivity.KEY_START_LOCATION, false);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getIntent().getIntExtra(LikingHomeActivity.KEY_TAB_INDEX, 0);
        mTitleTextView.setText("切换场馆");
        mRightTitleTextView.setText("上海市");
        setGymFragment();
        setCityListData();
    }


    private void setGymFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(LikingHomeActivity.KEY_SELECT_CITY_ID, selectCityId);
        bundle.putInt(LikingHomeActivity.KEY_TAB_INDEX, tabIndex);
        bundle.putString(LikingLessonFragment.KEY_GYM_ID, gymId);
        fragmentTransaction.add(R.id.gym_content_frame, ChangeGymFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }


    private void setCityListData() {
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                List<CityData> cityDataList = baseConfigData.getCityList();
                CityData data0 = new CityData();
                data0.setCityName("当前城市：" + "上海市");
                data0.setCityId(3100);
                cityDataList.add(0, data0);

                CityData data1 = new CityData();
                data1.setCityName("北京市");
                data1.setCityId(111);
                cityDataList.add(data1);

                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData cityData : cityDataList) {

                    }
                    mChangeGymCityAdapter = new ChangeGymCityAdapter(this);
                    mChangeGymCityAdapter.setData(cityDataList);
                    mDrawerRecyclerView.setAdapter(mChangeGymCityAdapter);
                    setCityOnItemClickListener();
                }
            }
        }
    }


    private void setCityOnItemClickListener() {
        mChangeGymCityAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.city_name);
                if (textView != null) {
                    CityData cityData = (CityData) textView.getTag();
                    if (cityData != null) {
                        selectCityName = cityData.getCityName();
                        List<CityData> list = mChangeGymCityAdapter.getDataList();
                        if (list != null && list.size() > 0) {
                            for (CityData data : list) {
                                if (data.getCityName().equals(cityData.getCityName())) {
                                    data.setSelct(true);
                                } else {
                                    data.setSelct(false);
                                }
                            }
                        }
                        setDrawerLayout();
                        mRightTitleTextView.setText(selectCityName);
                        mChangeGymCityAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mRightTitleTextView || v == mRightIconArrow) {
            setDrawerLayout();
        }
    }

    private void setDrawerLayout() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }
}
