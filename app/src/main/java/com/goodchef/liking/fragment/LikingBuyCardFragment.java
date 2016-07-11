package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.mvp.presenter.CardListPresenter;
import com.goodchef.liking.mvp.view.CardListView;
import com.goodchef.liking.storage.Preference;
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
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_BUY_TYPE = "key_buy_type";

    private PullToRefreshRecyclerView mRecyclerView;
    private BuyCardAdapter mBuyCardAdapter;
    private CardListPresenter mCardListPresenter;
    private View mHeadView;
    private static final int TYPE_BUY = 1;
    private TextView mCityOpenTextView;//当前城市是否开通

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
        initView(view);
        initData();
        initRecycleHeadView();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.buy_card_RecyclerView);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
    }

    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_card_item, mRecyclerView, false);
        mCityOpenTextView = (TextView) mHeadView.findViewById(R.id.buy_card_head_text);
        mCityOpenTextView.setVisibility(View.VISIBLE);
        mCityOpenTextView.setText("当前城市尚未开通服务");
    }

    private void initData() {
        mCardListPresenter = new CardListPresenter(getActivity(), this);
        mCardListPresenter.getCardList(TYPE_BUY);
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        List<CardResult.CardData.Card> list = cardData.getCardList();
        if (list != null && list.size() > 0) {
            mBuyCardAdapter = new BuyCardAdapter(getActivity());
            mBuyCardAdapter.setData(list);
            mBuyCardAdapter.setBuyCardListener(null);
            mBuyCardAdapter.setHeaderView(mHeadView);
            mRecyclerView.setAdapter(mBuyCardAdapter);
            mBuyCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CardResult.CardData.Card card = mBuyCardAdapter.getDataList().get(position);
                    if (card != null) {
                        Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
                        intent.putExtra(KEY_CARD_CATEGORY, card.getCategoryName());
                        intent.putExtra(KEY_CATEGORY_ID, card.getCategoryId());
                        intent.putExtra(KEY_BUY_TYPE, 1);
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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        String cityName = mainAddressChanged.getCityName();
        boolean isContains = false;
        List<CityData> cityDataList = Preference.getBaseConfig().getBaseConfigData().getCityList();
        if (cityDataList != null && cityDataList.size() > 0) {
            for (CityData data : cityDataList) {
                if (data.getCityName().contains(cityName)) {
                    isContains = true;
                    break;
                }
            }
            if (isContains) {
                mCityOpenTextView.setVisibility(View.GONE);
            } else {
                mCityOpenTextView.setVisibility(View.VISIBLE);
                mCityOpenTextView.setText("当前城市尚未开通服务");
            }
        }
    }
}
