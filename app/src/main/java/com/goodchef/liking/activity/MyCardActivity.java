package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardTimeLimitAdapter;
import com.goodchef.liking.eventmessages.BuyCardSuccessMessage;
import com.goodchef.liking.eventmessages.BuyCardWeChatMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.goodchef.liking.mvp.presenter.MyCardPresenter;
import com.goodchef.liking.mvp.view.MyCardView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.ListViewUtil;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:我的会员卡
 * Author shaozucheng
 * Time:16/6/21 下午3:54
 */
public class MyCardActivity extends AppBarActivity implements View.OnClickListener, MyCardView {
    public static final String KEY_INTENT_TITLE = "key_intent_title";
    @BindView(R.id.my_card_flow_card)
    TextView mFlowCardBtn;//续卡
    @BindView(R.id.card_number)
    TextView mCardNumberTextView;//卡号
    @BindView(R.id.buy_card_time)
    TextView mBuyCardTimeTextView;
    @BindView(R.id.my_card_gym_name)
    TextView mGymName;//健身房名称
    @BindView(R.id.my_card_gym_address)
    TextView mGymAddress;//健身房地址
    @BindView(R.id.period_of_validity)
    TextView mPeriodOfValidityTextView;//有效期
    @BindView(R.id.time_limit_recycleView)
    ListView mListView;
    @BindView(R.id.upgrade_card_prompt_TextView)
    TextView mUpgradeCardPromptTextView;//升级卡提示
    @BindView(R.id.layout_no_card)
    RelativeLayout mNoCardLayout;
    @BindView(R.id.layout_my_card_root_view)
    ScrollView mRootScrollView;
    @BindView(R.id.my_card_state_view)
    LikingStateView mStateView;

    private int hasCard;
    private String gymId;//场馆id

    private MyCardPresenter mMyCardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_my_card));
        initView();
        setViewOnClickListener();
        mMyCardPresenter = new MyCardPresenter(this, this);
        initData();
    }

    private void initView() {
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void setViewOnClickListener() {
        mFlowCardBtn.setOnClickListener(this);
    }

    private void initData() {
        mStateView.setState(StateView.State.LOADING);
        mMyCardPresenter.sendMyCardRequest();
    }

    @Override
    public void onClick(View v) {
        UMengCountUtil.UmengCount(this, UmengEventId.UPGRADEANDCONTINUECARDACTIVITY);
        if (v == mFlowCardBtn) {//续卡
            if (!checkGymId()) {
                startActivity(LoginActivity.class);
                return;
            }
            Intent intent = new Intent(this, UpgradeAndContinueCardActivity.class);
            intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, NumberConstantUtil.TWO);
            intent.putExtra(KEY_INTENT_TITLE, getString(R.string.flow_card));
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            startActivity(intent);
        }
    }


    private boolean checkGymId() {
        if (hasCard == NumberConstantUtil.ONE && gymId.equals(NumberConstantUtil.STR_ZERO)) {//有卡,后端数据传错了，gymId应该不为0
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void updateMyCardView(MyCardResult.MyCardData myCardData) {
        if (myCardData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            hasCard = myCardData.getHasCard();
            showRenewCard(myCardData);
            if (hasCard == NumberConstantUtil.ONE) {//有卡
                mNoCardLayout.setVisibility(View.GONE);
                mRootScrollView.setVisibility(View.VISIBLE);
                String title = myCardData.getShowDesc();
                if (!StringUtils.isEmpty(title)) {
                    mUpgradeCardPromptTextView.setText(title.replace("\\n", "\n"));
                }
                showMyCardInfo(myCardData);
            } else {//没卡
                mNoCardLayout.setVisibility(View.VISIBLE);
                mFlowCardBtn.setVisibility(View.GONE);
                mRootScrollView.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 展示我的会员卡 卡的信息
     *
     * @param myCardData
     */
    private void showMyCardInfo(MyCardResult.MyCardData myCardData) {
        MyCardResult.MyCardData.MyCard myCard = myCardData.getMyCard();
        if (myCard != null && !StringUtils.isEmpty(myCard.getCardNo()) && !StringUtils.isEmpty(myCard.getBuyTime())) {
            mCardNumberTextView.setText(myCard.getCardNo());
            mBuyCardTimeTextView.setText(myCard.getBuyTime());
            mPeriodOfValidityTextView.setText(myCard.getStartTime() + " ~ " + myCard.getEndTime());
            mGymName.setText(myCard.getGymName());
            mGymAddress.setText(myCard.getGymAddress());
            gymId = myCard.getGymId();
            List<TimeLimitData> limitDataList = myCard.getTimeLimit();
            if (limitDataList != null && limitDataList.size() > NumberConstantUtil.ZERO) {
                CardTimeLimitAdapter adapter = new CardTimeLimitAdapter(this);
                adapter.setData(limitDataList);
                mListView.setAdapter(adapter);
                ListViewUtil.setListViewHeightBasedOnChildren(mListView);
            }
        }
    }

    /**
     * 展示是否有续卡按钮
     * w
     *
     * @param myCardData
     */
    private void showRenewCard(MyCardResult.MyCardData myCardData) {
        String showRenewCard = myCardData.getShowRenewCard() + "";
        if (!StringUtils.isEmpty(showRenewCard)) {
            if (showRenewCard.equals(NumberConstantUtil.STR_ZERO)) {//不展示续卡按钮
                mFlowCardBtn.setVisibility(View.GONE);
                mFlowCardBtn.setEnabled(false);
            } else if (showRenewCard.equals(NumberConstantUtil.STR_ONE)) {//展示续卡按钮
                mFlowCardBtn.setVisibility(View.VISIBLE);
                mFlowCardBtn.setEnabled(true);
            }
        }
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(LoginFinishMessage message) {
        initData();
    }

    public void onEvent(BuyCardSuccessMessage message) {
        if (message != null) {
            finish();
        }
    }

}
