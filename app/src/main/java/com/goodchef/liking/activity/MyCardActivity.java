package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingBuyCardFragment;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午3:54
 */
public class MyCardActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_INTENT_TITLE = "key_intent_title";

    private TextView mCardNumberTextView;//卡号
    private TextView mBuyCardTimeTextView;//买卡时间
    private TextView mPeriodOfValidityTextView;//有效期
    private TextView mUserTimeTextView;//使用时间
    private TextView mUserTimeFiveDayTextView;//周一到周五
    private TextView mUserTimeTwoDayTextView;//周六周日
    private LinearLayout mTwoLayout;

    private LinearLayout mBottomLayout;
    private TextView mPromotionCardBtn;//升级卡
    private TextView mFlowCardBtn;//续卡


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        setTitle(getString(R.string.title_my_card));
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
        mBottomLayout = (LinearLayout) findViewById(R.id.layout_my_card_bottom);
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
        if (v == mPromotionCardBtn) {//升级卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE,3);
            intent.putExtra(KEY_INTENT_TITLE, "升级卡");
            startActivity(intent);
        } else if (v == mFlowCardBtn) {//续卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE,2);
            intent.putExtra(KEY_INTENT_TITLE, "续卡");
            startActivity(intent);
        }
    }
}
