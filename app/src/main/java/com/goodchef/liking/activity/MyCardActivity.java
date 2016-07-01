package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardTimeLimitAdapter;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.goodchef.liking.mvp.presenter.MyCardPresenter;
import com.goodchef.liking.mvp.view.MyCardView;
import com.goodchef.liking.utils.ListViewUtil;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午3:54
 */
public class MyCardActivity extends AppBarActivity implements View.OnClickListener, MyCardView {
    public static final String KEY_INTENT_TITLE = "key_intent_title";
    private TextView mCardNumberTextView;//卡号
    private TextView mBuyCardTimeTextView;//买卡时间
    private TextView mPeriodOfValidityTextView;//有效期
    private ListView mListView;
    private LinearLayout mBottomLayout;

    private TextView mPromotionCardBtn;//升级卡
    private TextView mFlowCardBtn;//续卡

    private MyCardPresenter mMyCardPresenter;

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
        mListView = (ListView) findViewById(R.id.time_limit_recycleView);
        mBottomLayout = (LinearLayout) findViewById(R.id.layout_my_card_bottom);
        mPromotionCardBtn = (TextView) findViewById(R.id.my_promotion_card);
        mFlowCardBtn = (TextView) findViewById(R.id.my_card_flow_card);

    }

    private void setViewOnClickListener() {
        mPromotionCardBtn.setOnClickListener(this);
        mFlowCardBtn.setOnClickListener(this);
    }

    private void initData() {
        mMyCardPresenter = new MyCardPresenter(this, this);
        mMyCardPresenter.sendMyCardRequest();
    }

    @Override
    public void onClick(View v) {
        if (v == mPromotionCardBtn) {//升级卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 3);
            intent.putExtra(KEY_INTENT_TITLE, "升级卡");
            startActivity(intent);
        } else if (v == mFlowCardBtn) {//续卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 2);
            intent.putExtra(KEY_INTENT_TITLE, "续卡");
            startActivity(intent);
        }
    }

    @Override
    public void updateMyCardView(MyCardResult.MyCardData myCardData) {
        int hasCard = myCardData.getHasCard();
        if (hasCard == 1) {//有卡
            mBottomLayout.setVisibility(View.VISIBLE);
            MyCardResult.MyCardData.MyCard myCard = myCardData.getMyCard();
            if (myCard != null && !StringUtils.isEmpty(myCard.getCardNo()) && !StringUtils.isEmpty(myCard.getBuyTime())) {
                mCardNumberTextView.setText(myCard.getCardNo());
                mBuyCardTimeTextView.setText(myCard.getBuyTime());
                mPeriodOfValidityTextView.setText(myCard.getBuyTime() + " ~ " + myCard.getEndTime());
                List<TimeLimitData> limitDataList = myCard.getTimeLimit();
                if (limitDataList != null && limitDataList.size() > 0) {
                    CardTimeLimitAdapter adapter = new CardTimeLimitAdapter(this);
                    adapter.setData(limitDataList);
                    mListView.setAdapter(adapter);
                    ListViewUtil.setListViewHeightBasedOnChildren(mListView);
                }
            }
        } else {
            mBottomLayout.setVisibility(View.GONE);
        }

    }

}
