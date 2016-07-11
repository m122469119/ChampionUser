package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.adapter.SelectCityAdapter;
import com.goodchef.liking.eventmessages.JumpToDishesDetailsMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.UserCityIdMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.fragment.LikingNearbyFragment;
import com.goodchef.liking.eventmessages.RefreshChangeDataMessage;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

import java.util.ArrayList;
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

    private TextView mLikingLeftTitleTextView;
    private TextView mLikingMiddleTitleTextTextView;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private TextView mLikingRightTitleTextView;
    private AppBarLayout mAppBarLayout;
    private ImageView mMiddleImageView;
    public TextView mShoppingCartNumTextView;

    private FragmentTabHost fragmentTabHost;
    private AmapGDLocation mAmapGDLocation;

    private double mLongitude;
    private double mLatitude;
    private String mCityId;
    private String mDistrictId;

    LikingNearbyFragment mLikingNearbyFragment = LikingNearbyFragment.newInstance();
    private ArrayList<Food> buyList = new ArrayList<>();
    private String mUserCityId;
    private SelectCityAdapter mSelectCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liking_home);
        setTitle(R.string.activity_liking_home);
        initViews();
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
        mShoppingCartNumTextView = (TextView) findViewById(R.id.tv_shopping_cart_num);
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
                , mLikingNearbyFragment.getClass(), null);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        mLikingLeftTitleTextView.setText("上海市");
        mLikingRightTitleTextView.setVisibility(View.VISIBLE);
        mLikingRightTitleTextView.setText("开门");
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
                    mLikingRightTitleTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setText("开门");
                    mLikingLeftTitleTextView.setText("上海市");
                    mRightImageView.setVisibility(View.GONE);
                    mShoppingCartNumTextView.setVisibility(View.GONE);
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
                    if (calcDishSize() > 0) {
                        mShoppingCartNumTextView.setVisibility(View.VISIBLE);
                        mShoppingCartNumTextView.setText(calcDishSize() + "");
                    } else {
                        mShoppingCartNumTextView.setVisibility(View.GONE);
                    }
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
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                } else if (tabId.equals(TAG_MY_TAB)) {
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    mLikingLeftTitleTextView.setVisibility(View.INVISIBLE);
                    mLeftImageView.setVisibility(View.INVISIBLE);
                    mMiddleImageView.setVisibility(View.GONE);
                    mLikingMiddleTitleTextTextView.setVisibility(View.VISIBLE);
                    mLikingRightTitleTextView.setVisibility(View.INVISIBLE);
                    mLikingMiddleTitleTextTextView.setText(R.string.tab_liking_home_my);
                    mRightImageView.setVisibility(View.GONE);
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        String tag = fragmentTabHost.getCurrentTabTag();
        if (v == mLikingLeftTitleTextView || v == mLeftImageView) {
            if (tag.equals(TAG_MAIN_TAB)) {
                showSelectDialog();
            }
        } else if (v == mLikingRightTitleTextView) {
            if (tag.equals(TAG_MAIN_TAB)) {
                Intent intent = new Intent(this, OpenTheDoorActivity.class);
                startActivity(intent);
            } else if (tag.equals(TAG_RECHARGE_TAB)) {
                Intent intent = new Intent(this, LookStoreMapActivity.class);
                startActivity(intent);
            }
        } else if (v == mRightImageView) {
            if (tag.equals(TAG_NEARBY_TAB)) {
                if (buyList != null && buyList.size() > 0 && calcDishSize() > 0) {
                    Intent intent = new Intent(this, ShoppingCartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(INTENT_KEY_BUY_LIST, buyList);
                    intent.putExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, INTENT_REQUEST_CODE_SHOP_CART);
                } else {
                    PopupUtils.showToast("您还没有购买任何营养餐");
                }
            }
        }
    }

    /**
     * 展示选择城市dialog
     */
    private void showSelectDialog() {
        final HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_city, null, false);
        TextView locationAddress = (TextView) view.findViewById(R.id.dialog_location_address);
        TextView getCityBtn = (TextView) view.findViewById(R.id.get_city_btn);
        ListView mCityListView = (ListView) view.findViewById(R.id.city_listView);
        setCityData(mCityListView, builder);
        builder.setCustomView(view);
        builder.setPositiveButton("查看场馆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LikingHomeActivity.this, LookStoreMapActivity.class);
                startActivity(intent);
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
        List<CityData> cityDataList = Preference.getBaseConfig().getBaseConfigData().getCityList();
        if (cityDataList != null && cityDataList.size() > 0) {
            for (CityData cityData : cityDataList) {
                if (cityData.getCityName().contains("上海市")) {
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
                            mLikingLeftTitleTextView.setText(cityData.getCityName());
                            //重新发送请求
                            builder.create().dismiss();
                        }
                    }
                }
            });
            mSelectCityAdapter.setData(cityDataList);
            mCityListView.setAdapter(mSelectCityAdapter);
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
                if (object == null || object.getErrorCode() != 0) {
                    LiKingVerifyUtils.initApi(LikingHomeActivity.this);
                    return;
                }
                LogUtils.d(TAG, "city: " + object.getCity() + " city code: " + object.getCityCode());
                LogUtils.d(TAG, "longitude:" + object.getLongitude() + "Latitude" + object.getLatitude());
                //  mTitleTextView.setText(StringUtils.isEmpty(locationAddress) ? getString(R.string.location_error) : object.getPoiName());
                //  updateLocationPoint(CityUtils.getCityId(object.getProvince(), object.getCity()), CityUtils.getDistrictId(object.getDistrict()), object.getLongitude(), object.getLatitude());
                updateLocationPoint(object.getProvince(), object.getDistrict(), object.getLongitude(), object.getLatitude(),object.getCity());
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

    private void updateLocationPoint(String cityId, String districtId, double longitude, double latitude,String cityName) {
        mLongitude = longitude;
        mLatitude = latitude;
        mCityId = cityId;
        mDistrictId = districtId;
        postEvent(new MainAddressChanged(longitude, latitude, cityId, districtId,cityName));
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

    @Override
    public void onShoppingDishAdded(Food foodData) {
        int riceNum = foodData.getSelectedOrderNum();
        if (riceNum >= foodData.getRestStock()) {
            PopupUtils.showToast("单个最多只能购买" + foodData.getRestStock() + "份");
        }

        if (buyList != null && buyList.size() > 0) {
            boolean isBuyListExits = false;
            for (int i = 0; i < buyList.size(); i++) {
                if (buyList.get(i).getGoodsId().equals(foodData.getGoodsId())) {
                    buyList.get(i).setSelectedOrderNum(foodData.getSelectedOrderNum());
                    isBuyListExits = true;
                    break;
                }
            }
            if (!isBuyListExits) {
                buyList.add(foodData);
            }

        } else {
            buyList.add(foodData);
        }


//        if (!buyList.contains(foodData)) {
//            buyList.add(foodData);
//        }
        if (calcDishSize() > 0) {
            mShoppingCartNumTextView.setVisibility(View.VISIBLE);
        }
        mShoppingCartNumTextView.setText(calcDishSize() + "");
    }

    @Override
    public void onShoppingDishRemove(Food foodData) {
        if (buyList != null && buyList.size() > 0) {
            for (int i = 0; i < buyList.size(); i++) {
                if (buyList.get(i).getGoodsId().equals(foodData.getGoodsId())) {
                    buyList.get(i).setSelectedOrderNum(foodData.getSelectedOrderNum());
                    if (buyList.get(i).getSelectedOrderNum() == 0) {
                        buyList.remove(buyList.get(i));
                    }
                }
            }
        }

//        if (foodData.getSelectedOrderNum() == 0) {//当
//            buyList.remove(foodData);
//        }
        if (calcDishSize() == 0) {
            mShoppingCartNumTextView.setVisibility(View.GONE);
        }
        mShoppingCartNumTextView.setText(calcDishSize() + "");
    }

    private int calcDishSize() {
        int num = 0;
        if (!ListUtils.isEmpty(buyList)) {
            for (Food data : buyList) {
                int n = data.getSelectedOrderNum();
                num += n;
            }
        }
        return num;
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UserCityIdMessage userCityIdMessage) {
        mUserCityId = userCityIdMessage.getUserCityId();
    }

    public void onEvent(JumpToDishesDetailsMessage jumpToDishesDetailsMessage) {
        String mUserCityId = jumpToDishesDetailsMessage.getUserCityId();
        Food foodData = jumpToDishesDetailsMessage.getFoodData();
        Intent intent = new Intent(this, DishesDetailsActivity.class);
        intent.putExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_KEY_FOOD_OBJECT, foodData);
        bundle.putParcelableArrayList(INTENT_KEY_BUY_LIST, buyList);
        intent.putExtras(bundle);
        startActivityForResult(intent, INTENT_REQUEST_CODE_DISHES_DETIALS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_SHOP_CART) {//从购物车回来时，带回购物车数据，从新计算购物车数量
                boolean isClearCart = data.getBooleanExtra(ShoppingCartActivity.KEY_CLEAR_CART, false);
                Bundle bundle = data.getExtras();
                buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
                if (isClearCart){//如果是清空购物车，清除购买集合
                    buyList.clear();
                }
                postEvent(new RefreshChangeDataMessage(buyList, isClearCart));
                if (calcDishSize() > 0) {
                    mShoppingCartNumTextView.setVisibility(View.VISIBLE);
                    mShoppingCartNumTextView.setText(calcDishSize() + "");
                } else {
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                }
            } else if (requestCode == INTENT_REQUEST_CODE_DISHES_DETIALS) {//从单个商品详情回来，带回购买数据集合，从新计算购物车数量
                Bundle bundle = data.getExtras();
                buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
                postEvent(new RefreshChangeDataMessage(buyList, false));
                if (calcDishSize() > 0) {
                    mShoppingCartNumTextView.setVisibility(View.VISIBLE);
                    mShoppingCartNumTextView.setText(calcDishSize() + "");
                } else {
                    mShoppingCartNumTextView.setVisibility(View.GONE);
                }
            }
        }
    }
}
