package com.goodchef.liking.module.gym.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.map.LocationListener;
import com.aaron.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.data.CityData;
import com.goodchef.liking.eventmessages.ChangeGymActivityMessage;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.module.gym.changecity.ChangeCityActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

/**
 * 说明: 切换场馆
 * Author shaozucheng
 * Time:16/9/14 下午3:24
 */
public class ChangeGymActivity extends BaseActivity implements View.OnClickListener {
    private TextView mRightTitleTextView;
    private ImageView mRightIconArrow;
    private TextView mTitleTextView;
    private ImageView mLeftIcon;

    private String currentCityName;//当前定位城市
    private String currentCityId;//当前定位的城市id

    private String selectCityName;//选择的城市名称
    private String cityId;//选择的城市id
    private boolean isLoaction;//是否定位
    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡

    private AmapGDLocation mAmapGDLocation;
    private List<CityData> cityDataList;//开通服务的城市列表
    private String longitude;
    private String latitude;
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
        mRightTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_right_title);
        mRightIconArrow = (ImageView) findViewById(R.id.change_gym_toolbar_right_icon);
        mTitleTextView = (TextView) findViewById(R.id.change_gym_toolbar_title);
        mLeftIcon = (ImageView) findViewById(R.id.change_gym_toolbar_left_icon);
    }

    private void setViewOnClickListener() {
        mRightTitleTextView.setOnClickListener(this);
        mRightIconArrow.setOnClickListener(this);
        mLeftIcon.setOnClickListener(this);
    }

    private void initData() {
        cityId = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getIntent().getBooleanExtra(LikingHomeActivity.KEY_WHETHER_LOCATION, false);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getIntent().getIntExtra(LikingHomeActivity.KEY_TAB_INDEX, 0);

        mRightTitleTextView.setText(R.string.change_city);
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
        BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                cityDataList = baseConfigData.getCityList();
                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData cityData : cityDataList) {
                        if (String.valueOf(cityData.getCityId()).equals(cityId)) {
                            mTitleTextView.setText(cityData.getCityName());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mRightTitleTextView || v == mRightIconArrow) {
            UMengCountUtil.UmengCount(this, UmengEventId.RIGHT_ICON_ARROW_BTN);
            jumpToChangeCityActivity();
        } else if (v == mLeftIcon) {
            finish();
        }
    }

    /**
     * 跳转到切换城市界面
     */
    private void jumpToChangeCityActivity() {
        Intent intent = new Intent(this, ChangeCityActivity.class);
        intent.putExtra(ChangeCityActivity.CITY_NAME, mTitleTextView.getText());
        startActivity(intent);
    }

    /***
     * 初始化定位
     */
    private void initTitleLocation() {
        mAmapGDLocation = new AmapGDLocation(this);
        mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
            @Override
            public void receive(AMapLocation object) {
                if (object != null && object.getErrorCode() == 0) {//定位成功
                    isLoaction = true;
                    currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                    currentCityId = object.getCityCode();
                    longitude = object.getLongitude() + "";
                    latitude = object.getLatitude() + "";

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

    /**
     * 处理当前定位城市是否在开通城市范围之内
     */
    private String doLocationCity() {
        String cityId = "";
        if (cityDataList != null && cityDataList.size() > 0) {
            for (CityData cityData : cityDataList) {
                if (currentCityName.equals(cityData.getCityName()) || currentCityName.contains(cityData.getCityName())) {
                    cityId = cityData.getCityId() + "";
                    return cityId;
                }
            }
            if (StringUtils.isEmpty(cityId)) {
                cityId = currentCityId;
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

    public void onEvent(ChangeGymActivityMessage message) {
        switch (message.what) {
            case ChangeGymActivityMessage.CHANGE_LEFT_CITY_TEXT:
                selectCityName = message.msg1;
                currentCityName = message.msg1;
                mTitleTextView.setText(selectCityName);
                postEvent(new RefreshChangeCityMessage(doLocationCity(), longitude, latitude));
                UMengCountUtil.UmengCount(ChangeGymActivity.this, UmengEventId.CHANGE_CITY, selectCityName);
                break;
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

}
