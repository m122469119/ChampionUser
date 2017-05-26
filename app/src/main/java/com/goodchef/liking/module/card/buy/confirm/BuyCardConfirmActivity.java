package com.goodchef.liking.module.card.buy.confirm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.base.widget.web.HDefaultWebActivity;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.pay.alipay.AliPay;
import com.aaron.pay.alipay.OnAliPayListener;
import com.aaron.pay.weixin.WeixinPay;
import com.aaron.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardRecyclerAdapter;
import com.goodchef.liking.dialog.AnnouncementDialog;
import com.goodchef.liking.eventmessages.BuyCardSuccessMessage;
import com.goodchef.liking.eventmessages.BuyCardWeChatMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.card.order.MyOrderActivity;
import com.goodchef.liking.module.coupons.CouponsActivity;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:确认购买卡
 * Author shaozucheng
 * Time:16/6/17 下午5:55
 */
public class BuyCardConfirmActivity extends AppBarActivity implements BuyCardConfirmContract.ConfirmBuyCardView {

    private static final int INTENT_REQUEST_CODE_COUPON = 101;
    private static final int BUY_TYPE_BUY = 1;//买卡
    private static final int BUY_TYPE_CONTINUE = 2;//续卡
    private static final int BUY_TYPE_UPGRADE = 3;//升级
    public static final String KEY_CARD_ID = "key_card_id";


    @BindView(R.id.buy_card_notice)
    TextView mBuyCardNoticeTextView;//活动
    @BindView(R.id.gym_name)
    TextView mGymNameTextView;//场馆名称
    @BindView(R.id.gym_address)
    TextView mGymAddressTextView;//场馆地址
    @BindView(R.id.period_of_validity)
    TextView mPeriodOfValidityTextView;//有效期
    @BindView(R.id.card_recyclerView)
    RecyclerView mCardRecyclerView;

    @BindView(R.id.layout_coupons_courses)
    RelativeLayout mCouponsLayout;
    @BindView(R.id.select_coupon_title)
    TextView mCouponsMoneyTextView;

    //支付相关
    @BindView(R.id.layout_alipay)
    RelativeLayout mAlipayLayout;
    @BindView(R.id.layout_wechat)
    RelativeLayout mWechatLayout;
    @BindView(R.id.pay_type_alipay_checkBox)
    CheckBox mAlipayCheckBox;
    @BindView(R.id.pay_type_wechat_checkBox)
    CheckBox mWechatCheckBox;

    @BindView(R.id.buy_card_agree_protocol)
    LinearLayout mAgreeProtocolTextView;
    @BindView(R.id.card_money)
    TextView mCardMoneyTextView;//
    @BindView(R.id.immediately_buy_btn)
    TextView mImmediatelyBuyBtn;//立即支付

    private String mCardName;
    private int mCategoryId;
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象
    private String payType = "-1";//支付方式
    private BuyCardConfirmContract.ConfirmBuyCardPresenter mConfirmBuyCardPresenter;

    private CardRecyclerAdapter mCardRecyclerAdapter;
    private List<ConfirmCard> confirmCardList;


    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private int mCardId;//会员卡ID
    private int buyType; //1 购卡  2 续卡  3 升级卡
    private String cardPrice;//卡的金额

    @BindView(R.id.buy_card_confirm_state_view)
    LikingStateView mStateView;
    private String explain;
    private String gymId = "0";
    private String noticeActivity;//活动
    private String submitGymId;
    private String mCardGymName;//场馆名称
    private String mCardType;//购卡类型
    private String mCardTotalMoney;//卡的总价钱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_confirm);
        ButterKnife.bind(this);
        initView();
        setDefaultPayType();
        initData();
        initPayModule();
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void initView() {
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendConfirmCardRequest();
            }
        });
    }

    /**
     * 设置默认支付方式
     */
    private void setDefaultPayType() {
        mAlipayCheckBox.setChecked(true);
        mWechatCheckBox.setChecked(false);
        payType = "1";
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mCardName = intent.getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        mCategoryId = intent.getIntExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, 0);
        buyType = intent.getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        gymId = intent.getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        sendConfirmCardRequest();
    }

    private void initData() {
        mCardName = getIntent().getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        mCategoryId = getIntent().getIntExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, 0);
        buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        sendConfirmCardRequest();
        setGymId();
    }

    private void sendConfirmCardRequest() {
        if (mConfirmBuyCardPresenter == null) {
            mConfirmBuyCardPresenter = new BuyCardConfirmContract.ConfirmBuyCardPresenter(this, this);
        }
        mConfirmBuyCardPresenter.confirmBuyCard(buyType, mCategoryId, gymId);
    }

    @OnClick({R.id.layout_alipay, R.id.layout_wechat, R.id.layout_coupons_courses, R.id.immediately_buy_btn, R.id.buy_card_agree_protocol, R.id.buy_card_notice})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_alipay:
                mAlipayCheckBox.setChecked(true);
                mWechatCheckBox.setChecked(false);
                payType = "1";
                break;
            case R.id.layout_wechat:
                mAlipayCheckBox.setChecked(false);
                mWechatCheckBox.setChecked(true);
                payType = "0";
                break;
            case R.id.layout_coupons_courses:
                if (LikingPreference.isLogin()) {
                    UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
                    Intent intent = new Intent(this, CouponsActivity.class);
                    intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "BuyCardConfirmActivity");
                    intent.putExtra(KEY_CARD_ID, mCardId + "");
                    intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, buyType + "");
                    intent.putExtra(LikingLessonFragment.KEY_GYM_ID, submitGymId);
                    if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
                        intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCoupon_code());
                    }
                    startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.immediately_buy_btn:
                if (LikingPreference.isLogin()) {
                    if (payType.equals("-1")) {
                        showToast(getString(R.string.please_select_pay_type));
                        return;
                    }
                    UMengCountUtil.UmengBtnCount(this, UmengEventId.BUY_CARD_IMMEDIATELY_BUY);
                    showSubmitDialog();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.buy_card_agree_protocol:
                BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
                if (baseConfigResult != null) {
                    BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                    if (baseConfigData != null) {
                        String serviceUrl = baseConfigData.getServiceUrl();
                        if (!StringUtils.isEmpty(serviceUrl)) {
                            HDefaultWebActivity.launch(this, serviceUrl, getString(R.string.terrace_member_agreement));
                        }
                    }
                }
                break;
            case R.id.buy_card_notice:
                final AnnouncementDialog dialog = new AnnouncementDialog(this, noticeActivity);
                dialog.setButtonDismiss();
                break;
            default:
        }
    }

    /***
     * 确认购卡信息对话框
     */
    private void showSubmitDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_view_buy_card_submit, null, false);
        TextView mGymTextView = (TextView) view.findViewById(R.id.buy_card_gym_TextView);
        TextView mCardTypeTextView = (TextView) view.findViewById(R.id.buy_card_type_TextView);
        TextView mMoneyTextView = (TextView) view.findViewById(R.id.buy_card_money_TextView);

        mGymTextView.setText(mCardGymName);
        mCardTypeTextView.setText(mCardName + mCardType);
        mMoneyTextView.setText(getString(R.string.money_symbol) + mCardTotalMoney);

        builder.setCustomView(view);
        builder.setNegativeButton(R.string.again_think, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.buy_card_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                senSubmitRequest();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 提交支付请求
     */
    private void senSubmitRequest() {
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
            mConfirmBuyCardPresenter.submitBuyCardData(mCardId, buyType, mCoupon.getCoupon_code(), payType, submitGymId);
        } else {
            mConfirmBuyCardPresenter.submitBuyCardData(mCardId, buyType, "", payType, submitGymId);
        }
    }

    private void setGymId() {
        if (buyType == BUY_TYPE_BUY) {//买卡
            submitGymId = LikingHomeActivity.gymId;
        } else if (buyType == BUY_TYPE_CONTINUE) {//续卡
            submitGymId = gymId;
        } else if (buyType == BUY_TYPE_UPGRADE) {//升级卡
            submitGymId = gymId;
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
        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
        double couponAmount = Double.parseDouble(couponAmountStr);
        double price = Double.parseDouble(cardPrice);
        mCouponsMoneyTextView.setText(mCoupon.getAmount() + getString(R.string.yuan));
        if (price >= couponAmount) {
            //订单的价格大于优惠券的面额
            double amount = price - couponAmount;
            if (amount >= 0) {
                mCardTotalMoney = amount + "";
                mCardMoneyTextView.setText(getString(R.string.money_symbol) + mCardTotalMoney);
            }
        } else {//订单的面额小于优惠券的面额
            mCardTotalMoney = "0.00";
            mCardMoneyTextView.setText(getString(R.string.money_symbol) + mCardTotalMoney);
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
                setTitle(getString(R.string.buy) + mCardName);
            } else if (type == BUY_TYPE_CONTINUE) {
                setTitle(getString(R.string.go_on_buy) + mCardName);
            } else if (type == BUY_TYPE_UPGRADE) {
                setTitle(getString(R.string.upgrade) + mCardName);
            }

            mPeriodOfValidityTextView.setText(confirmBuyCardData.getDeadLine());
            if (buyType == BUY_TYPE_UPGRADE) {
                cardPrice = confirmBuyCardData.getPrice();
                mCardTotalMoney = cardPrice;
                mCardMoneyTextView.setText(getString(R.string.money_symbol) + cardPrice);
            }
            confirmCardList = confirmBuyCardData.getCardList();
            int showLimit = confirmBuyCardData.getShowTimeLimit();
            if (showLimit == NumberConstantUtil.ZERO) {//如果是0不显示选择错峰和全通卡的选项
                mCardRecyclerView.setVisibility(View.GONE);
                if (!ListUtils.isEmpty(confirmCardList)) {//不显示选择卡类型是，从卡列表中选出默认选中的那个卡种
                    setCardOnlyView();
                }
            } else if (showLimit == NumberConstantUtil.ONE) {//如果是1显示
                mCardRecyclerView.setVisibility(View.VISIBLE);
                if (!ListUtils.isEmpty(confirmCardList)) {//设置卡的类型
                    setCardView(confirmCardList);
                    mCardRecyclerAdapter.setLayoutOnClickListener(mClickListener);
                    mCardRecyclerAdapter.setExplainClickListener(mExplainClickListener);
                }
            }

            mCardGymName = confirmBuyCardData.getGymName();
            mGymNameTextView.setText(mCardGymName);
            mGymAddressTextView.setText(confirmBuyCardData.getGymAddress());
            noticeActivity = confirmBuyCardData.getPurchaseActivity();
            mBuyCardNoticeTextView.setText(noticeActivity);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }

    }

    /**
     * 不显示选择卡类型是，从卡列表中选出默认选中的那个卡种,并且显示价格
     */
    private void setCardOnlyView() {
        for (ConfirmCard card : confirmCardList) {
            if (card.getQulification() == NumberConstantUtil.ONE) {
                mCardMoneyTextView.setVisibility(View.VISIBLE);
                mImmediatelyBuyBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
                mImmediatelyBuyBtn.setTextColor(ResourceUtils.getColor(R.color.white));
                mCardId = card.getCardId();
                mCardType = card.getName();
                if (buyType != BUY_TYPE_UPGRADE) {
                    cardPrice = card.getPrice();
                    mCardTotalMoney = cardPrice;
                    mCardMoneyTextView.setText(getString(R.string.money_symbol) + cardPrice);
                }
            }
        }
    }

    @Override
    public void updateSubmitPayView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == 3) {//3 免金额支付
            showToast(getString(R.string.pay_success));
            jumpOrderActivity();
        } else {
            handlePay(payResultData);
        }
    }

    @Override
    public void updateErrorView(String errorMessage) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BuyCardConfirmActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
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
                            data.setQulification(NumberConstantUtil.ONE);
                        } else {
                            data.setQulification(NumberConstantUtil.ZERO);
                        }
                    }
                    mCardRecyclerAdapter.notifyDataSetChanged();
                    cardPrice = object.getPrice();
                    mCardTotalMoney = cardPrice;
                    mCardMoneyTextView.setText(getString(R.string.money_symbol) + object.getPrice());
                    mCardId = object.getCardId();
                    mCardType = object.getName();
                    setPayFailView();
                }
            }
        }
    };


    /**
     * 设置card
     */
    private void setCardView(List<ConfirmCard> confirmCardList) {
        for (ConfirmCard card : confirmCardList) {
            if (card.getQulification() == NumberConstantUtil.ONE) {
                mCardMoneyTextView.setVisibility(View.VISIBLE);
                mImmediatelyBuyBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
                mImmediatelyBuyBtn.setTextColor(ResourceUtils.getColor(R.color.white));
                mCardId = card.getCardId();
                mCardType = card.getName();
                if (buyType != BUY_TYPE_UPGRADE) {
                    cardPrice = card.getPrice();
                    mCardTotalMoney = cardPrice;
                    mCardMoneyTextView.setText(getString(R.string.money_symbol) + cardPrice);
                }
            }
        }
        if (mCardRecyclerAdapter == null) {
            mCardRecyclerAdapter = new CardRecyclerAdapter(BuyCardConfirmActivity.this);
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
            LogUtils.i(TAG, "alipay start");
        }

        @Override
        public void onSuccess() {
            LogUtils.i(TAG, "alipay sucess");
            jumpOrderActivity();
        }

        @Override
        public void onFailure(String errorMessage) {
            LogUtils.i(TAG, "支付失败");
            setPayFailView();
        }

        @Override
        public void confirm() {
            LogUtils.i(TAG, "alipay confirm");
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
        } else {
            setPayFailView();
        }
    }

    private void setPayFailView() {
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getAmount())) {
            mCouponsMoneyTextView.setText("");
            mCardTotalMoney = cardPrice;
            mCardMoneyTextView.setText(getString(R.string.money_symbol) + mCardTotalMoney);
            mCoupon = null;
        }
    }

    private void jumpOrderActivity() {
        postEvent(new BuyCardSuccessMessage());
        LikingPreference.setLoginGymId(submitGymId);
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra(MyOrderActivity.KEY_CURRENT_INDEX, NumberConstantUtil.ONE);
        startActivity(intent);
        finish();
    }

    public void onEvent(LoginFinishMessage message) {
        sendConfirmCardRequest();
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
