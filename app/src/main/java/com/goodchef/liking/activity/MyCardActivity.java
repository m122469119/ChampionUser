package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardTimeLimitAdapter;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.goodchef.liking.mvp.presenter.MyCardPresenter;
import com.goodchef.liking.mvp.view.MyCardView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.ListViewUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:我的会员卡
 * Author shaozucheng
 * Time:16/6/21 下午3:54
 */
public class MyCardActivity extends AppBarActivity implements View.OnClickListener, MyCardView {
    public static final String KEY_INTENT_TITLE = "key_intent_title";
    private TextView mCardNumberTextView;//卡号
    private TextView mBuyCardTimeTextView;//买卡时间
    private TextView mPeriodOfValidityTextView;//有效期
    private TextView mGymName;//健身房名称
    private TextView mGymAddress;//健身房地址
    private ListView mListView;
    private LinearLayout mBottomLayout;
    private ScrollView mRootScrollView;
    private RelativeLayout mNoCardLayout;
    private LikingStateView mStateView;

    private TextView mPromotionCardBtn;//升级卡
    private TextView mFlowCardBtn;//续卡
    private String gymId;//场馆id

    private MyCardPresenter mMyCardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        setTitle(getString(R.string.title_my_card));
        initView();
        setViewOnClickListener();
        mMyCardPresenter = new MyCardPresenter(this, this);
        initData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_card_state_view);
        mCardNumberTextView = (TextView) findViewById(R.id.card_number);
        mBuyCardTimeTextView = (TextView) findViewById(R.id.buy_card_time);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);
        mGymName = (TextView) findViewById(R.id.my_card_gym_name);
        mGymAddress = (TextView) findViewById(R.id.my_card_gym_address);
        mListView = (ListView) findViewById(R.id.time_limit_recycleView);
        mBottomLayout = (LinearLayout) findViewById(R.id.layout_my_card_bottom);
        mPromotionCardBtn = (TextView) findViewById(R.id.my_promotion_card);
        mFlowCardBtn = (TextView) findViewById(R.id.my_card_flow_card);
        mRootScrollView = (ScrollView) findViewById(R.id.layout_my_card_root_view);
        mNoCardLayout = (RelativeLayout) findViewById(R.id.layout_no_card);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void setViewOnClickListener() {
        mPromotionCardBtn.setOnClickListener(this);
        mFlowCardBtn.setOnClickListener(this);
    }

    private void initData() {
        mStateView.setState(StateView.State.LOADING);
        mMyCardPresenter.sendMyCardRequest();
    }

    @Override
    public void onClick(View v) {
        UMengCountUtil.UmengCount(this, UmengEventId.UPGRADEANDCONTINUECARDACTIVITY);
        if (v == mPromotionCardBtn) {//升级卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 3);
            intent.putExtra(KEY_INTENT_TITLE, "升级卡");
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            startActivity(intent);
        } else if (v == mFlowCardBtn) {//续卡
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 2);
            intent.putExtra(KEY_INTENT_TITLE, "续卡");
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            startActivity(intent);
        }
    }

    @Override
    public void updateMyCardView(MyCardResult.MyCardData myCardData) {
        if (myCardData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            int hasCard = myCardData.getHasCard();
            if (hasCard == 1) {//有卡
                mNoCardLayout.setVisibility(View.GONE);
                mRootScrollView.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.VISIBLE);
                MyCardResult.MyCardData.MyCard myCard = myCardData.getMyCard();
                if (myCard != null && !StringUtils.isEmpty(myCard.getCardNo()) && !StringUtils.isEmpty(myCard.getBuyTime())) {
                    mCardNumberTextView.setText(myCard.getCardNo());
                    mBuyCardTimeTextView.setText(myCard.getBuyTime());
                    mPeriodOfValidityTextView.setText(myCard.getStartTime() + " ~ " + myCard.getEndTime());
                    mGymName.setText(myCard.getGymName());
                    mGymAddress.setText(myCard.getGymAddress());
                    gymId = myCard.getGymId();
                    List<TimeLimitData> limitDataList = myCard.getTimeLimit();
                    if (limitDataList != null && limitDataList.size() > 0) {
                        CardTimeLimitAdapter adapter = new CardTimeLimitAdapter(this);
                        adapter.setData(limitDataList);
                        mListView.setAdapter(adapter);
                        ListViewUtil.setListViewHeightBasedOnChildren(mListView);
                    }
                }
            } else {//没卡
                mNoCardLayout.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.GONE);
                mRootScrollView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
