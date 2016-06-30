package com.goodchef.liking.activity;

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
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardRecyclerAdapter;
import com.goodchef.liking.eventmessages.BuyCardWeChatMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.mvp.presenter.ConfirmBuyCardPresenter;
import com.goodchef.liking.mvp.view.ConfirmBuyCardView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午5:55
 */
public class BuyCardConfirmActivity extends AppBarActivity implements View.OnClickListener, ConfirmBuyCardView {
    private static final int INTENT_REQUEST_CODE_COUPON = 101;
    private static final int BUY_TYPE_BUY = 1;//买卡
    private static final int BUY_TYPE_CONTINUE = 2;//续卡
    private static final int BUY_TYPE_UPGRADE = 3;//升级

    private HImageView mHImageView;
    private TextView mPeriodOfValidityTextView;//有效期
    private RecyclerView mCardRecyclerView;

    private RelativeLayout mCouponsLayout;
    private TextView mCoursesMoneyTextView;

    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private TextView mCardMoneyTextView;
    private TextView mImmediatelyBuyBtn;

    private String mCardName;
    private int mCategoryId;
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象
    private String payType = "-1";//支付方式
    private ConfirmBuyCardPresenter mConfirmBuyCardPresenter;

    private CardRecyclerAdapter mCardRecyclerAdapter;
    private List<ConfirmCard> confirmCardList;


    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private int mCardId;
    private int buyType; //1 购卡  2 续卡  3 升级卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_confirm);
        initView();
        setViewOnClickListener();
        initData();
        initPayModule();
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void initView() {
        mHImageView = (HImageView) findViewById(R.id.buy_card_confirm_image);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);

        mCardRecyclerView = (RecyclerView) findViewById(R.id.card_recyclerView);

        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCoursesMoneyTextView = (TextView) findViewById(R.id.select_coupon_title);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mCardMoneyTextView = (TextView) findViewById(R.id.card_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
    }

    private void initData() {
        mCardName = getIntent().getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        mCategoryId = getIntent().getIntExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, 0);
        buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        if (buyType == BUY_TYPE_BUY) {
            setTitle("购买" + mCardName);
        } else if (buyType == BUY_TYPE_CONTINUE) {
            setTitle("续" + mCardName);
        } else if (buyType == BUY_TYPE_UPGRADE) {
            setTitle("升级" + mCardName);
        }
        mCardRecyclerAdapter = new CardRecyclerAdapter(this);
        sendConfirmCardRequest();
    }

    private void sendConfirmCardRequest() {
        mConfirmBuyCardPresenter = new ConfirmBuyCardPresenter(this, this);
        mConfirmBuyCardPresenter.confirmBuyCard(buyType, mCategoryId);
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
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "BuyCardConfirmActivity");
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        } else if (v == mImmediatelyBuyBtn) {
            if (Preference.isLogin()) {
                if (payType.equals("-1")) {
                    PopupUtils.showToast("请选择支付方式");
                    return;
                }
                senSubmitRequest();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }
    }


    private void senSubmitRequest() {
        mConfirmBuyCardPresenter.submitBuyCardData(mCardId, buyType, "", payType);
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

    }

    @Override
    public void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData) {
        String imageUrl = confirmBuyCardData.getAdsUrl();
        if (buyType == BUY_TYPE_BUY) {
            mHImageView.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
            }
        } else if (buyType == BUY_TYPE_CONTINUE) {
            mHImageView.setVisibility(View.GONE);
        } else if (buyType == BUY_TYPE_UPGRADE) {
            mHImageView.setVisibility(View.GONE);
        }

        mPeriodOfValidityTextView.setText(confirmBuyCardData.getDeadLine());
        if (buyType == BUY_TYPE_UPGRADE) {
            mCardMoneyTextView.setText("¥ " + confirmBuyCardData.getPrice());
        }

        confirmCardList = confirmBuyCardData.getCardList();
        setCardView(confirmCardList);
        mCardRecyclerAdapter.setLayoutOnClickListener(mClickListener);
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
     * 设置
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
                            data.setSelect(true);
                        } else {
                            data.setSelect(false);
                        }
                    }
                    mCardRecyclerAdapter.notifyDataSetChanged();
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
        if (confirmCardList != null && confirmCardList.size() >= 2) {
            if (confirmCardList.get(0).getQulification() == 1 && confirmCardList.get(1).getQulification() == 1) {
                confirmCardList.get(1).setLayoutViewEnable(true);
                confirmCardList.get(0).setLayoutViewEnable(true);
                for (ConfirmCard card : confirmCardList) {
                    if (card.getType() == 2) {//全天卡。1闲时，2全天,当非登录或者没有卡是，默认选中全天卡
                        card.setSelect(true);
                        mCardMoneyTextView.setText("¥ " + card.getPrice());
                    } else {
                        card.setSelect(false);
                    }
                }
            } else {
                if (buyType != BUY_TYPE_UPGRADE) {//当不是升级卡时，给买卡或者续卡默认选择状态的钱设置好
                    for (ConfirmCard card : confirmCardList) {
                        if (card.getQulification() == 1) {
                            mCardMoneyTextView.setText("¥ " + card.getPrice());
                        }
                    }
                }
            }

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mCardRecyclerView.setLayoutManager(mLayoutManager);
            mCardRecyclerAdapter.setData(confirmCardList);
            mCardRecyclerView.setAdapter(mCardRecyclerAdapter);
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
        jumpOrderActivity();
    }

    private void jumpOrderActivity() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }


}
