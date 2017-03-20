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
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.CouponsDetailsActivity;
import com.goodchef.liking.adapter.CouponsGymAdapter;
import com.goodchef.liking.http.result.CouponsCities;
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

    @Override
    protected void requestData(int page) {
        mPresenter.requestCityPage(page, mCouponsCode, CouponsCitysFragment.this);
    }

    @Override
    protected void initViews() {
        setPullMode(PullMode.PULL_BOTH);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCouponsCode = arguments.getString(CouponsDetailsActivity.COUPONS);
        }
        mPresenter = new CouponsCitysPresenter(getContext(), this);

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
        mCityCount.setText("共" + data.getTotal_gym() + "家");
        updateListView(data.getGym_list());
    }
}
