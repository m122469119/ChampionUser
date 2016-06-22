package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseActivity;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.fragment.LikingNearbyFragment;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

public class LikingHomeActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG_MAIN_TAB = "lesson";
    public static final String TAG_NEARBY_TAB = "nearby";
    public static final String TAG_RECHARGE_TAB = "recharge";
    public static final String TAG_MY_TAB = "my";

    private TextView mLikingLeftTitleTextView;
    private TextView mLikingMiddleTitleTextTextView;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private TextView mLikingRightTitleTextView;
    private AppBarLayout mAppBarLayout;
    private ImageView mMiddleImageView;

    private FragmentTabHost fragmentTabHost;
    private AmapGDLocation mAmapGDLocation;

    private double mLongitude;
    private double mLatitude;
    private String mCityId;
    private String mDistrictId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_home);
        setTitle(R.string.activity_liking_home);
        initViews();
        LiKingVerifyUtils.initApi(LikingHomeActivity.this);
        setViewOnClickListener();
        initTitleLocation();
    }

    private void initViews() {
        mLikingLeftTitleTextView = (TextView) findViewById(R.id.liking_left_title_text);
        mLikingMiddleTitleTextTextView = (TextView) findViewById(R.id.liking_middle_title_text);
        mLeftImageView = (ImageView) findViewById(R.id.title_down_arrow);
        mRightImageView = (ImageView) findViewById(R.id.liking_right_imageView);
        mMiddleImageView = (ImageView) findViewById(R.id.liking_middle_title_image);
        mLikingRightTitleTextView = (TextView) findViewById(R.id.liking_right_title_text);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.liking_home_appBar);
        initTabHost();
    }

    private void setViewOnClickListener() {
        mLikingLeftTitleTextView.setOnClickListener(this);
        mLikingRightTitleTextView.setOnClickListener(this);
        mLeftImageView.setOnClickListener(this);
        mRightImageView.setOnClickListener(this);
    }

    private void initTabHost() {
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabContent_liking_home);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MAIN_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_lesson), R.drawable.xml_tab_liking_home_lesson))
                , LikingLessonFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_NEARBY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_nearby), R.drawable.xml_tab_liking_home_nearby))
                , LikingNearbyFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_RECHARGE_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_recharge), R.drawable.xml_tab_liking_home_recharge))//setIndicator 设置标签样式
                , LikingBuyCardFragment.class, null); //setContent 点击标签后触发
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_my), R.drawable.xml_tab_liking_home_me))//setIndicator 设置标签样式
                , LikingMyFragment.class, null); //setContent 点击标签后触发
        TabWidget tabWidget = fragmentTabHost.getTabWidget();
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabWidget.setBackgroundResource(R.color.main_app_color);
        tabWidget.setPadding(0, DisplayUtils.dp2px(8), 0, DisplayUtils.dp2px(8));
        setHomeTabHost();

        mLeftImageView.setVisibility(View.VISIBLE);
        mLikingLeftTitleTextView.setText("上海");
    }

    private View buildTabIndicatorCustomView(String tabTitle, int drawableResId) {
        View tabView = getLayoutInflater().inflate(R.layout.layout_liking_home_tab_custom_view, null, false);
        ((ImageView) tabView.findViewById(R.id.imageview_chef_stove_tab)).setImageResource(drawableResId);
        ((TextView) tabView.findViewById(R.id.textview_chef_stove_tab)).setText(tabTitle);
        return tabView;
    }


    private void setHomeTabHost() {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAG_MAIN_TAB)) {
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
                    mLikingMiddleTitleTextTextView.setVisibility(View.GONE);
                    mMiddleImageView.setVisibility(View.VISIBLE);
                    mLeftImageView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingLeftTitleTextView.setText("上海");
                    mRightImageView.setVisibility(View.GONE);
                } else if (tabId.equals(TAG_NEARBY_TAB)) {
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLeftImageView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextTextView.setVisibility(View.VISIBLE);
                    mMiddleImageView.setVisibility(View.GONE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextTextView.setText(R.string.tab_liking_home_nearby);
                    mRightImageView.setVisibility(View.VISIBLE);
                    mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_shopping_cart));
                } else if (tabId.equals(TAG_RECHARGE_TAB)) {
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLeftImageView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextTextView.setVisibility(View.VISIBLE);
                    mMiddleImageView.setVisibility(View.GONE);
                    mLikingMiddleTitleTextTextView.setText(R.string.tab_liking_home_recharge);
                    mLikingRightTitleTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setText("查看场馆");
                    mRightImageView.setVisibility(View.GONE);
                } else if (tabId.equals(TAG_MY_TAB)) {
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLeftImageView.setVisibility(View.INVISIBLE);
                    mMiddleImageView.setVisibility(View.GONE);
                    mLikingMiddleTitleTextTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextTextView.setText(R.string.tab_liking_home_my);
                    mRightImageView.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        String tag = fragmentTabHost.getCurrentTabTag();
        if (v == mLikingLeftTitleTextView || v == mLeftImageView) {
            if (tag.equals(TAG_MAIN_TAB)) {
                Intent intent = new Intent(this, LookStoreMapActivity.class);
                startActivity(intent);
            }
        } else if (v == mLikingRightTitleTextView) {
            if (tag.equals(TAG_RECHARGE_TAB)) {
                Intent intent = new Intent(this, LookStoreMapActivity.class);
                startActivity(intent);
            }
        }else if (v == mRightImageView){
            if (tag.equals(TAG_NEARBY_TAB)){
                Intent intent = new Intent(this,ShoppingCartActivity.class);
                startActivity(intent);
            }
        }
    }


    private void initTitleLocation() {
        mAmapGDLocation = new AmapGDLocation(this);
        mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
            @Override
            public void receive(AMapLocation object) {
                if (object == null || object.getErrorCode() != 0) {
                    LiKingVerifyUtils.initApi(LikingHomeActivity.this);
                    return;
                }
                LogUtils.d(TAG, "city: " + object.getCity() + " city code: " + object.getCityCode());
                LogUtils.d(TAG, "longitude:" + object.getLongitude() + "Latitude" + object.getLatitude());
                //  mTitleTextView.setText(StringUtils.isEmpty(locationAddress) ? getString(R.string.location_error) : object.getPoiName());
                //  updateLocationPoint(CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), object.getLongitude(), object.getLatitude());
                updateLocationPoint(object.getProvince(), object.getDistrict(), object.getLongitude(), object.getLatitude());
                LiKingVerifyUtils.initApi(LikingHomeActivity.this);
                mLeftImageView.setVisibility(View.VISIBLE);
                mLikingLeftTitleTextView.setText(object.getCity());
            }

            @Override
            public void start() {
            }

            @Override
            public void end() {
                LogUtils.i(TAG, "定位结束...");
            }
        });
        mAmapGDLocation.start();
    }

    private void updateLocationPoint(String cityId, String districtId, double longitude, double latitude) {
        mLongitude = longitude;
        mLatitude = latitude;
        mCityId = cityId;
        mDistrictId = districtId;
        postEvent(new MainAddressChanged(longitude, latitude, cityId, districtId));
        saveLocationInfo(cityId, districtId, longitude, latitude);
    }

    private void saveLocationInfo(String cityId, String districtId, double longitude, double latitude) {
        LocationData locationData = new LocationData(cityId, districtId, longitude, latitude);
        Preference.setLocationData(locationData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAmapGDLocation.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAmapGDLocation.destroy();
    }
}
