package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.mvp.presenter.CardListPresenter;
import com.goodchef.liking.mvp.view.CardListView;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingBuyCardFragment extends BaseFragment implements CardListView {

    public static final String KEY_CARD_CATEGORY = "key_card_category";
    private PullToRefreshRecyclerView mRecyclerView;
    private BuyCardAdapter mBuyCardAdapter;
    private CardListPresenter mCardListPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.buy_card_RecyclerView);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private void initData() {
        mCardListPresenter = new CardListPresenter(getActivity(), this);
        mCardListPresenter.getCardList();
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        List<CardResult.CardData.Card> list = cardData.getCardList();
        if (list != null && list.size() > 0) {
            mBuyCardAdapter = new BuyCardAdapter(getActivity());
            mBuyCardAdapter.setData(list);
            mBuyCardAdapter.setBuyCardListener(null);
            mRecyclerView.setAdapter(mBuyCardAdapter);
            mBuyCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CardResult.CardData.Card Card = mBuyCardAdapter.getDataList().get(position);
                    if (Card != null) {
                        Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
                        intent.putExtra(KEY_CARD_CATEGORY, Card.getCategoryName());
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


}
