package com.goodchef.liking.module.home.myfragment.water;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.WaterRateAdapter;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;
import com.goodchef.liking.widgets.itemdecoration.DividerGridItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaterRateActivity extends AppBarMVPSwipeBackActivity<WaterRateContract.Presenter> implements WaterRateContract.View {


    @BindView(R.id.rv_water_rate)
    RecyclerView mWaterRate;

    View mAlipayView, mWechatView;

    CheckBox mAlipayCheckBox, mWechatCheckBox;

    WaterRateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_rate);
        ButterKnife.bind(this);
        setTitle(R.string.layout_water_rate);
        initView();
    }

    private void initView() {
        mAdapter = new WaterRateAdapter(this);

        View header = LayoutInflater.from(this).inflate(R.layout.head_water_rate, null);
        View floor = LayoutInflater.from(this).inflate(R.layout.floor_water_rate, null);

        mAlipayView = floor.findViewById(R.id.layout_alipay);
        mWechatView = floor.findViewById(R.id.layout_wechat);

        mAlipayCheckBox = (CheckBox) floor.findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) floor.findViewById(R.id.pay_type_wechat_checkBox);

        mAlipayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlipayCheckBox.setChecked(true);
                mWechatCheckBox.setChecked(false);
                mPresenter.savePayWay(WaterRateModel.PayWay.ALIPAY);
            }
        });

        mWechatCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlipayCheckBox.setChecked(false);
                mWechatCheckBox.setChecked(true);
                mPresenter.savePayWay(WaterRateModel.PayWay.WECHAT);
            }
        });

        mAdapter.setHeaderView(header);
        mAdapter.setFooterView(floor);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                mPresenter.onItemClick(position);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mWaterRate.setLayoutManager(gridLayoutManager);

        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(ContextCompat.getDrawable(this, R.drawable.water_h_line));
        mWaterRate.addItemDecoration(itemDecoration);
        mWaterRate.setAdapter(mAdapter);
        mPresenter.setWaterAdapter();


    }

    @OnClick(R.id.immediately_buy_btn)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.immediately_buy_btn:
                mPresenter.buyWaterRate();
                break;
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new WaterRateContract.Presenter();
    }

    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setWaterAdapter(List<WaterRateResult.WaterListBean> list) {
        mAdapter.setDatas(list);
        mAdapter.notifyDataSetChanged();
    }
}
