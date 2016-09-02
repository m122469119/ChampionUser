package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.ConstantUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.web.HDefaultWebActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.adapter.SelectCityAdapter;
import com.goodchef.liking.dialog.HomeRightDialog;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnCLickBuyCardFragmentMessage;
import com.goodchef.liking.eventmessages.UserCityIdMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.CityUtils;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

public class LikingHomeActivity extends BaseActivity implements View.OnClickListener, LikingNearbyAdapter.ShoppingDishChangedListener {

    public static final String TAG_MAIN_TAB = "lesson";
    public static final String TAG_NEARBY_TAB = "nearby";
    public static final String TAG_RECHARGE_TAB = "recharge";
    public static final String TAG_MY_TAB = "my";
    public static final String INTENT_KEY_BUY_LIST = "intent_key_buy_list";
    public static final String INTENT_KEY_FOOD_OBJECT = "intent_key_food_object";

    public static final int INTENT_REQUEST_CODE_SHOP_CART = 200;
    private static final int INTENT_REQUEST_CODE_DISHES_DETIALS = 201;
    public static final String KEY_SELECT_CITY = "key_select_city";
    public static final String KEY_SELECT_CITY_ID = "key_select_city_id";
    public static final String KEY_START_LOCATION = "key_start_location";
    public static final String KEY_TAB_INDEX = "key_tab_index";

    private TextView mLikingLeftTitleTextView;//左边文字
    private TextView mLikingMiddleTitleTextView;//中间title
    private ImageView mRightImageView;//右边图片
    private TextView mLikingRightTitleTextView;//右边文字
    private TextView mLikingDistanceTextView;//距离
    private TextView mRedPoint;//红色点点
    private LinearLayout mMiddleLayout;
    private AppBarLayout mAppBarLayout;

    public TextView mShoppingCartNumTextView;

    private FragmentTabHost fragmentTabHost;
    private AmapGDLocation mAmapGDLocation;

    private String currentCityName = "";
    private String selectCityId;
    private String selectCityName = "";
    private boolean isLocation = false;
    private boolean isWhetherLocation = false;

    // private LikingNearbyFragment mLikingNearbyFragment = LikingNearbyFragment.newInstance();
    //  private ArrayList<Food> buyList = new ArrayList<>();
    private String mUserCityId;
    private SelectCityAdapter mSelectCityAdapter;
    private long firstTime = 0;//第一点击返回键
    private CoursesResult.Courses.Gym mGym;
    private HomeRightDialog RightMenuDialog;//右边加好

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_home);
        setTitle(R.string.activity_liking_home);
        RightMenuDialog = new HomeRightDialog(this);
        initViews();
        setViewOnClickListener();
        initData();
    }

    private void initData() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            isWhetherLocation = false;
            mLikingLeftTitleTextView.setText(R.string.home_left_menu);
        } else {
            initTitleLocation();
        }
    }

    private void checkAppUpdate() {
        if (LiKingVerifyUtils.sBaseConfigResult != null) {
            final BaseConfigResult.BaseConfigData.UpdateData updateData = LiKingVerifyUtils.sBaseConfigResult.getBaseConfigData().getUpdateData();
            if (updateData == null) {
                return;
            }
            int needUpdate = updateData.getUpdate();
            String title = updateData.getTitle();
            String content = updateData.getContent();
            String lastVersion = updateData.getLastestVer();
            String currentVersion = EnvironmentUtils.Config.getAppVersionName();

            if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
                return;
            }
            if (needUpdate == 0) {//不需要强制更新
                if (!StringUtils.isEmpty(lastVersion) && !lastVersion.equals(currentVersion)) {//升级
                    if (Preference.getIsUpdate()) {
                        showAppUpdateDialog(updateData, needUpdate);
                        Preference.setIsUpdateApp(false);
                    }
                }
            } else if (needUpdate == 1) {//需要强制更新
                showAppUpdateDialog(updateData, needUpdate);
            }

        }
    }

    private void showAppUpdateDialog(final BaseConfigResult.BaseConfigData.UpdateData updateData, int needUpdate) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_textview, null, false);
        TextView textView = (TextView) view.findViewById(R.id.dialog_custom_title);
        textView.setText((updateData.getTitle()));
        builder.setCustomTitle(view);
        builder.setMessage(updateData.getContent());
        if (needUpdate != 1) {
            builder.create().setCancelable(true);
            builder.setNegativeButton(getString(R.string.dialog_know), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.create().setCancelable(false);
        }
        builder.setPositiveButton(getString(R.string.dialog_app_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HDefaultWebActivity.launch(LikingHomeActivity.this, updateData.getUrl(), ConstantUtils.BLANK_STRING);
            }
        });
        builder.show();
    }

    private void initViews() {
        mLikingLeftTitleTextView = (TextView) findViewById(R.id.liking_left_title_text);
        mLikingMiddleTitleTextView = (TextView) findViewById(R.id.liking_middle_title_text);
        mLikingDistanceTextView = (TextView) findViewById(R.id.liking_distance_text);
        mRightImageView = (ImageView) findViewById(R.id.liking_right_imageView);
        mLikingRightTitleTextView = (TextView) findViewById(R.id.liking_right_title_text);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.liking_home_appBar);
        mShoppingCartNumTextView = (TextView) findViewById(R.id.tv_shopping_cart_num);
        mRedPoint = (TextView) findViewById(R.id.home_notice_prompt);
        mMiddleLayout = (LinearLayout) findViewById(R.id.layout_home_middle);
        initTabHost();
    }

    private void setViewOnClickListener() {
        mLikingLeftTitleTextView.setOnClickListener(this);
        mLikingRightTitleTextView.setOnClickListener(this);
        mRightImageView.setOnClickListener(this);
        mMiddleLayout.setOnClickListener(this);
    }

    private void initTabHost() {
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabContent_liking_home);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MAIN_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_lesson), R.drawable.xml_tab_liking_home_lesson))
                , LikingLessonFragment.class, null);
//        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_NEARBY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_nearby), R.drawable.xml_tab_liking_home_nearby))
//                , NutrimealFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_RECHARGE_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_recharge), R.drawable.xml_tab_liking_home_recharge))//setIndicator 设置标签样式
                , LikingBuyCardFragment.class, null); //setContent 点击标签后触发
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_my), R.drawable.xml_tab_liking_home_me))//setIndicator 设置标签样式
                , LikingMyFragment.class, null); //setContent 点击标签后触发
        TabWidget tabWidget = fragmentTabHost.getTabWidget();
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabWidget.setBackgroundResource(R.color.main_app_color);
        tabWidget.setPadding(0, DisplayUtils.dp2px(8), 0, DisplayUtils.dp2px(8));
        setHomeTabHost();
        setMainTableView();
    }

    private void setMainTableView() {
      //  mLikingLeftTitleTextView.setText(R.string.home_left_menu);
        mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_home_menu));
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
                if (tabId.equals(TAG_MAIN_TAB)) {//首页
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingMiddleTitleTextView.setVisibility(View.VISIBLE);
                    mLikingDistanceTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.GONE);
                    mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
//                    mLikingLeftTitleTextView.setText(R.string.home_left_menu);
                    mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
                    mRightImageView.setVisibility(View.VISIBLE);
                    mRedPoint.setVisibility(View.VISIBLE);
                    mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_home_menu));
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                    setHomeTitle();
                    setHomeMenuReadNotice();
                } else if (tabId.equals(TAG_NEARBY_TAB)) {//购买营养餐
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextView.setText(R.string.tab_liking_home_nearby);
                    mRightImageView.setVisibility(View.GONE);
                    mRedPoint.setVisibility(View.GONE);
                    mLikingDistanceTextView.setVisibility(View.GONE);
                    //  mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_shopping_cart));
//                    if (calcDishSize() > 0) {
//                        mShoppingCartNumTextView.setVisibility(View.VISIBLE);
//                        mShoppingCartNumTextView.setText(calcDishSize() + "");
//                    } else {
//                        mShoppingCartNumTextView.setVisibility(View.GONE);
//                    }
                } else if (tabId.equals(TAG_RECHARGE_TAB)) {//买卡
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingMiddleTitleTextView.setVisibility(View.VISIBLE);
                    mLikingDistanceTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.GONE);
                    mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
//                    mLikingLeftTitleTextView.setText(R.string.home_left_menu);
                    mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
                    mRightImageView.setVisibility(View.GONE);
                    mRedPoint.setVisibility(View.GONE);
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                    setHomeTitle();
                    postEvent(new OnCLickBuyCardFragmentMessage());
                } else if (tabId.equals(TAG_MY_TAB)) {//我的
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingDistanceTextView.setVisibility(View.GONE);
                    mLikingMiddleTitleTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextView.setText(R.string.tab_liking_home_my);
                    mRightImageView.setVisibility(View.GONE);
                    mRedPoint.setVisibility(View.GONE);
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        String tag = fragmentTabHost.getCurrentTabTag();
        if (v == mLikingLeftTitleTextView) {
            if (tag.equals(TAG_MAIN_TAB)) {
                changeGym(0);
            } else if (tag.equals(TAG_RECHARGE_TAB)) {
                changeGym(1);
            }
        } else if (v == mRightImageView) {
            if (tag.equals(TAG_NEARBY_TAB)) {
//                if (buyList != null && buyList.size() > 0 && calcDishSize() > 0) {
//                    Intent intent = new Intent(this, ShoppingCartActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList(INTENT_KEY_BUY_LIST, buyList);
//                    intent.putExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, INTENT_REQUEST_CODE_SHOP_CART);
//                } else {
//                    PopupUtils.showToast("您还没有购买任何营养餐");
//                }
            } else if (tag.equals(TAG_MAIN_TAB)) {
                String announcementId = Preference.getAnnouncementId();
                if (!StringUtils.isEmpty(announcementId) || !StringUtils.isEmpty(mGym.getAnnouncementId())) {
                    if (mGym.getAnnouncementId().equals(announcementId)) {
                        RightMenuDialog.setRedPromptShow(false);
                        mRedPoint.setVisibility(View.GONE);
                    } else {
                        RightMenuDialog.setRedPromptShow(true);
                        mRedPoint.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!StringUtils.isEmpty(mGym.getAnnouncementId())) {
                        RightMenuDialog.setRedPromptShow(true);
                        mRedPoint.setVisibility(View.VISIBLE);
                    }
                }
                showRightMenuDialog();
            }
        } else if (v == mMiddleLayout) {
            if (tag.equals(TAG_MAIN_TAB)) {
                jumpArenaActivity();
            } else if (tag.equals(TAG_RECHARGE_TAB)) {
                jumpArenaActivity();
            }
        }
    }

    private void jumpArenaActivity() {
        if (isWhetherLocation) {//定位成功时查看门店
            if (!StringUtils.isEmpty(mGym.getGymId())) {
                UMengCountUtil.UmengCount(LikingHomeActivity.this, UmengEventId.ARENAACTIVITY);
                Intent intent = new Intent(this, ArenaActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
                this.startActivity(intent);
                this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
            }
        } else {//定位失败是，重新定位
            initTitleLocation();
        }
    }

    private void showRightMenuDialog() {
        RightMenuDialog.setAnchor(mRightImageView);
        RightMenuDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_notice://公告
                        if (!StringUtils.isEmpty(mGym.getAnnouncementInfo())) {
                            showNoticeDialog();
                            RightMenuDialog.dismiss();
                        }
                        break;
                    case R.id.layout_open_door://开门
                        jumpOpenTheDoorActivity();
                        RightMenuDialog.dismiss();
                        break;

                }
            }
        });
        RightMenuDialog.show();
    }


    /**
     * 查看公告
     */
    private void showNoticeDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_textview, null, false);
        TextView textView = (TextView) view.findViewById(R.id.dialog_custom_title);
        textView.setText(R.string.notice);
        builder.setCustomTitle(view);
        builder.setMessage(mGym.getAnnouncementInfo());
        builder.setNegativeButton(R.string.diaog_got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Preference.setAnnouncementId(mGym.getAnnouncementId());
                mRedPoint.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 开门
     */
    private void jumpOpenTheDoorActivity() {
        if (StringUtils.isEmpty(currentCityName)) {
            UMengCountUtil.UmengCount(this, UmengEventId.OPENTHEDOORACTIVITY, "定位失败");
        } else {
            UMengCountUtil.UmengCount(this, UmengEventId.OPENTHEDOORACTIVITY, currentCityName);
        }
        Intent intent = new Intent(this, OpenTheDoorActivity.class);
        startActivity(intent);
    }


    /**
     * 切换场馆
     */
    private void changeGym(int index) {
        if (isWhetherLocation) {
            if (currentCityName.equals(selectCityName)) {//当选择的城市和当前定位城市相同，在查看场馆中开启定位
                isLocation = true;
            } else {
                isLocation = false;
            }
            if (!StringUtils.isEmpty(mGym.getGymId())) {
                Intent intent = new Intent(this, LookStoreMapActivity.class);
                intent.putExtra(KEY_SELECT_CITY, selectCityName);
                intent.putExtra(KEY_START_LOCATION, isLocation);
                intent.putExtra(KEY_SELECT_CITY_ID, selectCityId);
                intent.putExtra(KEY_TAB_INDEX, index);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
                startActivity(intent);
            }
        } else {
            PopupUtils.showToast("定位失败，无法获取城市地图");
        }
    }

    /**
     * 展示选择城市dialog
     */
    private void showSelectCityDialog() {
        if (StringUtils.isEmpty(currentCityName)) {
            UMengCountUtil.UmengBtnCount(this, UmengEventId.CHECK_CITY, "定位失败");
        } else {
            UMengCountUtil.UmengBtnCount(this, UmengEventId.CHECK_CITY, currentCityName);
        }
        final HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_city, null, false);
        TextView locationAddress = (TextView) view.findViewById(R.id.dialog_location_address);
        TextView getCityBtn = (TextView) view.findViewById(R.id.get_city_btn);
        ListView mCityListView = (ListView) view.findViewById(R.id.city_listView);
        if (!StringUtils.isEmpty(currentCityName)) {
            locationAddress.setText("定位城市：" + currentCityName);
        } else {
            locationAddress.setText("定位失败");
        }
        getCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTitleLocation();
                builder.create().dismiss();
            }
        });
        setCityData(mCityListView, builder);
        builder.setCustomView(view);
        builder.setPositiveButton("查看场馆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isWhetherLocation) {
                    if (currentCityName.equals(selectCityName)) {//当选择的城市和当前定位城市相同，在查看场馆中开启定位
                        isLocation = true;
                    } else {
                        isLocation = false;
                    }
                    Intent intent = new Intent(LikingHomeActivity.this, LookStoreMapActivity.class);
                    intent.putExtra(KEY_SELECT_CITY, selectCityName);
                    intent.putExtra(KEY_SELECT_CITY_ID, selectCityId);
                    intent.putExtra(KEY_START_LOCATION, isLocation);
                    startActivity(intent);
                } else {
                    PopupUtils.showToast("定位失败，无法获取城市地图");
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置选择城市的数据
     *
     * @param mCityListView
     * @param builder
     */
    private void setCityData(ListView mCityListView, final HBaseDialog.Builder builder) {
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                List<CityData> cityDataList = baseConfigData.getCityList();
                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData cityData : cityDataList) {
                        if (cityData.getCityName().contains(selectCityName)) {
                            cityData.setSelct(true);
                        } else {
                            cityData.setSelct(false);
                        }
                    }
                    mSelectCityAdapter = new SelectCityAdapter(this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RelativeLayout textView = (RelativeLayout) v.findViewById(R.id.layout_city);
                            if (textView != null) {
                                CityData cityData = (CityData) textView.getTag();
                                if (cityData != null) {
                                    List<CityData> cityDataList = mSelectCityAdapter.getDataList();
                                    for (CityData dto : cityDataList) {
                                        if (dto.getCityId() == cityData.getCityId()) {
                                            dto.setSelct(true);
                                        } else {
                                            dto.setSelct(false);
                                        }
                                    }
                                    mSelectCityAdapter.notifyDataSetChanged();
                                    selectCityName = cityData.getCityName();
                                    selectCityId = cityData.getCityId() + "";
                                    //发送消息更新首页数据
                                    postEvent(new MainAddressChanged(0, 0, cityData.getCityId() + "", "0", selectCityName, true));
                                    builder.create().dismiss();
                                }
                            }
                        }
                    });
                    mSelectCityAdapter.setData(cityDataList);
                    mCityListView.setAdapter(mSelectCityAdapter);
                }
            }
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
                LiKingVerifyUtils.initApi(LikingHomeActivity.this);
                if (object != null && object.getErrorCode() == 0) {//定位成功
                    isWhetherLocation = true;
                    LogUtils.i("dust", "city: " + object.getCity() + " city code: " + object.getCityCode());
                    LogUtils.i("dust", "longitude:" + object.getLongitude() + "Latitude" + object.getLatitude());
                    currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                    selectCityName = currentCityName;//默认设置当前定位为选中城市
                    selectCityId = CityUtils.getCityId(object.getProvince(), object.getCity());//设置当前定位城市id,为定位城市id
                    postEvent(new MainAddressChanged(object.getLongitude(), object.getLatitude(), CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), currentCityName, true));
                    updateLocationPoint(CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), object.getLongitude(), object.getLatitude(), currentCityName);
                } else {//定位失败
                    isWhetherLocation = false;
                    postEvent(new MainAddressChanged(0, 0, CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), currentCityName, false));
                    updateLocationPoint(CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), 0, 0, currentCityName);
                }
            }

            @Override
            public void start() {
            }

            @Override
            public void end() {
                LogUtils.i("dust", "定位结束...");
                checkAppUpdate();
            }
        });
        mAmapGDLocation.start();
    }

    private void updateLocationPoint(String cityId, String districtId, double longitude, double latitude, String cityName) {
        saveLocationInfo(cityId, districtId, longitude, latitude, cityName);
    }

    private void saveLocationInfo(String cityId, String districtId, double longitude, double latitude, String cityName) {
        LocationData locationData = new LocationData(cityId, districtId, longitude, latitude, cityName);
        Preference.setLocationData(locationData);
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
    public void onShoppingDishAdded(Food foodData) {
//        int riceNum = foodData.getSelectedOrderNum();
//        if (riceNum >= foodData.getRestStock()) {
//            PopupUtils.showToast("单个最多只能购买" + foodData.getRestStock() + "份");
//        }
//
//        if (buyList != null && buyList.size() > 0) {
//            boolean isBuyListExits = false;
//            for (int i = 0; i < buyList.size(); i++) {
//                if (buyList.get(i).getGoodsId().equals(foodData.getGoodsId())) {
//                    buyList.get(i).setSelectedOrderNum(foodData.getSelectedOrderNum());
//                    isBuyListExits = true;
//                    break;
//                }
//            }
//            if (!isBuyListExits) {
//                buyList.add(foodData);
//            }
//
//        } else {
//            buyList.add(foodData);
//        }
//        if (calcDishSize() > 0) {
//            mShoppingCartNumTextView.setVisibility(View.VISIBLE);
//        }
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
    }

    @Override
    public void onShoppingDishRemove(Food foodData) {
//        if (buyList != null && buyList.size() > 0) {
//            for (int i = 0; i < buyList.size(); i++) {
//                if (buyList.get(i).getGoodsId().equals(foodData.getGoodsId())) {
//                    buyList.get(i).setSelectedOrderNum(foodData.getSelectedOrderNum());
//                    if (buyList.get(i).getSelectedOrderNum() == 0) {
//                        buyList.remove(buyList.get(i));
//                    }
//                }
//            }
//        }
//
//        if (calcDishSize() == 0) {
//            mShoppingCartNumTextView.setVisibility(View.GONE);
//        }
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
    }

//    private int calcDishSize() {
//        int num = 0;
//        if (!ListUtils.isEmpty(buyList)) {
//            for (Food data : buyList) {
//                int n = data.getSelectedOrderNum();
//                num += n;
//            }
//        }
//        return num;
//    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UserCityIdMessage userCityIdMessage) {
        mUserCityId = userCityIdMessage.getUserCityId();
    }

    public void onEvent(getGymDataMessage message) {
        mGym = message.getGym();
        setHomeTitle();
        setHomeMenuReadNotice();
    }

    /**
     * 设置首页标题
     */
    private void setHomeTitle() {
        if (isWhetherLocation) {
            mLikingDistanceTextView.setText(mGym.getDistance());
            mLikingMiddleTitleTextView.setText(mGym.getName());
        } else {
            mLikingMiddleTitleTextView.setText("定位失败");
            mLikingDistanceTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否读取过公告
     */
    private void setHomeMenuReadNotice() {
        String announcementId = Preference.getAnnouncementId();
        Log.i(TAG, "announcementId = " + announcementId);
        if (!StringUtils.isEmpty(announcementId) && !StringUtils.isEmpty(mGym.getAnnouncementId())) {
            if (mGym.getAnnouncementId().equals(announcementId)) {
                mRedPoint.setVisibility(View.GONE);
            } else {
                mRedPoint.setVisibility(View.VISIBLE);
            }
        } else {
            if (!StringUtils.isEmpty(mGym.getAnnouncementId())) {
                mRedPoint.setVisibility(View.VISIBLE);
            }
        }
    }

//    public void onEvent(JumpToDishesDetailsMessage jumpToDishesDetailsMessage) {
//        String mUserCityId = jumpToDishesDetailsMessage.getUserCityId();
//        Food foodData = jumpToDishesDetailsMessage.getFoodData();
//        Intent intent = new Intent(this, DishesDetailsActivity.class);
//        intent.putExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(INTENT_KEY_FOOD_OBJECT, foodData);
//        bundle.putParcelableArrayList(INTENT_KEY_BUY_LIST, buyList);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, INTENT_REQUEST_CODE_DISHES_DETIALS);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_SHOP_CART) {//从购物车回来时，带回购物车数据，从新计算购物车数量
//                boolean isClearCart = data.getBooleanExtra(ShoppingCartActivity.KEY_CLEAR_CART, false);
//                Bundle bundle = data.getExtras();
//                buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
//                if (isClearCart) {//如果是清空购物车，清除购买集合
//                    buyList.clear();
//                }
//                postEvent(new RefreshChangeDataMessage(buyList, isClearCart));
//                if (calcDishSize() > 0) {
//                    mShoppingCartNumTextView.setVisibility(View.VISIBLE);
//                    mShoppingCartNumTextView.setText(calcDishSize() + "");
//                } else {
//                    mShoppingCartNumTextView.setVisibility(View.GONE);
//                }
            } else if (requestCode == INTENT_REQUEST_CODE_DISHES_DETIALS) {//从单个商品详情回来，带回购买数据集合，从新计算购物车数量
//                Bundle bundle = data.getExtras();
//                buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
//                postEvent(new RefreshChangeDataMessage(buyList, false));
//                if (calcDishSize() > 0) {
//                    mShoppingCartNumTextView.setVisibility(View.VISIBLE);
//                    mShoppingCartNumTextView.setText(calcDishSize() + "");
//                } else {
//                    mShoppingCartNumTextView.setVisibility(View.GONE);
//                }
            }
        }
    }


//    public void onEvent(DishesWechatPayMessage wechatMessage) {
//        buyList.clear();
//        postEvent(new RefreshChangeDataMessage(buyList, true));
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
//    }

//    public void onEvent(DishesAliPayMessage dishesAliPayMessage) {
//        buyList.clear();
//        postEvent(new RefreshChangeDataMessage(buyList, true));
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
//    }

//    public void onEvent(DishesPayFalse dishesWechatPayFalse) {
//        buyList.clear();
//        postEvent(new RefreshChangeDataMessage(buyList, true));
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
//    }

//    public void onEvent(FreePayMessage message) {
//        buyList.clear();
//        postEvent(new RefreshChangeDataMessage(buyList, true));
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
//    }

//    public void onEvent(ClearCartMessage message) {
//        buyList.clear();
//        postEvent(new RefreshChangeDataMessage(buyList, true));
//        mShoppingCartNumTextView.setText(calcDishSize() + "");
//    }

    public void onEvent(LikingHomeNoNetWorkMessage message) {
        initTitleLocation();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                PopupUtils.showToast("再按一次退出应用");//再按一次退出应用
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                BaseApplication.getInstance().exitApp();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
