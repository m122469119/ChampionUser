package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.http.result.CouponsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午5:55
 */
public class BuyCardConfirmActivity extends AppBarActivity implements View.OnClickListener {
    private static final int INTENT_REQUEST_CODE_COUPON = 101;
    private HImageView mHImageView;
    private TextView mPeriodOfValidityTextView;

    private RelativeLayout mWholeDayLayout;
    private RelativeLayout mIdleHoursLayout;
    private CheckBox mWholeDayCheckBox;
    private CheckBox mIdleHoursCheckBox;

    private TextView mWeekDayTextview;
    private TextView mWeekendTextView;

    private RelativeLayout mCouponsLayout;
    private TextView mCoursesMoneyTextView;

    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private TextView mCardMoneyTextView;
    private TextView mImmediatelyBuyBtn;

    private String mCardName;
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象
    private int payType;//支付方式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_confirm);
        initView();
        setViewOnClickListener();
        initData();
    }

    private void initView() {
        mHImageView = (HImageView) findViewById(R.id.buy_card_confirm_image);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);

        mWholeDayLayout = (RelativeLayout) findViewById(R.id.layout_whole_day);
        mIdleHoursLayout = (RelativeLayout) findViewById(R.id.layout_idle_hours);
        mWholeDayCheckBox = (CheckBox) findViewById(R.id.whole_day_checkBox);
        mIdleHoursCheckBox = (CheckBox) findViewById(R.id.idle_hours_checkBox);

        mWeekDayTextview = (TextView) findViewById(R.id.buy_period_textview);
        mWeekendTextView = (TextView) findViewById(R.id.buy_all_day_textview);

        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCoursesMoneyTextView = (TextView) findViewById(R.id.courses_money);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mCardMoneyTextView = (TextView) findViewById(R.id.card_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {
        mWholeDayLayout.setOnClickListener(this);
        mIdleHoursLayout.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
    }

    private void initData() {
        mCardName = getIntent().getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        setTitle("购买" + mCardName);
    }

    @Override
    public void onClick(View v) {
        if (v == mWholeDayLayout) {//选择整天
            mWholeDayCheckBox.setChecked(true);
            mIdleHoursCheckBox.setChecked(false);
        } else if (v == mIdleHoursLayout) {//选择周末
            mWholeDayCheckBox.setChecked(false);
            mIdleHoursCheckBox.setChecked(true);
        } else if (v == mAlipayLayout) {//选择支付宝
            mAlipayCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
            payType = 1;
        } else if (v == mWechatLayout) {//选择微信
            mAlipayCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
            payType = 0;
        } else if (v == mCouponsLayout) {//选优惠券
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.TYPE_MY_COUPONS,"BuyCardConfirmActivity");
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        } else if (v == mImmediatelyBuyBtn) {
            PopupUtils.showToast("开发中");
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

    }

}
