package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

/**
 * 说明:确认订单页
 * Author shaozucheng
 * Time:16/6/23 下午6:14
 */
public class DishesConfirmActivity extends AppBarActivity {
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

    private void initData() {

    }
}
