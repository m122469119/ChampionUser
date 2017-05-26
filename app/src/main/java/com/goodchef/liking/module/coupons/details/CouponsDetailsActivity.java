package com.goodchef.liking.module.coupons.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CouponsDetailsAdapter;
import com.goodchef.liking.data.remote.retrofit.result.CouponsDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsPersonResult;
import com.goodchef.liking.module.coupons.gym.CouponsGymActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDetailsActivity extends AppBarActivity implements CouponDetailsContract.CouponDetailsView {

    public static final String ACTION_SHOW_DETAILS = "show_details";
    public static final String COUPONS = "coupons";
    @BindView(R.id.rv_activity_coupons_contains)
    RecyclerView mRecycleView;

    CouponDetailsContract.CouponDetailsPresenter mCouponDetailsPresenter;
    CouponsPersonResult.DataBean.CouponListBean mBean;
    public CouponsDetailsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_details);
        mCouponDetailsPresenter = new CouponDetailsContract.CouponDetailsPresenter(this, this);
        ButterKnife.bind(this);
        setTitle(getString(R.string.coupons_details));
        Intent intent = getIntent();
        if (intent.getAction().equals(ACTION_SHOW_DETAILS)) {
            mBean = (CouponsPersonResult.DataBean.CouponListBean) intent.getSerializableExtra(COUPONS);
            initView();
            initData(savedInstanceState);
        }
    }

    private void initView() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CouponsDetailsAdapter(this);
        mAdapter.setOnReadAllClickListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CouponsDetailsActivity.this, CouponsGymActivity.class);
                intent.setAction(CouponsGymActivity.ACTION_SHOW_GYM);
                intent.putExtra(CouponsGymActivity.COUPONS_CODE, mBean.getCoupon_code());
                startActivity(intent);
            }
        });
        mRecycleView.setAdapter(mAdapter);
    }

    private void initData(Bundle savedInstanceState) {
        mCouponDetailsPresenter.getCouponsDetails(mBean.getCoupon_code());
    }

    @Override
    public void updateCouponData(CouponsDetailsResult.DataBean couponData) {
        mAdapter.setCouponData(couponData);
        mAdapter.notifyDataSetChanged();
    }
}
