package com.goodchef.liking.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.mvp.BaseMVPActivity;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.map.LocationListener;
import com.aaron.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.UnreadMessageResult;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.data.remote.retrofit.result.data.NoticeData;
import com.goodchef.liking.eventmessages.BuyCardMessage;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.LoginInvalidMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnClickLessonFragmentMessage;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.module.home.myfragment.LikingMyFragment;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.utils.CityUtils;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Set;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class LikingHomeActivity extends BaseMVPActivity<LikingHomeContract.Presenter> implements LikingHomeContract.View {

    public static final String TAG_MAIN_TAB = "lesson";
    public static final String TAG_NEARBY_TAB = "nearby";
    public static final String TAG_RECHARGE_TAB = "recharge";
    public static final String TAG_MY_TAB = "my";

    public static final String KEY_SELECT_CITY_ID = "key_select_city_id";
    public static final String KEY_TAB_INDEX = "key_tab_index";
    public static final String KEY_INTENT_TAB = "key_intent_tab";
    public static final String KEY_WHETHER_LOCATION = "key_whether_location";

    private FragmentTabHost fragmentTabHost;
    private TabWidget tabWidget;
    private AmapGDLocation mAmapGDLocation;//定位
    private String currentCityName = "";

    public int mCanSchedule = -1;//是否支持自助团体课
    public static boolean isWhetherLocation = false;
    public static int isStartLoaction = 0;
    public static String gymId;
    public static String gymName = "";
    public static String gymTel = "";
    public static int defaultGym;
    private long firstTime = 0;//第一点击返回键
    private CoursesResult.Courses.Gym mGym;//买卡界面传过来的带有城市id的Gym对象
    private CoursesResult.Courses.Gym mNoticeGym;//带有公告的Gym对象
    public static String cityCode;//高德地图定位的城市返回的code码
    public static boolean isChangeGym = false;
    public static boolean shoDefaultDialog = true;
    public static final String ACTION = "action";
    public static final int SHOW_PUSH_NOTICE = 0x00001111;
    public static final int SHOW_PUSH_NOTICE_RECEIVED = 0x00001112;
    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_home);
        ButterKnife.bind(this);
        setTitle(R.string.activity_liking_home);
        mPresenter.initHomeGymId();
        initTabHost();
        sendUpdateAppRequest();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        pause = false;
        int intExtra = intent.getIntExtra(ACTION, 0);
        if (SHOW_PUSH_NOTICE == intExtra) {
            fragmentTabHost.setCurrentTab(0);
            mPresenter.showPushDialog();
        } else if (SHOW_PUSH_NOTICE_RECEIVED == intExtra) {
            if (fragmentTabHost.getCurrentTabTag().equals(TAG_MAIN_TAB)) {
                mPresenter.showPushDialog();
            }
        } else {
            int tag = intent.getIntExtra(KEY_INTENT_TAB, 0);
            fragmentTabHost.setCurrentTab(tag);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
        if (fragmentTabHost.getCurrentTabTag().equals(TAG_MAIN_TAB)) {
            mPresenter.showPushDialog();
        }
    }

    private void initData() {
        //首次进来如果没有网络设置定位失败设置显示的view
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            // setNotNetWorkMiddleView();
        } else {//有网络，去定位
            initTitleLocation();
        }
    }

    /**
     * 发送请求
     */
    private void sendUpdateAppRequest() {
        mPresenter.getAppUpdate(this);
    }

    private void initTabHost() {
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabContent_liking_home);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MAIN_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_lesson), R.drawable.xml_tab_liking_home_lesson))
                , LikingLessonFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_RECHARGE_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_recharge), R.drawable.xml_tab_liking_home_recharge))//setIndicator 设置标签样式
                , LikingBuyCardFragment.class, null); //setContent 点击标签后触发
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_my), R.drawable.xml_tab_liking_home_me))//setIndicator 设置标签样式
                , LikingMyFragment.class, null); //setContent 点击标签后触发
        tabWidget = fragmentTabHost.getTabWidget();
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabWidget.setBackgroundResource(R.color.main_app_color);
        tabWidget.setPadding(0, DisplayUtils.dp2px(8), 0, DisplayUtils.dp2px(8));
        setHomeTabHostListener();
    }

    private android.view.View buildTabIndicatorCustomView(String tabTitle, int drawableResId) {
        android.view.View tabView = getLayoutInflater().inflate(R.layout.layout_liking_home_tab_custom_view, null, false);
        ((ImageView) tabView.findViewById(R.id.imageview_chef_stove_tab)).setImageResource(drawableResId);
        ((TextView) tabView.findViewById(R.id.textview_chef_stove_tab)).setText(tabTitle);
        return tabView;
    }

    /***
     * 设置tab事件
     */
    private void setHomeTabHostListener() {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAG_MAIN_TAB)) {//首页
                    mPresenter.checkUpdateApp(LikingHomeActivity.this);
                    postEvent(new OnClickLessonFragmentMessage());
                } else if (tabId.equals(TAG_NEARBY_TAB)) {//购买营养餐
                } else if (tabId.equals(TAG_RECHARGE_TAB)) {//买卡
                    mPresenter.checkUpdateApp(LikingHomeActivity.this);
                } else if (tabId.equals(TAG_MY_TAB)) {//我的
                    mPresenter.checkUpdateApp(LikingHomeActivity.this);
                }
            }
        });
    }

    @Override
    public void showNoticesDialog(final Set<NoticeData> noticeData) {

    }

    @Override
    public void updateHasMessage(UnreadMessageResult.UnreadMsgData data) {
    }

    /***
     * 初始化定位
     */
    private void initTitleLocation() {
        new RxPermissions(this).request(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        initAmapGDLocation();
                    }
                });

    }

    public void initAmapGDLocation() {
        mAmapGDLocation = new AmapGDLocation(this);
        mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
            @Override
            public void receive(AMapLocation object) {
                if (object != null && object.getErrorCode() == 0) {//定位成功
                    isWhetherLocation = true;
                    LogUtils.i("dust", "city: " + object.getCity() + "; city code: " + object.getCityCode()
                            + " ; AdCodeId: " + object.getAdCode() + "; District:" + object.getDistrict() + "; Province:" + object.getProvince());
                    LogUtils.i("dust", "longitude:" + object.getLongitude() + "Latitude" + object.getLatitude());
                    currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                    String longitude = CityUtils.getLongitude(LikingHomeActivity.this, object.getCityCode(), object.getLongitude());
                    String latitude = CityUtils.getLatitude(LikingHomeActivity.this, object.getCityCode(), object.getLatitude());
                    String cityId = CityUtils.getCityId(LikingHomeActivity.this, object.getCityCode());
                    String districtId = object.getAdCode();
                    cityCode = object.getCityCode();
                    postEvent(new MainAddressChanged(longitude, latitude, cityId, districtId, currentCityName, true));
                    updateLocationPoint(cityId, districtId, longitude, latitude, currentCityName, true);

                    //虚拟定位
//                     postEvent(new MainAddressChanged(117.20, 34.26, "123456", "24", "徐州市", true));
//                     updateLocationPoint("123456", "24", 117.20, 34.26, "徐州市", true);

                } else {//定位失败
                    isWhetherLocation = false;
                    String cityId = CityUtils.getCityId(LikingHomeActivity.this, object.getCityCode());
                    String districtId = CityUtils.getDistrictId(LikingHomeActivity.this, object.getCityCode(), object.getDistrict());

                    postEvent(new MainAddressChanged("0", "0", cityId, districtId, currentCityName, false));
                    updateLocationPoint(cityId, districtId, "0", "0", currentCityName, false);
                }
            }

            @Override
            public void start() {
                isWhetherLocation = false;
                isStartLoaction = 1;
                // mLikingMiddleTitleTextView.setText("定位中...");
            }

            @Override
            public void end() {
                LogUtils.i("dust", "定位结束...");
            }
        });
        mAmapGDLocation.start();
    }

    private void updateLocationPoint(String cityId, String districtId, String longitude, String latitude, String cityName, boolean isLocation) {
        saveLocationInfo(cityId, districtId, longitude, latitude, cityName, isLocation);
    }

    private void saveLocationInfo(String cityId, String districtId, String longitude, String latitude, String cityName, boolean isLocation) {
        LocationData locationData = new LocationData(cityId, districtId, longitude, latitude, cityName, isLocation);
        LikingPreference.setLocationData(locationData);
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

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BuyCardMessage message) {
        if (message != null && fragmentTabHost != null) {
            fragmentTabHost.setCurrentTab(NumberConstantUtil.ONE);
        }
    }
    public void onEvent(LikingHomeNoNetWorkMessage message) {
        initTitleLocation();
    }

    public void onEvent(LoginInvalidMessage message) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("start_home", message.arg1);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                showToast(getString(R.string.exit_app));//再按一次退出应用
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                BaseApplication.exitApp();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void setPresenter() {
        mPresenter = new LikingHomeContract.Presenter(this);
    }
}
