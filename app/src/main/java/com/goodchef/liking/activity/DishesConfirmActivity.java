package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.DishesConfirmAdapter;
import com.goodchef.liking.adapter.MealTimeAdapter;
import com.goodchef.liking.eventmessages.DishesAliPayMessage;
import com.goodchef.liking.eventmessages.DishesPayFalse;
import com.goodchef.liking.eventmessages.DishesWechatPayMessage;
import com.goodchef.liking.eventmessages.FreePayMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.LikingNutrimealFragment;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.GymListResult;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.result.data.MealTimeData;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.mvp.presenter.NutritionMealConfirmPresenter;
import com.goodchef.liking.mvp.view.NutritionMealConfirmView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:确认订单页
 * Author shaozucheng
 * Time:16/6/23 下午6:14
 */
public class DishesConfirmActivity extends AppBarActivity implements View.OnClickListener, NutritionMealConfirmView {
    private static final int INTENT_REQUEST_CODE_COUPON = 112;
    public static final int INTENT_REQUEST_CODE_CHANGE_SHOP = 113;
    private TextView mChangeShopTextView;
    private TextView mMealsAddressTextView;
    private TextView mDishesCouponMoney;

    private RelativeLayout mMealtimeLayout;//选择取餐时间
    private TextView mGetMealsTimeTextView;//取餐时间
    private RelativeLayout mCouponsLayout;//优惠券列表
    private TextView mCouponTitleTextView;//优惠券

    //支付相关
    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private RecyclerView mRecyclerView;
    private TextView mDishesMoneyextView;//总价钱
    private TextView mImmediatelyPayBtn;//立即支付

    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象

    private NutritionMealConfirmPresenter mNutritionMealConfirmPresenter;

    private String totalAmount;//总金额
    private String mUserCityId;//城市id

    private ArrayList<Food> confirmBuyList;
    private List<MealTimeData> mealTimeList;//取餐时间集合

    private String mSelectMealtime;//取餐时间
    private DishesConfirmAdapter mDishesConfirmAdapter;

    private GymListResult.GymData.Shop myShop;//门店对象

    private String gymId;//门店id
    private String payType = "-1";//支付方式
    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_confirm);
        setTitle(getString(R.string.title_dishes_confirm_order));
        initView();
        setViewOnClickListener();
        initData();
        initPayModule();
        setPayDefultType();
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void initView() {
        mChangeShopTextView = (TextView) findViewById(R.id.change_shop);
        mMealsAddressTextView = (TextView) findViewById(R.id.having_meals_address);

        mMealtimeLayout = (RelativeLayout) findViewById(R.id.layout_get_meals_time);
        mGetMealsTimeTextView = (TextView) findViewById(R.id.get_meals_time);
        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCouponTitleTextView = (TextView) findViewById(R.id.select_coupon_title);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_order_dishes);
        mDishesMoneyextView = (TextView) findViewById(R.id.dishes_money);
        mDishesCouponMoney = (TextView) findViewById(R.id.dishes_coupon_money);
        mImmediatelyPayBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {
        mChangeShopTextView.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyPayBtn.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mMealtimeLayout.setOnClickListener(this);
    }

    private void initData() {
        mNutritionMealConfirmPresenter = new NutritionMealConfirmPresenter(this, this);

        mUserCityId = getIntent().getStringExtra(LikingNutrimealFragment.INTENT_KEY_USER_CITY_ID);
        Bundle bundle = getIntent().getExtras();
        confirmBuyList = bundle.getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);
        if (confirmBuyList != null && confirmBuyList.size() > 0) {
            setConfirmListView();
            setNumAndPrice();
        }
        sendRequest();
    }

    /**
     * 设置默认支付方式
     */
    private void setPayDefultType() {
        mAlipayCheckBox.setChecked(true);
        mWechatCheckBox.setChecked(false);
        payType = "1";
    }

    /**
     * 计算并显示选择的菜品份数和总价格
     */
    private void setNumAndPrice() {
        int num = 0;
        float dishPrice = 0;
        if (!ListUtils.isEmpty(confirmBuyList)) {
            for (Food data : confirmBuyList) {
                int n = data.getSelectedOrderNum();
                float p = Float.parseFloat(data.getPrice()) * n;
                num += n;
                dishPrice += p;
            }
        }
        mDishesMoneyextView.setText(getString(R.string.money_symbol) + dishPrice);
    }


    private void setConfirmListView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDishesConfirmAdapter = new DishesConfirmAdapter(this);
        mDishesConfirmAdapter.setData(confirmBuyList);
        mRecyclerView.setAdapter(mDishesConfirmAdapter);
    }

    private void sendRequest() {
        if (confirmBuyList != null && confirmBuyList.size() > 0) {
            String confirmString = createDishesJson();
            mNutritionMealConfirmPresenter.confirmFood(mUserCityId, confirmString);
        }
    }

    private String createDishesJson() {
        StringBuilder builder = new StringBuilder("{");
        for (Food food : confirmBuyList) {
            builder.append("\"").append(food.getGoodsId()).append("\":");
            builder.append(food.getSelectedOrderNum()).append(",");
        }
        builder.replace(builder.length() - 1, builder.length(), "}");
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        if (v == mChangeShopTextView) {//切换门店
            Intent intent = new Intent(this, ChangeShopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST, confirmBuyList);
            intent.putExtra(LikingNutrimealFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
            bundle.putSerializable(ChangeShopActivity.INTENT_KEY_SHOP_OBJECT, myShop);
            intent.putExtras(bundle);
            startActivityForResult(intent, INTENT_REQUEST_CODE_CHANGE_SHOP);
        } else if (v == mMealtimeLayout) {//选择取餐时间
            if (mealTimeList != null && mealTimeList.size() > 0) {
                showMealTimeDialog();
            } else {
                PopupUtils.showToast(getString(R.string.no_time_get_dinner));
            }
        } else if (v == mCouponsLayout) {//选择优惠券
            UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.KEY_COURSE_ID, "");
            intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "DishesConfirmActivity");
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, LikingHomeActivity.gymId);
            if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
                intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCoupon_code());
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST, confirmBuyList);
            intent.putExtras(bundle);
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        } else if (v == mAlipayLayout) {
            mAlipayCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
            payType = "1";
        } else if (v == mWechatLayout) {
            mAlipayCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
            payType = "0";
        } else if (v == mImmediatelyPayBtn) {
            String mealTime = mGetMealsTimeTextView.getText().toString();
            if (StringUtils.isEmpty(mealTime)) {
                PopupUtils.showToast(getString(R.string.select_dinner_time));
                return;
            }
            if (payType.equals("-1")) {
                PopupUtils.showToast(getString(R.string.please_select_pay_type));
                return;
            }
            if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
                mNutritionMealConfirmPresenter.submitFoodOrder(gymId, mSelectMealtime, mCoupon.getCoupon_code(), createDishesJson(), payType);
            } else {
                mNutritionMealConfirmPresenter.submitFoodOrder(gymId, mSelectMealtime, null, createDishesJson(), payType);
            }
        }
    }

    @Override
    public void updateNutritionMealConfirmView(NutritionMealConfirmResult.NutritionMealConfirmData confirmData) {
        NutritionMealConfirmResult.NutritionMealConfirmData.Store store = confirmData.getStore();
        mMealsAddressTextView.setText(store.getAddress() + "  " + store.getName());
        totalAmount = confirmData.getTotalAmount();
        List<String> list = confirmData.getSelectDate();
        mealTimeList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                MealTimeData mealTimeData = new MealTimeData();
                mealTimeData.setMealTime(list.get(i));
                if (i == 0) {
                    mealTimeData.setSelect(true);
                } else {
                    mealTimeData.setSelect(false);
                }
                mealTimeList.add(mealTimeData);
            }
            mSelectMealtime = mealTimeList.get(0).getMealTime();
            mGetMealsTimeTextView.setText(mSelectMealtime);
        }
        gymId = confirmData.getStore().getGymId();
    }

    @Override
    public void updateSubmitOrderView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == 3) {//3 免金额支付
            PopupUtils.showToast(getString(R.string.pay_success));
            postEvent(new FreePayMessage());
            jumpIntentDishesOrderList();
        } else {
            handlePay(payResultData);
        }
    }

    private void handlePay(PayResultData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_DISHES_ORDER;
                WXPayEntryActivity.orderId = data.getOrderId();
                mWeixinPay.setPrePayId(data.getWxPrepayId());
                mWeixinPay.doPay();
                break;
        }
    }

    /**
     * 选择取餐时间
     */
    private void showMealTimeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_meal_time, null, false);
        ListView listView = (ListView) view.findViewById(R.id.meal_time_listView);
        final MealTimeAdapter mealTimeAdapter = new MealTimeAdapter(this);
        listView.setAdapter(mealTimeAdapter);
        mealTimeAdapter.setData(mealTimeList);
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setCustomView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mTextView = (TextView) view.findViewById(R.id.select_meal_time);
                if (mTextView != null) {
                    MealTimeData mealTimeData = (MealTimeData) mTextView.getTag();
                    if (mealTimeData != null) {
                        for (MealTimeData data : mealTimeList) {
                            if (data.getMealTime().equals(mealTimeData.getMealTime())) {
                                data.setSelect(true);
                            } else {
                                data.setSelect(false);
                            }
                        }
                        mSelectMealtime = mealTimeData.getMealTime();
                        mealTimeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGetMealsTimeTextView.setText(mSelectMealtime);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_COUPON) {//选择优惠券
                mCoupon = (CouponsResult.CouponData.Coupon) data.getSerializableExtra(CouponsActivity.INTENT_KEY_COUPONS_DATA);
                if (mCoupon != null) {
                    handleCoupons(mCoupon);
                }
            } else if (requestCode == INTENT_REQUEST_CODE_CHANGE_SHOP) {//切换门店
                myShop = (GymListResult.GymData.Shop) data.getSerializableExtra(ChangeShopActivity.INTENT_KEY_SHOP_OBJECT);
                if (myShop != null) {
                    mMealsAddressTextView.setText(myShop.getAddress() + " " + myShop.getName());
                    gymId = myShop.getGymId();
                }
            }
        }
    }

    /**
     * 处理优惠券
     */
    private void handleCoupons(CouponsResult.CouponData.Coupon mCoupon) {

        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
        double coursesPrice = Double.parseDouble(totalAmount);//订单的总价
        double couponAmount = Double.parseDouble(couponAmountStr);

        mCouponTitleTextView.setText(mCoupon.getTitle() + mCoupon.getAmount() + getString(R.string.yuan));
        if (coursesPrice >= couponAmount) {
            //订单的价格大于优惠券的面额
            double amount = coursesPrice - couponAmount;
            if (amount >= 0) {
                mDishesCouponMoney.setText(getString(R.string.has_coupons) + mCoupon.getAmount());
                mDishesMoneyextView.setText(getString(R.string.money_symbol) + amount);
            }
        } else {//订单的面额小于优惠券的面额
            mDishesCouponMoney.setText(getString(R.string.has_coupons) + mCoupon.getAmount());
            mDishesMoneyextView.setText(getString(R.string.money_symbol) + "0.00");
        }

    }

    /**
     * 支付宝支付结果处理
     */
    private final OnAliPayListener mOnAliPayListener = new OnAliPayListener() {
        @Override
        public void onStart() {
            LogUtils.e(TAG, "alipay start");
        }

        @Override
        public void onSuccess() {
            postEvent(new DishesAliPayMessage());
            jumpIntentDishesOrderList();
        }

        @Override
        public void onFailure(String errorMessage) {
            postEvent(new DishesPayFalse());
            jumpIntentDishesOrderList();
        }

        @Override
        public void confirm() {
        }
    };

    /**
     * 微信支付结果处理
     */
    private WeixinPayListener mWeixinPayListener = new WeixinPayListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(String errorMessage) {
        }
    };


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(DishesWechatPayMessage wechatMessage) {
        if (wechatMessage.isBuySuccess()) {
            jumpIntentDishesOrderList();
        } else {
            postEvent(new DishesPayFalse());
            jumpIntentDishesOrderList();
        }
    }

    private void jumpIntentDishesOrderList() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra(MyOrderActivity.KEY_CURRENT_INDEX, 0);
        startActivity(intent);
        this.finish();
    }

}
