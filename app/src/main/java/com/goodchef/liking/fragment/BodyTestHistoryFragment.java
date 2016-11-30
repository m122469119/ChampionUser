package com.goodchef.liking.fragment;

import android.os.Bundle;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BodyTestHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:12
 * version 1.0.0
 */

public class BodyTestHistoryFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment {

    private BodyTestHistoryAdapter mBodyTestHistoryAdapter;

    public static BodyTestHistoryFragment newInstance() {
        Bundle args = new Bundle();
        BodyTestHistoryFragment fragment = new BodyTestHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        getStateView().setState(StateView.State.SUCCESS);
        List<String> list = new ArrayList<>();
        for (int i = 40; i < 70; i++) {
            list.add(i + "");
        }
        getRecyclerView().setBackgroundColor(ResourceUtils.getColor(R.color.app_content_background));
        if (mBodyTestHistoryAdapter == null) {
            mBodyTestHistoryAdapter = new BodyTestHistoryAdapter(getActivity());
        }
        mBodyTestHistoryAdapter.setData(list);
        setRecyclerAdapter(mBodyTestHistoryAdapter);
    }

    @Override
    protected void initViews() {

    }
}
