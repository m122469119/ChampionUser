package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.UpgradeContinueCardAdapter;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.mvp.presenter.CardListPresenter;
import com.goodchef.liking.mvp.view.CardListView;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.List;

/**
 * 说明:升级卡或者续卡
 * Author shaozucheng
 * Time:16/6/30 上午10:02
 */
public class UpgradeAndContinueCardActivity extends AppBarActivity implements CardListView {

    private PullToRefreshRecyclerView mRecyclerView;
    private int buyType;
    private String title;

    private UpgradeContinueCardAdapter mUpgradeContinueCardAdapter;
    private CardListPresenter mCardListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_and_continue);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.upgrade_and_continue_recycleView);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
    }

    private void initData() {
        buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        title = getIntent().getStringExtra(MyCardActivity.KEY_INTENT_TITLE);
        setTitle(title);
        mCardListPresenter = new CardListPresenter(this, this);
        mCardListPresenter.getCardList(buyType);
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        List<CardResult.CardData.Card> list = cardData.getCardList();
        if (list != null && list.size() > 0) {
            mUpgradeContinueCardAdapter = new UpgradeContinueCardAdapter(this);
            mUpgradeContinueCardAdapter.setData(list);
            mRecyclerView.setAdapter(mUpgradeContinueCardAdapter);
            setOnItemClickListener();
        }
    }

    private void  setOnItemClickListener(){
        mUpgradeContinueCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CardResult.CardData.Card card = mUpgradeContinueCardAdapter.getDataList().get(position);
                if (card != null) {
                    Intent intent = new Intent(UpgradeAndContinueCardActivity.this, BuyCardConfirmActivity.class);
                    intent.putExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY, card.getCategoryName());
                    intent.putExtra(LikingBuyCardFragment.KEY_CATEGORY_ID,card.getCategoryId());
                    intent.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE,buyType);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }
}
