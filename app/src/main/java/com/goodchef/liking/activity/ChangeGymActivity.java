package com.goodchef.liking.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ChangeGymCityAdapter;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.fragment.ChangeGymFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

/**
 * 说明: 切换场馆
 * Author shaozucheng
 * Time:16/9/14 下午3:24
 */
public class ChangeGymActivity extends SwipeBackActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mListView;

    private View mCityHeadView;
    private View mCityFootView;
    private TextView mRightTitleTextView;
    private ImageView mRightIconArrow;
    private TextView mTitleTextView;
    private ImageView mLeftIcon;
    private TextView mCityHeadText;
    private RelativeLayout mCurrentCityLayout;
    private RelativeLayout mLayoutCityFootView;


    private String currentCityName;//当前定位城市
    private String currentCityId;//当前定位的城市id

    private String selectCityName;//选择的城市名称
    private String cityId;//选择的城市id
    private boolean isLoaction;//是否定位
    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡

    private ChangeGymCityAdapter mChangeGymCityAdapter;

    private AmapGDLocation mAmapGDLocation;
    private List<CityData> cityDataList;//开通服务的城市列表
    private double longitude;
    private double latitude;
    private boolean isSecondLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_gym);
        initView();
        initData();
        setViewOnClickListener();
        initTitleLocation();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.right_drawer);
        mRightTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_right_title);
        mRightIconArrow = (ImageView) findViewById(R.id.change_gym_toolbar_right_icon);
        mTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_title);
        mLeftIcon = (ImageView) findViewById(R.id.change_gym_toolbar_left_icon);
        initCityHeadView();
        initCityFootView();
    }

    private void setViewOnClickListener() {
        mRightTitleTextView.setOnClickListener(this);
        mRightIconArrow.setOnClickListener(this);
        mLeftIcon.setOnClickListener(this);
    }

    private void initCityHeadView() {
        mCityHeadView = LayoutInflater.from(this).inflate(R.layout.item_city_head_view, mListView, false);
        mCityHeadText = (TextView) mCityHeadView.findViewById(R.id.city_head_test);
        mCurrentCityLayout = (RelativeLayout) mCityHeadView.findViewById(R.id.layout_current_city);
        mCurrentCityLayout.setOnClickListener(this);
    }

    private void initCityFootView() {
        mCityFootView = LayoutInflater.from(this).inflate(R.layout.item_city_foot_view, mListView, false);
        mLayoutCityFootView = (RelativeLayout) mCityFootView.findViewById(R.id.layout_city_foot_view);
    }


    private void initData() {
        cityId = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getIntent().getBooleanExtra(LikingHomeActivity.KEY_WHETHER_LOCATION, false);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getIntent().getIntExtra(LikingHomeActivity.KEY_TAB_INDEX, 0);

        mTitleTextView.setText("切换场馆");
        setGymFragment();
        setCityListData();
    }


    private void setGymFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(LikingHomeActivity.KEY_SELECT_CITY_ID, cityId);
        bundle.putInt(LikingHomeActivity.KEY_TAB_INDEX, tabIndex);
        bundle.putString(LikingLessonFragment.KEY_GYM_ID, gymId);
        bundle.putBoolean(LikingHomeActivity.KEY_WHETHER_LOCATION, isLoaction);
        fragmentTransaction.add(R.id.gym_content_frame, ChangeGymFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }


    private void setCityListData() {
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                cityDataList = baseConfigData.getCityList();
                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData cityData : cityDataList) {
                        if (String.valueOf(cityData.getCityId()).equals(cityId)) {
                            mRightTitleTextView.setText(cityData.getCityName());
                            cityData.setSelct(true);
                            selectCityName = cityData.getCityName();
                        }
                    }
                    mChangeGymCityAdapter = new ChangeGymCityAdapter(this);
                    mChangeGymCityAdapter.setData(cityDataList);
                    mListView.setAdapter(mChangeGymCityAdapter);
                    setCityOnItemClickListener();
                    setCityFootView();
                }
            }
        }
    }


    private void setCityOnItemClickListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_name);
                if (textView != null) {
                    CityData cityData = (CityData) textView.getTag();
                    if (cityData != null) {
                        selectCityName = cityData.getCityName();
                        compareSelectCity(selectCityName);
                        compareCurrentCity(selectCityName);
                        setDrawerLayout();
                        mRightTitleTextView.setText(selectCityName);
                        postEvent(new RefreshChangeCityMessage(String.valueOf(cityData.getCityId()), longitude, latitude));
                        UMengCountUtil.UmengCount(ChangeGymActivity.this, UmengEventId.CHANGE_CITY, selectCityName);
                    }
                }
            }
        });
    }

    /**
     * 比较选择的城市列表中的城市名称是否相等
     *
     * @param cityName
     */
    private void compareSelectCity(String cityName) {
        if (mChangeGymCityAdapter != null) {
            List<CityData> list = mChangeGymCityAdapter.getDataList();
            if (list != null && list.size() > 0) {
                for (CityData data : list) {
                    if (data.getCityName().equals(cityName)) {
                        data.setSelct(true);
                    } else {
                        data.setSelct(false);
                    }
                }
            }
            mChangeGymCityAdapter.notifyDataSetChanged();
        }
    }


    private void compareCurrentCity(String cityName) {
        if (!StringUtils.isEmpty(currentCityName)) {
            if (cityName.equals(currentCityName)) {
                mCityHeadText.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            } else {
                mCityHeadText.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mRightTitleTextView || v == mRightIconArrow) {
            UMengCountUtil.UmengCount(this, UmengEventId.RIGHT_ICON_ARROW_BTN);
            setDrawerLayout();
        } else if (v == mLeftIcon) {
            finish();
        } else if (v == mCurrentCityLayout) {
            if (isLoaction) {
                setDrawerLayout();
                mRightTitleTextView.setText(currentCityName);
                if (!StringUtils.isEmpty(doLocationCity())) {//如果当前城市在开通范围城市范围之内
                    compareSelectCity(currentCityName);
                }
                compareCurrentCity(currentCityName);
                postEvent(new RefreshChangeCityMessage(doLocationCity(), longitude, latitude));
            } else {
                isSecondLocation = true;
                initTitleLocation();
            }
        }
    }

    private void setDrawerLayout() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }


    /***
     * 初始化定位
     */
    private void initTitleLocation() {
        mAmapGDLocation = new AmapGDLocation(this);
        mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
            @Override
            public void receive(AMapLocation object) {
                LiKingVerifyUtils.initApi(ChangeGymActivity.this);
                if (object != null && object.getErrorCode() == 0) {//定位成功
                    isLoaction = true;
                    currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                    currentCityId = object.getCityCode();
                    longitude = object.getLongitude();
                    latitude = object.getLatitude();

                    //虚拟定位
//                    currentCityName = "徐州市";
//                    currentCityId = "123456";
//                    longitude = 117.20;
//                    latitude = 34.26;

                    if (isSecondLocation) {
                        postEvent(new RefreshChangeCityMessage(doLocationCity(), longitude, latitude));
                    }
                } else {//定位失败
                    isLoaction = false;
                }
                setCityHeadView();
            }

            @Override
            public void start() {
                isLoaction = false;
            }

            @Override
            public void end() {
                LogUtils.i("dust", "定位结束...");
            }
        });
        mAmapGDLocation.start();
    }

    private void setCityHeadView() {
        if (mChangeGymCityAdapter != null) {
            if (mCityHeadView != null) {
                mListView.removeHeaderView(mCityHeadView);
                if (isLoaction) {
                    mCityHeadText.setText("当前城市：" + currentCityName);
                } else {
                    mCityHeadText.setText("定位失败·重新定位");
                }
                mListView.addHeaderView(mCityHeadView);
            }
        }
        if (!StringUtils.isEmpty(selectCityName)) {
            compareCurrentCity(selectCityName);
        }
    }

    private void setCityFootView() {
        if (mChangeGymCityAdapter != null) {
            if (mCityFootView != null) {
                setFootViewHeight();
                mListView.addFooterView(mCityFootView);
            }
        }
    }


    private void setFootViewHeight() {
        int heightPixels = DisplayUtils.getHeightPixels();
        int ActionBarHeight = DisplayUtils.getActionBarSize(this);
        int cityListHeight = 0;
        int footViewContentHeight = DisplayUtils.dp2px(100);
        if (cityDataList != null && cityDataList.size() > 0) {
            cityListHeight = (cityDataList.size() + 1) * DisplayUtils.dp2px(40);
        }
        int blankHeight;

        WindowManager wmManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(this);
            blankHeight = heightPixels - ActionBarHeight - cityListHeight - navigationBarHeight - DisplayUtils.dp2px(30);
        } else {
            blankHeight = heightPixels - ActionBarHeight - cityListHeight - DisplayUtils.dp2px(30);
        }

        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) mLayoutCityFootView.getLayoutParams();
        if (blankHeight > 0 && blankHeight > footViewContentHeight) {
            layoutParams.height = blankHeight;
        } else {
            layoutParams.height = layoutParams.WRAP_CONTENT;
        }
        mLayoutCityFootView.setLayoutParams(layoutParams);

    }


    /**
     * 处理当前定位城市是否在开通城市范围之内
     */
    private String doLocationCity() {
        String cityId = "";
        if (cityDataList != null && cityDataList.size() > 0) {
            for (CityData cityData : cityDataList) {
                if (cityData.getCityName().equals(currentCityName) || cityData.getCityName().contains(currentCityName)) {
                    cityId = cityData.getCityId() + "";
                    return cityId;
                } else {
                    cityId = currentCityId;
                    return cityId;
                }
            }
        }
        return cityId;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAmapGDLocation != null) {
            mAmapGDLocation.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAmapGDLocation != null) {
            mAmapGDLocation.destroy();
        }
    }

}
