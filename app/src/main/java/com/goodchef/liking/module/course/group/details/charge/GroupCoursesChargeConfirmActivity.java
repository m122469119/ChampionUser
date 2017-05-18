package com.goodchef.liking.module.course.group.details.charge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.pay.alipay.AliPay;
import com.aaron.pay.alipay.OnAliPayListener;
import com.aaron.pay.weixin.WeixinPay;
import com.aaron.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.eventmessages.BuyGroupCoursesAliPayMessage;
import com.goodchef.liking.eventmessages.BuyGroupCoursesWechatMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.NoCardMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.module.coupons.CouponsActivity;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:收费团体课确认订单页
 * Author shaozucheng
 * Time:16/8/24 下午5:14
 */
public class GroupCoursesChargeConfirmActivity extends AppBarActivity implements GroupCoursesChargeConfirmContract.ChargeGroupCoursesView {

    private static final int INTENT_REQUEST_CODE_GROUP_COURSES_COUPON = 200;
    private static final int PAY_TYPE = 3;//3 免金额支付

    @BindView(R.id.charge_group_courses_state_view)
    LikingStateView mStateView;
    @BindView(R.id.group_courses_confirm_prompt)
    TextView mPromptTextView;//确认够爱提示
    @BindView(R.id.courses_name)
    TextView mCoursesNameTextView;//课程名称
    @BindView(R.id.courses_teacher)
    TextView mCoursesTeacherTextView;//课程教练
    @BindView(R.id.courses_length)
    TextView mCoursesLengthTextView;//课程时长
    @BindView(R.id.courses_strength)
    RatingBar mCoursesStrengthTextView;//课程强度
    @BindView(R.id.courses_time)
    TextView mCoursesTimeTextView;//课程时间
    @BindView(R.id.courses_address)
    TextView mCoursesAddressTextView;//课程地址

    @BindView(R.id.layout_coupons_courses)
    RelativeLayout mCouponsLayout;//优惠券布局
    @BindView(R.id.select_coupon_title)
    TextView mCouponTitleTextView;//优惠券信息

    @BindView(R.id.courses_money)
    TextView mCoursesMoneyTextView;//课程金额
    @BindView(R.id.immediately_buy_btn)
    TextView mImmediatelyBuyBtn;//立即购买

    @BindView(R.id.layout_alipay)
    RelativeLayout mAlipayLayout;//支付布局
    @BindView(R.id.layout_wechat)
    RelativeLayout mWechatLayout;
    @BindView(R.id.pay_type_alipay_checkBox)
    CheckBox mAlipayCheckBox;
    @BindView(R.id.pay_type_wechat_checkBox)
    CheckBox mWechatCheckBox;

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private String payType = "-1";//支付方式

    private String scheduleId;//排期id

    private GroupCoursesChargeConfirmContract.ChargeGroupCoursesConfirmPresenter mChargeGroupCoursesPresenter;

    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象

    private String mAmountCount;//课程总金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_courses_charge_confrim);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_confirm_buy_group_courses));
        initView();
        getIntentData();
        initData();
        initPayModule();
        setPayDefaultType();
    }

    private void initView() {
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void getIntentData() {
        scheduleId = getIntent().getStringExtra(LikingLessonFragment.KEY_SCHEDULE_ID);
    }

    private void initData() {
        mChargeGroupCoursesPresenter = new GroupCoursesChargeConfirmContract.ChargeGroupCoursesConfirmPresenter(this, this);
        mChargeGroupCoursesPresenter.getChargeGroupCoursesConfirmData(LikingHomeActivity.gymId, scheduleId);
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void setPayDefaultType() {
        mAlipayCheckBox.setChecked(true);
        mWechatCheckBox.setChecked(false);
        payType = "1";
    }


    @Override
    public void updateChargeGroupCoursesView(ChargeGroupConfirmResult.ChargeGroupConfirmData chargeGroupConfirmData) {
        if (chargeGroupConfirmData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mPromptTextView.setText(chargeGroupConfirmData.getPrompt());
            mCoursesNameTextView.setText(chargeGroupConfirmData.getCourseName());
            mCoursesTeacherTextView.setText(chargeGroupConfirmData.getTrainerName());
            mCoursesLengthTextView.setText(chargeGroupConfirmData.getDuration());
            mCoursesTimeTextView.setText(chargeGroupConfirmData.getCourseTime());
            mCoursesAddressTextView.setText(chargeGroupConfirmData.getCourseAddress());
            float strength = (float) chargeGroupConfirmData.getIntensity();
            mCoursesStrengthTextView.setRating(strength);
            mAmountCount = chargeGroupConfirmData.getAmount();
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + mAmountCount);
        }
    }


    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @OnClick({R.id.layout_coupons_courses, R.id.layout_alipay, R.id.layout_wechat, R.id.immediately_buy_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_coupons_courses://收费团体课没有传入gymid
                UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
                Intent intent = new Intent(this, CouponsActivity.class);
                intent.putExtra(CouponsActivity.KEY_SCHEDULE_ID, scheduleId);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, LikingHomeActivity.gymId);
                if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
                    intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCoupon_code());
                }
                intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "GroupCoursesChargeConfirmActivity");
                startActivityForResult(intent, INTENT_REQUEST_CODE_GROUP_COURSES_COUPON);
                break;
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
            case R.id.immediately_buy_btn:
                sendBuyCoursesRequest();
                break;
            default:
                break;
        }
    }

    /**
     * 发送购买私教课请求
     */
    private void sendBuyCoursesRequest() {
        if (payType.equals("-1")) {
            showToast(getString(R.string.please_select_pay_type));
            return;
        }
        UMengCountUtil.UmengBtnCount(GroupCoursesChargeConfirmActivity.this, UmengEventId.GROUPCOURSESCHARGECONFIRMBTN);
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
            mChargeGroupCoursesPresenter.chargeGroupCoursesImmediately(LikingHomeActivity.gymId, scheduleId, mCoupon.getCoupon_code(), payType);
        } else {
            mChargeGroupCoursesPresenter.chargeGroupCoursesImmediately(LikingHomeActivity.gymId, scheduleId, "", payType);
        }
    }

    @Override
    public void updatePaySubmitView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == PAY_TYPE) {//3 免金额支付
            showToast(getString(R.string.pay_success));
            postEvent(new BuyGroupCoursesAliPayMessage());
            jumpToMyCoursesActivity();
        } else {
            handlePay(payResultData);
        }
    }

    @Override
    public void updateBuyCoursesErrorView() {
        this.finish();
    }

    @Override
    public void updateErrorNoCard(String errorMessage) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GroupCoursesChargeConfirmActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.go_buy_card), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GroupCoursesChargeConfirmActivity.this, LikingHomeActivity.class);
                intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, 1);
                startActivity(intent);
                postEvent(new NoCardMessage(1));
                dialog.dismiss();
                GroupCoursesChargeConfirmActivity.this.finish();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
    }

    @Override
    public void updateBuyCoursesNotOnGym(String errorMessage) {
        //购买的的课程不在该健身房下
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.diaog_got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
    }


    private void handlePay(PayResultData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_BUY_GROUP_COURSES;
                WXPayEntryActivity.orderId = data.getOrderId();
                mWeixinPay.setPrePayId(data.getWxPrepayId());
                mWeixinPay.doPay();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_GROUP_COURSES_COUPON) {
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
        double coursesPrice = Double.parseDouble(mAmountCount);
        double couponAmount = Double.parseDouble(couponAmountStr);
        mCouponTitleTextView.setText(mCoupon.getAmount() + getString(R.string.yuan));
        if (coursesPrice >= couponAmount) {
            //课程的价格大于优惠券的面额
            double amount = coursesPrice - couponAmount;
            if (amount >= 0) {
                mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + amount);
            }
        } else {//课程的面额小于优惠券的面额
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + "0.00");
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
            postEvent(new BuyGroupCoursesAliPayMessage());
            jumpToMyCoursesActivity();
        }

        @Override
        public void onFailure(String errorMessage) {
            setPayFailView();
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

    public void onEvent(BuyGroupCoursesWechatMessage wechatMessage) {
        if (wechatMessage.isPaySuccess()) {
            jumpToMyCoursesActivity();
        } else {
            setPayFailView();
        }
    }

    private void setPayFailView() {
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getAmount())) {
            mCouponTitleTextView.setText("");
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + mAmountCount);
            mCoupon = null;
        }
    }

    public void onEvent(CoursesErrorMessage message) {
        this.finish();
    }


    /**
     * 跳转我的课程列表
     */
    private void jumpToMyCoursesActivity() {
        Intent intent = new Intent(this, MyLessonActivity.class);
        intent.putExtra(MyLessonActivity.KEY_CURRENT_ITEM, 0);
        startActivity(intent);
        this.finish();
    }
}
