package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.BaseFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.DishesDetailsActivity;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingNearbyFragment extends BaseFragment {

    private PullToRefreshRecyclerView mRecyclerView;
    private LikingNearbyAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_nearby, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.nearby_recyclerView);
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int  i=20;i<50;i++){
            list.add(""+i);
        }
        mAdapter = new LikingNearbyAdapter(getActivity());
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LikingNearbyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                Intent intent = new Intent(getActivity(), DishesDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
