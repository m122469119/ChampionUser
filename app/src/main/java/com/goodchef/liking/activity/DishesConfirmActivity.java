package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.mvp.presenter.NutritionMealConfirmPresenter;
import com.goodchef.liking.mvp.view.NutritionMealConfirmView;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

/**
 * 说明:确认订单页
 * Author shaozucheng
 * Time:16/6/23 下午6:14
 */
public class DishesConfirmActivity extends AppBarActivity implements View.OnClickListener, NutritionMealConfirmView {
    private static final int INTENT_REQUEST_CODE_COUPON = 102;
    private TextView mChangeShopTextView;
    private TextView mMealsAddressTextView;
    private TextView mGetMealsTimeTextView;

    private RelativeLayout mCouponsLayout;
    private TextView mCouponTitleTextView;

    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private PullToRefreshRecyclerView mRecyclerView;
    private TextView mDishesMoneyextView;
    private TextView mImmediatelyPayBtn;

    private String payType = "-1";//支付方式

    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象

    private NutritionMealConfirmPresenter mNutritionMealConfirmPresenter;
    private String userCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_confirm);
        initView();
        initData();
    }

    private void initView() {
        mChangeShopTextView = (TextView) findViewById(R.id.change_shop);
        mMealsAddressTextView = (TextView) findViewById(R.id.having_meals_address);
        mGetMealsTimeTextView = (TextView) findViewById(R.id.get_meals_time);

        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCouponTitleTextView = (TextView) findViewById(R.id.select_coupon_title);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.my_order_dishes);
        mDishesMoneyextView = (TextView) findViewById(R.id.dishes_money);
        mImmediatelyPayBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyPayBtn.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
    }

    private void initData() {
       // sendRequest();
        setViewOnClickListener();
    }

    private void sendRequest() {
        mNutritionMealConfirmPresenter = new NutritionMealConfirmPresenter(this, this);
        mNutritionMealConfirmPresenter.confirmFood(userCityId, "");
    }

    @Override
    public void onClick(View v) {
        if (v == mCouponsLayout) {
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.KEY_COURSE_ID, "");
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
        double coursesPrice = Double.parseDouble("10");
        double couponAmount = Double.parseDouble(couponAmountStr);
        double minAmount = Double.parseDouble(minAmountStr);
        if (coursesPrice > minAmount) {//课程价格>优惠券最低使用值，该优惠券可用
            if (coursesPrice > couponAmount) {
                mCouponTitleTextView.setText(mCoupon.getTitle() + mCoupon.getAmount() + " 元");
                //课程的价格大于优惠券的面额
                double amount = coursesPrice - couponAmount;
                if (amount > 0) {
                    mDishesMoneyextView.setText("¥ " + amount);
                }
            } else {//课程的面额小于优惠券的面额
                mDishesMoneyextView.setText("¥ " + "0.00");
            }
        } else {//优惠券不可用
            mCouponTitleTextView.setText("");
            PopupUtils.showToast("该优惠券未达使用范围请重新选择");
        }
    }

    @Override
    public void updateNutritionMealConfirmView(NutritionMealConfirmResult.NutritionMealConfirmData confirmData) {

    }
}
