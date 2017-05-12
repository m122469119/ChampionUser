package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.map.LocationListener;
import com.aaron.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.dialog.CancelOnClickListener;
import com.goodchef.liking.dialog.ConfirmOnClickListener;
import com.goodchef.liking.dialog.DefaultGymDialog;
import com.goodchef.liking.dialog.HomeRightDialog;
import com.goodchef.liking.eventmessages.BuyCardMessage;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.LikingHomeActivityMessage;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnCLickBuyCardFragmentMessage;
import com.goodchef.liking.eventmessages.OnClickLessonFragmentMessage;
import com.goodchef.liking.eventmessages.UserCityIdMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.result.data.NoticeData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.CheckUpdateAppPresenter;
import com.goodchef.liking.mvp.presenter.LikingHomePresenter;
import com.goodchef.liking.mvp.view.CheckUpdateAppView;
import com.goodchef.liking.mvp.view.LikingHomeView;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.CityUtils;
import com.goodchef.liking.utils.FileDownloaderManager;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.Iterator;
import java.util.Set;

public class LikingHomeActivity extends BaseActivity implements View.OnClickListener,
        LikingNearbyAdapter.ShoppingDishChangedListener,
        CheckUpdateAppView,
        LikingHomeView {

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
    public static final String KEY_INTENT_TAB = "key_intent_tab";
    public static final String KEY_WHETHER_LOCATION = "key_whether_location";

    private TextView mLikingLeftTitleTextView;//左边文字
    private TextView mLikingMiddleTitleTextView;//中间title
    private ImageView mRightImageView;//右边图片
    private TextView mLikingRightTitleTextView;//右边文字
    private TextView mLikingDistanceTextView;//距离
    private TextView mRedPoint;//红色点点
    private RelativeLayout mMiddleLayout;//title中间布局

    public TextView mShoppingCartNumTextView;//购物车数量
    public int mCanSchedule = -1;//是否支持自助团体课

    private FragmentTabHost fragmentTabHost;
    private TabWidget tabWidget;
    private AmapGDLocation mAmapGDLocation;//定位

    private String currentCityName = "";
    public boolean isWhetherLocation = false;
    public static String gymId;
    public static String gymName = "";
    public static String gymTel = "";
    public static int defaultGym;
    private long firstTime = 0;//第一点击返回键
    private CoursesResult.Courses.Gym mGym;//买卡界面传过来的带有城市id的Gym对象
    private CoursesResult.Courses.Gym mNoticeGym;//带有公告的Gym对象
    private HomeRightDialog RightMenuDialog;//右边加好
    private CheckUpdateAppPresenter mCheckUpdateAppPresenter;
    private LikingHomePresenter mPresenter;
    private CheckUpdateAppResult.UpdateAppData mUpDateAppData;
    private FileDownloaderManager mFileDownloaderManager;
    private String cityCode;//高德地图定位的城市返回的code码
    public static boolean isChangeGym = false;
    public static boolean shoDefaultDialog = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_home);
        mPresenter = new LikingHomePresenter(this, this);
        initGymId();
        setTitle(R.string.activity_liking_home);
        RightMenuDialog = new HomeRightDialog(this);
        initViews();
        sendUpdateAppRequest();
        setViewOnClickListener();
        initData();
    }



    /**
     * 初始化gymId
     */
    private void initGymId() {
        String id = LikingPreference.getLoginGymId();
        if (!StringUtils.isEmpty(id) && !"0".equals(id)) {
            gymId = id;
        } else {
            gymId = "0";
        }
    }

    public static final String ACTION = "action";

    public static final int SHOW_PUSH_NOTICE = 0x00001111;

    public static final int SHOW_PUSH_NOTICE_RECEIVED = 0x00001112;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
        if (fragmentTabHost.getCurrentTabTag().equals(TAG_MAIN_TAB))
            mPresenter.showPushDialog();
    }

    private void initData() {
        //首次进来如果没有网络设置定位失败设置显示的view
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            setNotNetWorkMiddleView();
        } else {//有网络，去定位
            initTitleLocation();
        }
    }

    /**
     * 发送请求
     */
    private void sendUpdateAppRequest() {
        if (mCheckUpdateAppPresenter == null) {
            mCheckUpdateAppPresenter = new CheckUpdateAppPresenter(this, this);
        }
        mCheckUpdateAppPresenter.getUpdateApp();
    }

    @Override
    public void updateCheckUpdateAppView(CheckUpdateAppResult.UpdateAppData updateAppData) {
        mUpDateAppData = updateAppData;
        checkUpdateApp();
    }

    private void checkUpdateApp() {
        if (mUpDateAppData == null) {
            return;
        }
        int update = mUpDateAppData.getUpdate();
        if (update == 0) {//无更新
            LikingPreference.setUpdateApp(0);
        } else if (update == 1) {//有更新
            LikingPreference.setUpdateApp(1);
            LikingPreference.setNewApkName(mUpDateAppData.getLastestVer());
            if (LikingPreference.getIsUpdate()) {
                LikingPreference.setIsUpdateApp(false);
                showCheckUpdateDialog(false);
            }
        } else if (update == 2) {//强制更新
            LikingPreference.setUpdateApp(2);
            LikingPreference.setNewApkName(mUpDateAppData.getLastestVer());
            showCheckUpdateDialog(true);
        }
    }


    /**
     * 更新app弹出框
     *
     * @param isForceUpdate
     */
    private void showCheckUpdateDialog(boolean isForceUpdate) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_textview, null, false);
        TextView textView = (TextView) view.findViewById(R.id.dialog_custom_title);
        textView.setText((mUpDateAppData.getTitle()));
        builder.setCustomTitle(view);
        builder.setMessage(mUpDateAppData.getContent());
        if (!isForceUpdate) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setPositiveButton(R.string.dialog_app_update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!StringUtils.isEmpty(mUpDateAppData.getUrl())) {
                    mFileDownloaderManager = new FileDownloaderManager(LikingHomeActivity.this);
                    mFileDownloaderManager.downloadFile(mUpDateAppData.getUrl(), DiskStorageManager.getInstance().getApkFileStoragePath());
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void initViews() {
        mLikingLeftTitleTextView = (TextView) findViewById(R.id.liking_left_title_text);
        mLikingMiddleTitleTextView = (TextView) findViewById(R.id.liking_middle_title_text);
        mLikingDistanceTextView = (TextView) findViewById(R.id.liking_distance_text);
        mRightImageView = (ImageView) findViewById(R.id.liking_right_imageView);
        mLikingRightTitleTextView = (TextView) findViewById(R.id.liking_right_title_text);
        mShoppingCartNumTextView = (TextView) findViewById(R.id.tv_shopping_cart_num);
        mRedPoint = (TextView) findViewById(R.id.home_notice_prompt);
        mMiddleLayout = (RelativeLayout) findViewById(R.id.layout_home_middle);
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
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_RECHARGE_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_recharge), R.drawable.xml_tab_liking_home_recharge))//setIndicator 设置标签样式
                , LikingBuyCardFragment.class, null); //setContent 点击标签后触发
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TAG_MY_TAB).setIndicator(buildTabIndicatorCustomView(getString(R.string.tab_liking_home_my), R.drawable.xml_tab_liking_home_me))//setIndicator 设置标签样式
                , LikingMyFragment.class, null); //setContent 点击标签后触发
        tabWidget = fragmentTabHost.getTabWidget();
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabWidget.setBackgroundResource(R.color.main_app_color);
        tabWidget.setPadding(0, DisplayUtils.dp2px(8), 0, DisplayUtils.dp2px(8));
        setHomeTabHostListener();
        setMainTableView();
    }

    /**
     * 设置初始标题view
     */
    private void setMainTableView() {
        mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
        mLikingLeftTitleTextView.setText(R.string.title_change_gym);
        //  mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_home_menu));
    }

    private View buildTabIndicatorCustomView(String tabTitle, int drawableResId) {
        View tabView = getLayoutInflater().inflate(R.layout.layout_liking_home_tab_custom_view, null, false);
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
                    setTagMainTab();
                    setHomeTitle();
                    setHomeMenuReadNotice();
                    checkUpdateApp();
                    postEvent(new OnClickLessonFragmentMessage());
                } else if (tabId.equals(TAG_NEARBY_TAB)) {//购买营养餐
                    setTagRechargeTab();
                } else if (tabId.equals(TAG_RECHARGE_TAB)) {//买卡
                    setTagNearbyTab();
                    setHomeTitle();
                    postEvent(new OnCLickBuyCardFragmentMessage());
                    checkUpdateApp();
                } else if (tabId.equals(TAG_MY_TAB)) {//我的
                    setTagMyTab();
                    checkUpdateApp();
                }
            }
        });
    }

    /**
     * 设置首页界面tab
     */
    private void setTagMainTab() {
        mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
        if (mGym != null && !StringUtils.isEmpty(mGym.getCityName())) {
            mLikingLeftTitleTextView.setText(mGym.getCityName());
        }
        //  mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
        mLikingDistanceTextView.setVisibility(View.VISIBLE);
        mLikingRightTitleTextView.setVisibility(View.GONE);
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_home_menu));
        mShoppingCartNumTextView.setVisibility(View.GONE);
    }

    /**
     * 设置买卡界面tab
     */
    private void setTagNearbyTab() {
        mLikingLeftTitleTextView.setVisibility(View.VISIBLE);
        // mLikingLeftTitleTextView.setBackgroundResource(R.drawable.icon_chenge);
        if (mGym != null && !StringUtils.isEmpty(mGym.getCityName())) {
            mLikingLeftTitleTextView.setText(mGym.getCityName());
        }
        mLikingDistanceTextView.setVisibility(View.VISIBLE);
        mLikingRightTitleTextView.setVisibility(View.GONE);
        mRightImageView.setVisibility(View.GONE);
        mShoppingCartNumTextView.setVisibility(View.GONE);
        mRedPoint.setVisibility(View.GONE);
    }

    /**
     * 设置营养餐tab
     */
    private void setTagRechargeTab() {
        mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
        mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
        // mLikingMiddleTitleTextView.setText(R.string.tab_liking_home_nearby);
        mRightImageView.setVisibility(View.GONE);
        mRedPoint.setVisibility(View.GONE);
        mLikingDistanceTextView.setVisibility(View.GONE);
    }

    /**
     * 设置我的界面Tab
     */
    private void setTagMyTab() {
        mLikingLeftTitleTextView.setVisibility(View.GONE);
        mLikingDistanceTextView.setVisibility(View.GONE);
        mLikingRightTitleTextView.setVisibility(View.GONE);
        mLikingMiddleTitleTextView.setText(R.string.tab_liking_home_my);
        mRightImageView.setVisibility(View.GONE);
        mRedPoint.setVisibility(View.GONE);
        mShoppingCartNumTextView.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        String tag = fragmentTabHost.getCurrentTabTag();
        if (v == mLikingLeftTitleTextView) {
            if (tag.equals(TAG_MAIN_TAB)) {
                changeGym(NumberConstantUtil.ZERO);//从首页切换过去
            } else if (tag.equals(TAG_RECHARGE_TAB)) {
                changeGym(NumberConstantUtil.ONE);//从买卡界面切换过去
            }
        } else if (v == mRightImageView) {
            if (tag.equals(TAG_NEARBY_TAB)) {
            } else if (tag.equals(TAG_MAIN_TAB)) {
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

    /**
     * 显示默认场馆的对话框
     */
    private void setDefaultGymDialog(String text, boolean isDefaultGym) {
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(this, DefaultGymDialog.defaultGymType);
        defaultGymDialog.setCancelable(false);
        defaultGymDialog.setCanceledOnTouchOutside(false);
        if (isDefaultGym) {
            defaultGymDialog.setDefaultPromptView(text);
        } else {
            defaultGymDialog.setCurrentCityNotOpen(text);
        }
        defaultGymDialog.setCancelClickListener(new CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
                if (!shoDefaultDialog) {
                    setHomeMenuReadNotice();
                }
            }
        });

        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                changeGym(0);
                dialog.dismiss();
            }
        });

    }

    /***
     * 跳转门店
     */
    private void jumpArenaActivity() {
        if (mGym != null && !StringUtils.isEmpty(mGym.getGymId()) && !StringUtils.isEmpty(mGym.getName())) {
            UMengCountUtil.UmengCount(LikingHomeActivity.this, UmengEventId.ARENAACTIVITY);
            Intent intent = new Intent(this, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
        }
    }

    /**
     * 展示右边按钮
     */
    private void showRightMenuDialog() {
        UMengCountUtil.UmengBtnCount(this, UmengEventId.ADD_BTN, currentCityName);
        RightMenuDialog.setAnchor(mRightImageView);
        RightMenuDialog.setViewOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       switch (v.getId()) {
                                                           case R.id.layout_notice://公告
                                                               if (mNoticeGym != null) {
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
                                               }

        );
        RightMenuDialog.show();
    }


    /**
     * 查看公告
     */
    private DefaultGymDialog showNoticeDialog() {
        UMengCountUtil.UmengBtnCount(this, UmengEventId.CHECK_ANNOUNCEMENT, currentCityName);
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(this, DefaultGymDialog.noticeType);
        defaultGymDialog.setCancelable(true);
        defaultGymDialog.setCanceledOnTouchOutside(true);
        LogUtils.e(TAG, "------------->mNoticeGym.getAnnouncementId() == " + mNoticeGym.getAnnouncementId());
        if (!StringUtils.isEmpty(mNoticeGym.getAnnouncementId())) {
            if (!StringUtils.isEmpty(mNoticeGym.getAnnouncementInfo())) {
                defaultGymDialog.setNoticesMessage(mNoticeGym.getName(), mNoticeGym.getAnnouncementInfo());
            } else {
                defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
            }
            LikingPreference.setAnnouncementId(mNoticeGym.getAnnouncementId());
            mRedPoint.setVisibility(View.GONE);
            RightMenuDialog.setRedPromptShow(false);
        } else if (!StringUtils.isEmpty(mNoticeGym.getAnnouncementInfo())) {
            defaultGymDialog.setNoticesMessage(mNoticeGym.getName(), mNoticeGym.getAnnouncementInfo());
            LikingPreference.setAnnouncementId(mNoticeGym.getAnnouncementId());
            mRedPoint.setVisibility(View.GONE);
            RightMenuDialog.setRedPromptShow(false);
        } else {
            defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
        }
        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });
        return defaultGymDialog;
    }

    @Override
    public void showNoticesDialog(final Set<NoticeData> noticeDatas) {
        Iterator<NoticeData> iterator = noticeDatas.iterator();
        NoticeData next = null;
        if (iterator.hasNext()) {
            next = iterator.next();
            noticeDatas.remove(next);
        } else {
            return;
        }

        LogUtils.e(TAG, "------------->mNoticeGym.getAnnouncementId() == " + next.getAid());

        if (!LikingPreference.isIdenticalAnnouncement(next.getGym_id())) {
            showNoticesDialog(noticeDatas);
        }

        UMengCountUtil.UmengBtnCount(this, UmengEventId.CHECK_ANNOUNCEMENT, currentCityName);
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(this, DefaultGymDialog.noticeType);
        defaultGymDialog.setCancelable(true);
        defaultGymDialog.setCanceledOnTouchOutside(true);

        if (!StringUtils.isEmpty(next.getAid())) {
            if (!StringUtils.isEmpty(next.getGymContent())) {
                defaultGymDialog.setNoticesMessage(next.getGymName(), next.getGymContent());
            } else {
                defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
            }
            LikingPreference.setAnnouncementId(next.getAid());
        } else if (!StringUtils.isEmpty(next.getGymContent())) {
            defaultGymDialog.setNoticesMessage(next.getGymName(), next.getGymContent());
            LikingPreference.setAnnouncementId(next.getAid());
        } else {
            defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
        }
        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
                showNoticesDialog(noticeDatas);
            }
        });
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
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            showToast(getString(R.string.network_no_connection));
            return;
        }
        if (mGym != null && !StringUtils.isEmpty(mGym.getGymId()) && !StringUtils.isEmpty(mGym.getCityId())) {
            if (StringUtils.isEmpty(currentCityName)) {
                UMengCountUtil.UmengCount(this, UmengEventId.CHANGE_GYM_ACTIVITY, "定位失败");
            } else {
                UMengCountUtil.UmengCount(this, UmengEventId.CHANGE_GYM_ACTIVITY, currentCityName);
            }
            Intent intent = new Intent(this, ChangeGymActivity.class);
            intent.putExtra(KEY_SELECT_CITY_ID, mGym.getCityId());
            intent.putExtra(KEY_WHETHER_LOCATION, isWhetherLocation);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
            intent.putExtra(KEY_TAB_INDEX, index);
            startActivity(intent);
        } else {
            showToast(getString(R.string.network_home_error));
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

                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    setHomeTitle();
                }
            }

            @Override
            public void start() {
                isWhetherLocation = false;
                mLikingMiddleTitleTextView.setText("定位中...");
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
    public void onShoppingDishAdded(Food foodData) {
    }

    @Override
    public void onShoppingDishRemove(Food foodData) {
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UserCityIdMessage userCityIdMessage) {

    }

    public void onEvent(getGymDataMessage message) {
        mGym = message.getGym();
        setHomeTitle();
        setHomeMenuReadNotice();//切换场馆有公告就弹出公告
    }

    /**
     * @param message
     */
    public void onEvent(GymNoticeMessage message) {
        mNoticeGym = message.getGym();
        mCanSchedule = mNoticeGym.getCanSchedule();
        if (mNoticeGym != null
                && !StringUtils.isEmpty(mNoticeGym.getGymId())
                && !StringUtils.isEmpty(mNoticeGym.getName())) {
            mGym = mNoticeGym;
            gymId = mNoticeGym.getGymId();
            gymName = mNoticeGym.getName();
        }
        setHomeTitle();
        if (showDefaultGymDialog()) {
            setHomeMenuReadNotice();
        }
    }

    /**
     * 弹出默认场馆的对话框
     */
    private boolean showDefaultGymDialog() {
        //无卡，定位失败，并且是默认场馆 ,没有弹出过 满足以上4各条件弹出
        if (!LikingPreference.getUserHasCard() && NumberConstantUtil.ONE == defaultGym && shoDefaultDialog) {
            if (!isWhetherLocation) {//定位失败
                shoDefaultDialog = false;
                setDefaultGymDialog(getString(R.string.current_default_gym) + "\n" + "      " + getString(R.string.please_hand_change_gym), true);
            } else {//定位成功，但是定位所在的城市不再我们开通的城市范围内
                if (!CityUtils.isDredge(cityCode)) {
                    shoDefaultDialog = false;
                    setDefaultGymDialog(getString(R.string.current_default_gym_no_gym) + "\n" + getString(R.string.current_default_gym_location) + "\n" + getString(R.string.please_hand_change_gym), false);
                }
            }
            return false;
        }
        return true;
    }




    public void onEvent(BuyCardMessage message) {
        if (message != null && fragmentTabHost != null) {
            fragmentTabHost.setCurrentTab(NumberConstantUtil.ONE);
        }
    }

    /**
     * 设置首页标题
     */
    private void setHomeTitle() {
        if (mGym != null && !StringUtils.isEmpty(mGym.getCityName())) {
            mLikingLeftTitleTextView.setText(mGym.getCityName());
        }
        String tag = fragmentTabHost.getCurrentTabTag();
        if (tag.equals(TAG_MAIN_TAB) || tag.equals(TAG_RECHARGE_TAB)) {//如果是首页
            if (EnvironmentUtils.Network.isNetWorkAvailable()) {
                if (mGym != null && !StringUtils.isEmpty(mGym.getName())) {
                    mLikingDistanceTextView.setVisibility(View.VISIBLE);
                    mLikingDistanceTextView.setText(mGym.getDistance());
                    mLikingMiddleTitleTextView.setText(mGym.getName());
                } else {//当一个（上海）地区所有的店铺关闭时，而它定位在某个地区（上海），后台返回的场馆数据为空
                    mLikingMiddleTitleTextView.setText("");
                    mLikingDistanceTextView.setVisibility(View.GONE);
                }
            } else {
                setNotNetWorkMiddleView();
            }
        } else if (tag.equals(TAG_MY_TAB)) {//我的
            setTagMyTab();
        }
    }

    /**
     * 设置没有网络是中间view的显示
     */
    private void setNotNetWorkMiddleView() {
        isWhetherLocation = false;
        mLikingMiddleTitleTextView.setText(R.string.title_network_contact_fail);
        mLikingDistanceTextView.setVisibility(View.GONE);
    }

    /**
     * 设置是否读取过公告
     */
    private void setHomeMenuReadNotice() {
        String tag = fragmentTabHost.getCurrentTabTag();
        if (tag.equals(TAG_MAIN_TAB)) {
            if (mNoticeGym != null && !StringUtils.isEmpty(mNoticeGym.getAnnouncementId())) {
                if (LikingPreference.isIdenticalAnnouncement(mNoticeGym.getAnnouncementId())) {
                    mRedPoint.setVisibility(View.VISIBLE);
                    RightMenuDialog.setRedPromptShow(true);
                    showNoticeDialog();
                } else {
                    mRedPoint.setVisibility(View.GONE);
                    RightMenuDialog.setRedPromptShow(false);
                }
            } else {
                mRedPoint.setVisibility(View.GONE);
                RightMenuDialog.setRedPromptShow(false);
            }
        } else {
            mRedPoint.setVisibility(View.GONE);
            RightMenuDialog.setRedPromptShow(false);
        }
    }

    public void onEvent(LikingHomeNoNetWorkMessage message) {
        initTitleLocation();
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

    public void onEvent(LikingHomeActivityMessage message){
        switch (message.what) {
            case LikingHomeActivityMessage.SHOW_PUSH_DIALOG:
                mPresenter.showPushDialog();
                break;
        }
    }


}
