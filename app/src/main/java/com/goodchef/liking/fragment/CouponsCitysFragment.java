package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.CouponsDetailsActivity;
import com.goodchef.liking.adapter.CouponsGymAdapter;
import com.goodchef.liking.mvp.presenter.CouponsCitysPresenter;
import com.goodchef.liking.mvp.view.CouponsCitysView;
import com.goodchef.liking.widgets.decoration.CouponsDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCitysFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CouponsCitysView {

    @BindView(R.id.rv_fragment_coupons_citys)
    RecyclerView mRecyclerView;
    TextView mCityCount;
    CouponsGymAdapter mAdapter;
    CouponsCitysPresenter mPresenter;

    public String mCouponsCode;

    public static CouponsCitysFragment newInstance(String couponsCode) {
        Bundle args = new Bundle();
        args.putString(CouponsDetailsActivity.COUPONS, couponsCode);
        CouponsCitysFragment fragment = new CouponsCitysFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupons_citys, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            mCouponsCode = savedInstanceState.getString(CouponsDetailsActivity.COUPONS);
        }
        mPresenter = new CouponsCitysPresenter(getContext(), this);
        return view;
    }

    @Override
    protected void requestData(int page) {
        mPresenter.requestCityPage(page);
    }

    @Override
    protected void initViews() {
        mAdapter = new CouponsGymAdapter(getContext());
        View head = LayoutInflater.from(getContext()).inflate(R.layout.head_coupons_view, null);
        mCityCount = (TextView) head.findViewById(R.id.txt_head_coupons_count);
        mAdapter.addHeaderView(head);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CouponsDecoration decoration = new CouponsDecoration(getContext(),
                CouponsDecoration.VERTICAL_LIST,
                R.drawable.coupons_head_line,
                R.drawable.coupons_content_line,
                R.drawable.coupons_head_line);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
    }
}
