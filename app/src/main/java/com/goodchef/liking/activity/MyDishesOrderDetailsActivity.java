package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.MyDishesDetailsMenuAdapter;
import com.goodchef.liking.fragment.MyDishesOrderFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyDishesOrderDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午5:51
 */
public class MyDishesOrderDetailsActivity extends AppBarActivity {
    private static final int ORDER_STATE_SUBMIT = 0;//0:已提交
    private static final int ORDER_STATE_PAYED = 1;//1:已支付
    private static final int ORDER_STATE_CANCEL = 2;// 2:已取消
    private static final int ORDER_STATE_GET_DEISHES = 3; //3:已取餐

    private TextView mSerialNumberTextView;//流水号
    private TextView mOrderNumberTextView;//订单号
    private TextView mOrderStateTextView;//状态
    private RecyclerView mMenuRecyclerView;
    private RelativeLayout mCouponLayout;
    private TextView mCouponAmount;//优惠券金额
    private TextView mActualDelivery;//实际支付
    private TextView mPayType;//支付方式
    private TextView mUsername;//姓名
    private TextView mUserPhone;//手机
    private TextView mMealShop;//门店名称
    private TextView mMealShopAddress;//门店地址

   private TextView mPaySurplusTimeTextView;//剩余支付时间
    private TextView mGoPayBtn;//去支付
    private TextView mCancelOrderBtn;//取消
    private TextView mConfirmGetDishesBtn;//确认点餐
    private RelativeLayout mPayLayout;//支付布局

    private String orderId;
    private MyDishesDetailsMenuAdapter mMyDishesDetailsMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dishes_order_details);
        setTitle(getString(R.string.title_dishes_details));
        initView();
        initData();
    }

    private void initView() {
        mSerialNumberTextView = (TextView) findViewById(R.id.details_serial_number);
        mOrderNumberTextView = (TextView) findViewById(R.id.details_order_number);
        mOrderStateTextView = (TextView) findViewById(R.id.details_order_state);
        mMenuRecyclerView = (RecyclerView) findViewById(R.id.dishes_menu_recyclerView);
        mCouponLayout = (RelativeLayout) findViewById(R.id.layout_details_coupons);
        mCouponAmount = (TextView) findViewById(R.id.details_coupon_amount);
        mActualDelivery = (TextView) findViewById(R.id.actual_delivery);
        mPayType = (TextView) findViewById(R.id.details_pay_type);
        mUsername = (TextView) findViewById(R.id.eating_user_name);
        mUserPhone = (TextView) findViewById(R.id.eating_user_phone);
        mMealShop = (TextView) findViewById(R.id.details_get_meals_shop);
        mMealShopAddress = (TextView) findViewById(R.id.details_get_meals_address);

        mPaySurplusTimeTextView = (TextView) findViewById(R.id.details_pay_surplus_time);
        mGoPayBtn = (TextView) findViewById(R.id.details_go_pay);
        mCancelOrderBtn = (TextView) findViewById(R.id.details_cancel_order);
        mConfirmGetDishesBtn = (TextView) findViewById(R.id.details_confirm_get_dishes_btn);
        mPayLayout = (RelativeLayout) findViewById(R.id.layout_details_order_pay);
    }

    private void initData() {
        orderId = getIntent().getStringExtra(MyDishesOrderFragment.INTENT_KEY_ORDER_ID);
        sendDetailsRequest();
    }

    private void sendDetailsRequest() {
        LiKingApi.getDishesDetails(Preference.getToken(), orderId, new RequestUiLoadingCallback<MyDishesOrderDetailsResult>(this, R.string.loading_data) {
            @Override
            public void onSuccess(MyDishesOrderDetailsResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(MyDishesOrderDetailsActivity.this, result)) {
                    setDetailsData(result.getOrderDetailsData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    private void setDetailsData(MyDishesOrderDetailsResult.OrderDetailsData detailsData) {
        mSerialNumberTextView.setText("流水号：" + detailsData.getSerialNumber());
        mOrderNumberTextView.setText("订单号：" + detailsData.getOrderId());
        int state = detailsData.getOrderStatus();
        setOrderState(state);
        List<MyDishesOrderDetailsResult.OrderDetailsData.FoodListData> menuList = detailsData.getFoodList();
        if (menuList != null && menuList.size() > 0) {
            setDishesMenuList(menuList);
        }

        String couponAmount = detailsData.getCouponAmount();
        if (Double.parseDouble(couponAmount) > 0) {
            mCouponLayout.setVisibility(View.VISIBLE);
            mCouponAmount.setText("¥ " + couponAmount);
        } else {
            mCouponLayout.setVisibility(View.GONE);
        }

        mActualDelivery.setText("¥ " + detailsData.getActualAmount());

        int payType = detailsData.getPayType();
        if (payType == 0) {
            mPayType.setText(R.string.pay_wechat_type);
        } else if (payType == 1) {
            mPayType.setText(R.string.pay_alipay_type);
        } else if (payType == 3) {
            mPayType.setText(R.string.pay_type_free_of);
        }

        mUsername.setText(detailsData.getName());
        mUserPhone.setText(detailsData.getPhone());
        mMealShop.setText(detailsData.getGymName());
        mMealShopAddress.setText(detailsData.getGymAddress());
    }

    private void setOrderState(int state) {
        if (state == ORDER_STATE_SUBMIT) {//已提交
            mConfirmGetDishesBtn.setVisibility(View.GONE);
            mPayLayout.setVisibility(View.VISIBLE);
            mOrderStateTextView.setText(R.string.dishes_order_state_submit);
        } else if (state == ORDER_STATE_PAYED) {//已支付
            mConfirmGetDishesBtn.setVisibility(View.VISIBLE);
            mPayLayout.setVisibility(View.GONE);
            mOrderStateTextView.setText(R.string.dishes_order_state_payed);
        } else if (state == ORDER_STATE_CANCEL) {//已取消
            mConfirmGetDishesBtn.setVisibility(View.GONE);
            mPayLayout.setVisibility(View.GONE);
            mOrderStateTextView.setText(R.string.dishes_order_state_cancel);
        } else if (state == ORDER_STATE_GET_DEISHES) {//已完成
            mConfirmGetDishesBtn.setVisibility(View.GONE);
            mPayLayout.setVisibility(View.GONE);
            mOrderStateTextView.setText(R.string.dishes_order_state_complete);
        }
    }


    private void setDishesMenuList(List<MyDishesOrderDetailsResult.OrderDetailsData.FoodListData> list) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMenuRecyclerView.setLayoutManager(mLayoutManager);
        mMyDishesDetailsMenuAdapter = new MyDishesDetailsMenuAdapter(this);
        mMyDishesDetailsMenuAdapter.setData(list);
        mMenuRecyclerView.setAdapter(mMyDishesDetailsMenuAdapter);
    }

}
