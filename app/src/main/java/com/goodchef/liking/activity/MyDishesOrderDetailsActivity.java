package com.goodchef.liking.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.MyDishesDetailsMenuAdapter;
import com.goodchef.liking.eventmessages.CancelMyDishesOrderMessage;
import com.goodchef.liking.eventmessages.CompleteMyDishesOrderMessage;
import com.goodchef.liking.eventmessages.MyDishesDetailsWechatMessage;
import com.goodchef.liking.eventmessages.MyDishesOrderAlipayMessage;
import com.goodchef.liking.fragment.MyDishesOrderFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.MyDishesOrderDetailsResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyDishesOrderPresenter;
import com.goodchef.liking.mvp.view.MyDishesOrderView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.widgets.dialog.SelectPayTypeCustomDialog;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.Date;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午5:51
 */
public class MyDishesOrderDetailsActivity extends AppBarActivity implements MyDishesOrderView, View.OnClickListener {
    private static final int ORDER_STATE_SUBMIT = 0;//0:已提交
    private static final int ORDER_STATE_PAYED = 1;//1:已支付
    private static final int ORDER_STATE_CANCEL = 2;// 2:已取消
    private static final int ORDER_STATE_GET_DEISHES = 3; //3:已取餐
    private static final int PAY_TYPE_WECHAT = 0;
    private static final int PAY_TYPE_ALIPY = 1;
    private static final int PAY_TYPE_FREE = 3;


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
    private TextView mOrderTimeTextView;//下单时间

    private TextView mPaySurplusTimeTextView;//剩余支付时间
    private TextView mGoPayBtn;//去支付
    private TextView mCancelOrderBtn;//取消
    private TextView mConfirmGetDishesBtn;//确认点餐
    private RelativeLayout mPayLayout;//支付布局

    private String orderId;
    private MyDishesDetailsMenuAdapter mMyDishesDetailsMenuAdapter;
    private MyDishesOrderPresenter mMyDishesOrderPresenter;

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private OrderSurplusCountDownTimer mCountDownTimer;//倒计时
    private LikingStateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dishes_order_details);
        setTitle(getString(R.string.title_dishes_details));
        initView();
        initData();
        initPayModule();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_dishes_order_details_state_view);
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
        mOrderTimeTextView = (TextView) findViewById(R.id.details_order_time);

        mPaySurplusTimeTextView = (TextView) findViewById(R.id.details_pay_surplus_time);
        mGoPayBtn = (TextView) findViewById(R.id.details_go_pay);
        mCancelOrderBtn = (TextView) findViewById(R.id.details_cancel_order);
        mConfirmGetDishesBtn = (TextView) findViewById(R.id.details_confirm_get_dishes_btn);
        mPayLayout = (RelativeLayout) findViewById(R.id.layout_details_order_pay);

        mGoPayBtn.setOnClickListener(this);
        mCancelOrderBtn.setOnClickListener(this);
        mConfirmGetDishesBtn.setOnClickListener(this);
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendDetailsRequest();
            }
        });
    }

    private void initData() {
        orderId = getIntent().getStringExtra(MyDishesOrderFragment.INTENT_KEY_ORDER_ID);
        sendDetailsRequest();
        mMyDishesOrderPresenter = new MyDishesOrderPresenter(this, this);
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }


    private void sendDetailsRequest() {
        LiKingApi.getDishesDetails(Preference.getToken(), orderId, new RequestCallback<MyDishesOrderDetailsResult>() {
            @Override
            public void onSuccess(MyDishesOrderDetailsResult result) {
                if (LiKingVerifyUtils.isValid(MyDishesOrderDetailsActivity.this, result)) {
                    setDetailsData(result.getOrderDetailsData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mStateView.setState(StateView.State.FAILED);
            }
        });
    }

    private void setDetailsData(MyDishesOrderDetailsResult.OrderDetailsData detailsData) {
        if (detailsData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mSerialNumberTextView.setText(getString(R.string.serial_number) + detailsData.getSerialNumber());
            mOrderNumberTextView.setText(getString(R.string.order_number) + detailsData.getOrderId());
            //设置购买的食品清单
            List<MyDishesOrderDetailsResult.OrderDetailsData.FoodListData> menuList = detailsData.getFoodList();
            if (menuList != null && menuList.size() > 0) {
                setDishesMenuList(menuList);
            }
            //设置优惠券
            String couponAmount = detailsData.getCouponAmount();
            if (Double.parseDouble(couponAmount) > 0) {
                mCouponLayout.setVisibility(View.VISIBLE);
                mCouponAmount.setText(getString(R.string.money_symbol) + couponAmount);
            } else {
                mCouponLayout.setVisibility(View.GONE);
            }
            //设置总价
            mActualDelivery.setText(getString(R.string.money_symbol) + detailsData.getActualAmount());
            //设置支付方式
            int payType = detailsData.getPayType();
            if (payType == PAY_TYPE_WECHAT) {
                mPayType.setText(R.string.pay_wechat_type);
            } else if (payType == PAY_TYPE_ALIPY) {
                mPayType.setText(R.string.pay_alipay_type);
            } else if (payType == PAY_TYPE_FREE) {
                mPayType.setText(R.string.pay_type_free_of);
            }

            mUsername.setText(detailsData.getName());
            mUserPhone.setText(detailsData.getPhone());
            mMealShop.setText(detailsData.getGymName());
            mMealShopAddress.setText(detailsData.getGymAddress());
            mOrderTimeTextView.setText(detailsData.getOrderTime());

            //处理倒计时的
            long serviceTime = DateUtils.currentDataSeconds() + LiKingApi.sTimestampOffset;//服务器当前时间
            String orderTime = detailsData.getOrderTime();//下单时间String类型,
            Date oderDate = DateUtils.parseString("yyyy-MM-dd HH:mm:ss", orderTime);//转换为时间格式
            long orderDateLong = oderDate.getTime() / 1000;//将订单时间转换为s
            long orderOverdueTime = orderDateLong + getLimitTime();//订单过期时间 = 下单时间+限时时间
            long orderSurplusTime = orderOverdueTime - serviceTime;// 订单剩余时间 = 服务器订单过期时间-服务器当前时间
            long orderSurplusData = Math.abs(orderSurplusTime);

            int state = detailsData.getOrderStatus();
            setOrderState(state, (int) orderSurplusData);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    private Long getLimitTime() {
        long limitTime;
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                String limitTimeStr = baseConfigData.getCountSecond();
                if (StringUtils.isEmpty(limitTimeStr)) {
                    limitTime = (long) 300;
                } else {
                    limitTime = Long.parseLong(limitTimeStr);
                }
            } else {
                limitTime = (long) 300;
            }
        } else {
            limitTime = (long) 300;
        }
        return limitTime;
    }

    private void setOrderState(int state, int orderSurplusTime) {
        if (state == ORDER_STATE_SUBMIT) {//已提交
            mConfirmGetDishesBtn.setVisibility(View.GONE);
            mPayLayout.setVisibility(View.VISIBLE);
            mOrderStateTextView.setText(R.string.dishes_order_state_submit);
            if (orderSurplusTime > 0) {
                mCountDownTimer = new OrderSurplusCountDownTimer(orderSurplusTime * 1000, 1000);
                mCountDownTimer.start();
            }
        } else if (state == ORDER_STATE_PAYED) {//已支付
            cancelCountDownTime();
            mConfirmGetDishesBtn.setVisibility(View.VISIBLE);
            mPayLayout.setVisibility(View.GONE);
            mOrderStateTextView.setText(R.string.dishes_order_state_payed);
        } else if (state == ORDER_STATE_CANCEL) {//已取消
            cancelCountDownTime();
            mConfirmGetDishesBtn.setVisibility(View.GONE);
            mPayLayout.setVisibility(View.GONE);
            mOrderStateTextView.setText(R.string.dishes_order_state_cancel);
        } else if (state == ORDER_STATE_GET_DEISHES) {//已完成
            cancelCountDownTime();
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

    @Override
    public void onClick(View v) {
        if (v == mGoPayBtn) {
            showPayDialog(orderId);
        } else if (v == mCancelOrderBtn) {
            sendCancelOrderRequest(orderId);
        } else if (v == mConfirmGetDishesBtn) {
            sendCompleteOrderRequest(orderId);
        }
    }

    private void sendCompleteOrderRequest(String orderId) {
        mMyDishesOrderPresenter.completeDishesOrder(orderId);
    }

    private void sendCancelOrderRequest(String orderId) {
        mMyDishesOrderPresenter.cancelMyDishesOrder(orderId);
    }


    private void showPayDialog(final String orderId) {
        final SelectPayTypeCustomDialog customDialog = new SelectPayTypeCustomDialog(this, "");
        customDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.alipay_pay_layout) {//支付宝支付
                    sendGoPayRequest(orderId, "1");
                    customDialog.dismiss();
                } else if (v.getId() == R.id.wechat_pay_layout) {//微信支付
                    sendGoPayRequest(orderId, "0");
                    customDialog.dismiss();
                }
            }
        });
    }

    private void sendGoPayRequest(String payType, String orderId) {
        mMyDishesOrderPresenter.myDishesOrderPay(payType, orderId);
    }

    @Override
    public void updateMyDishesPayView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == 3) {//3 免金额支付
            PopupUtils.showToast(R.string.pay_success);
            sendDetailsRequest();
        } else {
            handlePay(payResultData);
        }
    }

    @Override
    public void updateCancelDishesOrder() {
        sendDetailsRequest();
        postEvent(new CancelMyDishesOrderMessage());
    }

    @Override
    public void updateCompleteDishesOrder() {
        sendDetailsRequest();
        postEvent(new CompleteMyDishesOrderMessage());
    }

    private void handlePay(PayResultData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_MY_DISHES_DETAILS;
                WXPayEntryActivity.orderId = data.getOrderId();
                mWeixinPay.setPrePayId(data.getWxPrepayId());
                mWeixinPay.doPay();
                break;
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
            sendDetailsRequest();
            postEvent(new MyDishesOrderAlipayMessage());
        }

        @Override
        public void onFailure(String errorMessage) {

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

    public void onEvent(MyDishesDetailsWechatMessage wechatMessage) {
        sendDetailsRequest();
    }

    class OrderSurplusCountDownTimer extends CountDownTimer {

        public OrderSurplusCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String str = DateUtils.formatTime(millisUntilFinished);
            mPaySurplusTimeTextView.setText(getString(R.string.surplus_pay_time) + str);
        }

        @Override
        public void onFinish() {
            setOrderState(2, 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountDownTime();
    }

    private void cancelCountDownTime() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

}
