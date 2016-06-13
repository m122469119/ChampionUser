package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingBuyCardFragment extends BaseFragment {

    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.buy_card_listView);
        mPullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
    }


}
