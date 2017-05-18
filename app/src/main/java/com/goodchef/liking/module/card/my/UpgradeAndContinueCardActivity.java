package com.goodchef.liking.module.card.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.base.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.adapter.UpgradeContinueCardAdapter;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:升级卡或者续卡
 * Author shaozucheng
 * Time:16/6/30 上午10:02
 */
public class UpgradeAndContinueCardActivity extends AppBarActivity implements UpgradeAndContinueCardContract.CardListView {

    @BindView(R.id.upgrade_and_continue_recycleView) PullToRefreshRecyclerView mRecyclerView;
    private int buyType;
    private String title;

    private UpgradeContinueCardAdapter mUpgradeContinueCardAdapter;
    private UpgradeAndContinueCardContract.CardListPresenter mCardListPresenter;
    @BindView(R.id.upgrade_continue_state_view) LikingStateView mStateView;
    private String mGymId;

    private String longitude = "0";
    private String latitude = "0";
    private String cityId = "310100";
    private String districtId = "310104";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_and_continue);
        ButterKnife.bind(this);
        setLocationData();
        initView();
        initData();
    }

    /**
     * 从本地拿到定位的经纬度和城市等信息
     */
    private void setLocationData() {
        LocationData locationData = LikingPreference.getLocationData();
        if (locationData != null) {
            longitude = locationData.getLongitude();
            latitude = locationData.getLatitude();
            cityId = locationData.getCityId();
            districtId = locationData.getDistrictId();
        }
    }

    private void initView() {
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                mCardListPresenter.getCardList(longitude, latitude, cityId, districtId, mGymId, buyType);
            }
        });
    }

    private void initData() {
        buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        title = getIntent().getStringExtra(MyCardActivity.KEY_INTENT_TITLE);
        mGymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        setTitle(title);
        mCardListPresenter = new UpgradeAndContinueCardContract.CardListPresenter(this, this);
        mCardListPresenter.getCardList(longitude, latitude, cityId, districtId, mGymId, buyType);
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        List<CardResult.CardData.Card> list = cardData.getCardList();
        if (list != null) {
            if (list.size() > 0) {
                mStateView.setState(StateView.State.SUCCESS);
                mUpgradeContinueCardAdapter = new UpgradeContinueCardAdapter(this);
                mUpgradeContinueCardAdapter.setData(list);
                mRecyclerView.setAdapter(mUpgradeContinueCardAdapter);
                setOnItemClickListener();
            } else {
                setNoUpGradeCard();
            }
        } else {
            setNoUpGradeCard();
        }
    }

    private void setNoUpGradeCard() {
        mStateView.initNoDataView(R.drawable.icon_no_data, getString(R.string.no_card_upgrade), getString(R.string.refresh_look));
    }

    private void setOnItemClickListener() {
        mUpgradeContinueCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CardResult.CardData.Card card = mUpgradeContinueCardAdapter.getDataList().get(position);
                if (card != null) {
                    UMengCountUtil.UmengCount(UpgradeAndContinueCardActivity.this, UmengEventId.BUYCARDCONFIRMACTIVITY);
                    Intent intent = new Intent(UpgradeAndContinueCardActivity.this, BuyCardConfirmActivity.class);
                    intent.putExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY, card.getCategoryName());
                    intent.putExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, card.getCategoryId());
                    intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, buyType);
                    intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGymId);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
