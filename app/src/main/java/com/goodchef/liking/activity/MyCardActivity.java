package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午3:54
 */
public class MyCardActivity extends AppBarActivity implements View.OnClickListener {
    private TextView mCardNumberTextView;//卡号
    private TextView mBuyCardTimeTextView;//买卡时间
    private TextView mPeriodOfValidityTextView;//有效期
    private TextView mUserTimeTextView;//使用时间
    private TextView mUserTimeFiveDayTextView;//周一到周五
    private TextView mUserTimeTwoDayTextView;//周六周日
    private LinearLayout mTwoLayout;
    private TextView mPromotionCardBtn;//升级卡
    private TextView mFlowCardBtn;//续卡


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        setTitle("我的会员卡");
        initView();
        setViewOnClickListener();
        initData();
    }

    private void initView() {
        mCardNumberTextView = (TextView) findViewById(R.id.card_number);
        mBuyCardTimeTextView = (TextView) findViewById(R.id.buy_card_time);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);
        mUserTimeTextView = (TextView) findViewById(R.id.user_time);
        mUserTimeFiveDayTextView = (TextView) findViewById(R.id.user_time_five_day);
        mUserTimeTwoDayTextView = (TextView) findViewById(R.id.user_time_two_day);
        mTwoLayout = (LinearLayout) findViewById(R.id.layout_user_time_two_day);
        mPromotionCardBtn = (TextView) findViewById(R.id.promotion_card);
        mFlowCardBtn = (TextView) findViewById(R.id.flow_card);
    }

    private void setViewOnClickListener() {
        mPromotionCardBtn.setOnClickListener(this);
        mFlowCardBtn.setOnClickListener(this);
    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v == mPromotionCardBtn) {
            PopupUtils.showToast("开发中");
        } else if (v == mFlowCardBtn) {
            PopupUtils.showToast("开发中。。。");
        }
    }
}
