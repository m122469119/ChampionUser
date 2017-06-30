package com.goodchef.liking.module.home.myfragment.water;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.WaterRateAdapter;
import com.goodchef.liking.data.remote.retrofit.result.WaterOrderResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;
import com.goodchef.liking.eventmessages.WaterRateActivityMessage;
import com.goodchef.liking.widgets.itemdecoration.DividerGridItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaterRateActivity extends AppBarMVPSwipeBackActivity<WaterRateContract.Presenter> implements WaterRateContract.View {


    @BindView(R.id.rv_water_rate)
    RecyclerView mWaterRate;

    @BindView(R.id.water_state)
    StateView mStateView;

    View mAlipayView, mWechatView;

    CheckBox mAlipayCheckBox, mWechatCheckBox;

    WaterRateAdapter mAdapter;

    TextView mGymName;
    TextView mResidueWaterTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_rate);
        ButterKnife.bind(this);
        setTitle(R.string.layout_water_rate);
        initView();
        mPresenter.getWaterAll();
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                mPresenter.getWaterAll();
            }
        });
    }

    private void initView() {
        mAdapter = new WaterRateAdapter(this);

        View header = LayoutInflater.from(this).inflate(R.layout.head_water_rate, null);
        View floor = LayoutInflater.from(this).inflate(R.layout.floor_water_rate, null);

        mGymName = (TextView) header.findViewById(R.id.gym_name);
        mResidueWaterTime = (TextView) header.findViewById(R.id.residue_water_time);

        mAlipayView = floor.findViewById(R.id.layout_alipay);
        mWechatView = floor.findViewById(R.id.layout_wechat);

        mAlipayCheckBox = (CheckBox) floor.findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) floor.findViewById(R.id.pay_type_wechat_checkBox);

        mAlipayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlipayCheckBox.setChecked(true);
                mWechatCheckBox.setChecked(false);
                mPresenter.savePayWay(WaterOrderResult.DataBean.ALIPAY);
            }
        });

        mWechatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlipayCheckBox.setChecked(false);
                mWechatCheckBox.setChecked(true);
                mPresenter.savePayWay(WaterOrderResult.DataBean.WECHATPAY);
            }
        });

        mAlipayView.performClick();

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
                mPresenter.buyWaterRate(this);
                break;
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new WaterRateContract.Presenter(this);
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setWaterAdapter(List<WaterRateResult.DataBean.WaterListBean> list) {
        mAdapter.setDatas(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateInfoData(WaterRateResult.DataBean data) {
        mGymName.setText(data.getGym_name());
        mResidueWaterTime.setText(data.getResidue_water_time() + getString(R.string.min));
        mPresenter.setWaterAdapter();
    }


    public void onEvent(WaterRateActivityMessage message){
        switch (message.what) {
            case WaterRateActivityMessage.WECHAT_PAY:
                boolean isSuccess = (boolean) message.obj1;
                if (isSuccess) {
                    showToast("支付成功");
                } else {
                    showToast("支付失败");
                }
                break;


        }
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }
}
