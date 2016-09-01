package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.web.HDefaultWebActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardRecyclerAdapter;
import com.goodchef.liking.dialog.AnnouncementDialog;
import com.goodchef.liking.eventmessages.BuyCardWeChatMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.mvp.presenter.ConfirmBuyCardPresenter;
import com.goodchef.liking.mvp.view.ConfirmBuyCardView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

/**
 * 说明:确认购买卡
 * Author shaozucheng
 * Time:16/6/17 下午5:55
 */
public class BuyCardConfirmActivity extends AppBarActivity implements View.OnClickListener, ConfirmBuyCardView {
    private static final int INTENT_REQUEST_CODE_COUPON = 101;
    private static final int BUY_TYPE_BUY = 1;//买卡
    private static final int BUY_TYPE_CONTINUE = 2;//续卡
    private static final int BUY_TYPE_UPGRADE = 3;//升级
    public static final String KEY_CARD_ID = "key_card_id";


    private TextView mBuyCardNoticeTextView;//活动
    private TextView mGymNameTextView;//场馆名称
    private TextView mGymAddressTextView;//场馆地址
    private TextView mPeriodOfValidityTextView;//有效期
    private RecyclerView mCardRecyclerView;

    private RelativeLayout mCouponsLayout;
    private TextView mCouponsMoneyTextView;

    //支付相关
    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private LinearLayout mAgreeProtocolTextView;
    private TextView mCardMoneyTextView;//
    private TextView mImmediatelyBuyBtn;//立即支付

    private String mCardName;
    private int mCategoryId;
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象
    private String payType = "-1";//支付方式
    private ConfirmBuyCardPresenter mConfirmBuyCardPresenter;

    private CardRecyclerAdapter mCardRecyclerAdapter;
    private List<ConfirmCard> confirmCardList;


    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private int mCardId;//会员卡ID
    private int buyType; //1 购卡  2 续卡  3 升级卡
    private String cardPrice;//卡的金额
    private LikingStateView mStateView;
    private String explain;
    private String gymId = "0";
    private String noticeActivity;//活动
    private String submitGymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_confirm);
        initView();
        setViewOnClickListener();
        setDefaultPayType();
        initData();
        initPayModule();
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.buy_card_confirm_state_view);
        mBuyCardNoticeTextView = (TextView) findViewById(R.id.buy_card_notice);
        mGymNameTextView = (TextView) findViewById(R.id.gym_name);
        mGymAddressTextView = (TextView) findViewById(R.id.gym_address);

        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);

        mCardRecyclerView = (RecyclerView) findViewById(R.id.card_recyclerView);

        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCouponsMoneyTextView = (TextView) findViewById(R.id.select_coupon_title);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mAgreeProtocolTextView = (LinearLayout) findViewById(R.id.buy_card_agree_protocol);
        mCardMoneyTextView = (TextView) findViewById(R.id.card_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendConfirmCardRequest();
            }
        });
    }

    private void setViewOnClickListener() {
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
        mAgreeProtocolTextView.setOnClickListener(this);
        mBuyCardNoticeTextView.setOnClickListener(this);
    }

    /**
     * 设置默认支付方式
     */
    private void setDefaultPayType() {
        mAlipayCheckBox.setChecked(true);
        mWechatCheckBox.setChecked(false);
        payType = "1";
    }

    private void initData() {
        mCardName = getIntent().getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        mCategoryId = getIntent().getIntExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, 0);
        buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        mCardRecyclerAdapter = new CardRecyclerAdapter(this);
        sendConfirmCardRequest();
    }

    private void sendConfirmCardRequest() {
        mConfirmBuyCardPresenter = new ConfirmBuyCardPresenter(this, this);
        mConfirmBuyCardPresenter.confirmBuyCard(buyType, mCategoryId, gymId);
    }

    @Override
    public void onClick(View v) {
        if (v == mAlipayLayout) {//选择支付宝
            mAlipayCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
            payType = "1";
        } else if (v == mWechatLayout) {//选择微信
            mAlipayCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
            payType = "0";
        } else if (v == mCouponsLayout) {//选优惠券
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
                Intent intent = new Intent(this, CouponsActivity.class);
                intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "BuyCardConfirmActivity");
                intent.putExtra(KEY_CARD_ID, mCardId);
                intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, buyType + "");
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID,submitGymId);
                if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCouponCode())) {
                    intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCouponCode());
                }
                startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mImmediatelyBuyBtn) {
            if (Preference.isLogin()) {
                if (payType.equals("-1")) {
                    PopupUtils.showToast("请选择支付方式");
                    return;
                }
                UMengCountUtil.UmengBtnCount(this, UmengEventId.BUY_CARD_IMMEDIATELY_BUY);
                senSubmitRequest();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mAgreeProtocolTextView) {
            BaseConfigResult baseConfigResult = Preference.getBaseConfig();
            if (baseConfigResult != null) {
                BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                if (baseConfigData != null) {
                    String serviceUrl = baseConfigData.getServiceUrl();
                    if (!StringUtils.isEmpty(serviceUrl)) {
                        HDefaultWebActivity.launch(this, serviceUrl, "平台会员协议");
                    }
                }
            }
        } else if (v == mBuyCardNoticeTextView) {//公告
            final AnnouncementDialog dialog = new AnnouncementDialog(this, noticeActivity);
            dialog.setButtonDismiss();
        }
    }


    private void senSubmitRequest() {
        if (buyType == BUY_TYPE_BUY) {
            submitGymId = gymId;
        } else if (buyType == BUY_TYPE_CONTINUE) {
            submitGymId = "0";
        } else if (buyType == BUY_TYPE_UPGRADE) {
            submitGymId = "0";
        }
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCouponCode())) {
            mConfirmBuyCardPresenter.submitBuyCardData(mCardId, buyType, mCoupon.getCouponCode(), payType, submitGymId);
        } else {
            mConfirmBuyCardPresenter.submitBuyCardData(mCardId, buyType, "", payType, submitGymId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_COUPON) {
                mCoupon = (CouponsResult.CouponData.Coupon) data.getSerializableExtra(CouponsActivity.INTENT_KEY_COUPONS_DATA);
                if (mCoupon != null) {
                    handleCoupons(mCoupon);
                }
            }
        }
    }


    /**
     * 处理优惠券
     */
    private void handleCoupons(CouponsResult.CouponData.Coupon mCoupon) {
        String minAmountStr = mCoupon.getMinAmount();//优惠券最低使用标准
        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
        double couponAmount = Double.parseDouble(couponAmountStr);
        double minAmount = Double.parseDouble(minAmountStr);
        double price = Double.parseDouble(cardPrice);
        if (price >= minAmount) {//订单价格>优惠券最低使用值，该优惠券可用
            mCouponsMoneyTextView.setText(mCoupon.getTitle() + mCoupon.getAmount() + " 元");
            if (price >= couponAmount) {
                //订单的价格大于优惠券的面额
                double amount = price - couponAmount;
                if (amount >= 0) {
                    mCardMoneyTextView.setText("¥ " + amount);
                }
            } else {//订单的面额小于优惠券的面额
                mCardMoneyTextView.setText("¥ " + "0.00");
            }
        } else {//优惠券不可用
            mCouponsMoneyTextView.setText("");
        }

    }

    @Override
    public void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData) {
        if (confirmBuyCardData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            //1购卡 2续卡 3升级卡
            int type = confirmBuyCardData.getPurchaseType();
            explain = confirmBuyCardData.getTips();
            if (type == BUY_TYPE_BUY) {
                setTitle("购买" + mCardName);
            } else if (type == BUY_TYPE_CONTINUE) {
                setTitle("续" + mCardName);
            } else if (type == BUY_TYPE_UPGRADE) {
                setTitle("升级" + mCardName);
            }

            if (buyType == BUY_TYPE_BUY) {

            } else if (buyType == BUY_TYPE_CONTINUE) {

            } else if (buyType == BUY_TYPE_UPGRADE) {

            }

            mPeriodOfValidityTextView.setText(confirmBuyCardData.getDeadLine());
            if (buyType == BUY_TYPE_UPGRADE) {
                cardPrice = confirmBuyCardData.getPrice();
                mCardMoneyTextView.setText("¥ " + cardPrice);
            }

            confirmCardList = confirmBuyCardData.getCardList();
            setCardView(confirmCardList);
            mCardRecyclerAdapter.setLayoutOnClickListener(mClickListener);
            mCardRecyclerAdapter.setExplainClickListener(mExplainClickListener);
            mGymNameTextView.setText(confirmBuyCardData.getGymName());
            mGymAddressTextView.setText(confirmBuyCardData.getGymAddress());
            noticeActivity = confirmBuyCardData.getPurchaseActivity();
            mBuyCardNoticeTextView.setText(noticeActivity);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }

    }

    @Override
    public void updateSubmitPayView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == 3) {//3 免金额支付
            PopupUtils.showToast("支付成功");
            jumpOrderActivity();
        } else {
            handlePay(payResultData);
        }
    }


    /**
     * 设置不能点击 说明 文字事件
     */
    private View.OnClickListener mExplainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConfirmCard confirmCard = (ConfirmCard) v.getTag();
            if (confirmCard != null) {
                showExplainDialog(explain);
            }
        }
    };

    private void showExplainDialog(String explain) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(explain);
        builder.setPositiveButton(getString(R.string.diaog_got_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置闲时或者全天选择状态
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout mLayout = (LinearLayout) v.findViewById(R.id.layout_confirm_card);
            if (mLayout != null) {
                ConfirmCard object = (ConfirmCard) mLayout.getTag();
                if (object != null) {
                    for (ConfirmCard data : confirmCardList) {
                        if (data.getType() == object.getType()) {
                            data.setQulification(1);
                        } else {
                            data.setQulification(0);
                        }
                    }
                    mCardRecyclerAdapter.notifyDataSetChanged();
                    cardPrice = object.getPrice();
                    mCardMoneyTextView.setText("¥ " + object.getPrice());
                    mCardId = object.getCardId();
                }
            }
        }
    };


    /**
     * 设置card
     */
    private void setCardView(List<ConfirmCard> confirmCardList) {
        for (ConfirmCard card : confirmCardList) {
            if (card.getQulification() == 1) {
                mCardMoneyTextView.setVisibility(View.VISIBLE);
                mImmediatelyBuyBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
                mImmediatelyBuyBtn.setTextColor(ResourceUtils.getColor(R.color.white));
                mCardId = card.getCardId();
                if (buyType != BUY_TYPE_UPGRADE) {
                    cardPrice = card.getPrice();
                    mCardMoneyTextView.setText("¥ " + cardPrice);
                }
            }
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCardRecyclerView.setLayoutManager(mLayoutManager);
        mCardRecyclerAdapter.setData(confirmCardList);
        mCardRecyclerView.setAdapter(mCardRecyclerAdapter);
    }


    private void handlePay(PayResultData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_BUY_CARD;
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
            jumpOrderActivity();
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

    public void onEvent(BuyCardWeChatMessage weChatMessage) {
        if (weChatMessage.isPaySuccess()) {
            jumpOrderActivity();
        }
    }

    private void jumpOrderActivity() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra(MyOrderActivity.KEY_CURRENT_INDEX, 1);
        startActivity(intent);
        this.finish();
    }

    public void onEvent(LoginFinishMessage message) {
        sendConfirmCardRequest();
    }


    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
