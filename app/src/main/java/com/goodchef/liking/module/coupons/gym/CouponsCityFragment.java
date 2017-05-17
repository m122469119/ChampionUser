package com.goodchef.liking.module.coupons.gym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CouponsGymAdapter;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.module.coupons.details.CouponsDetailsActivity;
import com.goodchef.liking.widgets.decoration.CouponsDecoration;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCityFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CouponsGymContract.CouponsGymView {

    TextView mCityCount;
    CouponsGymAdapter mAdapter;
    CouponsGymContract.CouponsGymPresenter mPresenter;

    public String mCouponsCode;

    public static CouponsCityFragment newInstance(String couponsCode) {
        Bundle args = new Bundle();
        args.putString(CouponsDetailsActivity.COUPONS, couponsCode);
        CouponsCityFragment fragment = new CouponsCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        mPresenter.getCouponsCitys(page,mCouponsCode);
    }

    @Override
    protected void initViews() {
        setPullMode(PullMode.PULL_BOTH);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCouponsCode = arguments.getString(CouponsDetailsActivity.COUPONS);
        }
        mPresenter = new CouponsGymContract.CouponsGymPresenter(getActivity(),this);

        mAdapter = new CouponsGymAdapter(getContext());
        View head = LayoutInflater.from(getContext()).inflate(R.layout.head_coupons_view, null);
        mCityCount = (TextView) head.findViewById(R.id.txt_head_coupons_count);
        mAdapter.addHeaderView(head);
        CouponsDecoration decoration = new CouponsDecoration(getContext(),
                CouponsDecoration.VERTICAL_LIST,
                R.drawable.coupons_head_line,
                R.drawable.coupons_content_line,
                R.drawable.coupons_head_line);
        setRecyclerAdapter(mAdapter);
        addRecyclerItemDecoration(decoration);
    }

    @Override
    public void updateCouponData(CouponsCities.DataBean data) {
        mCityCount.setText(getString(R.string.common) + data.getTotalGym() + getString(R.string.home));
        updateListView(data.getGymList());
    }
}
