package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:我的手环
 * Author : shaozucheng
 * Time: 下午3:09
 * version 1.0.0
 */

public class MyBraceletActivity extends AppBarActivity implements View.OnClickListener {


    @BindView(R.id.my_bracelet_ImageView)
    ImageView mMyBraceletImageView;
    @BindView(R.id.my_bracelet_TextView)
    TextView mMyBraceletTextView;
    @BindView(R.id.current_devices_name_TextView)
    TextView mCurrentDevicesNameTextView;
    @BindView(R.id.devices_address_TextView)
    TextView mDevicesAddressTextView;
    @BindView(R.id.devices_version_TextView)
    TextView mDevicesVersionTextView;
    @BindView(R.id.unbind_TextView)
    TextView mUnbindTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bracelet);
        ButterKnife.bind(this);
        setTitle("我的手环");
        setViewOnClickListener();
    }

    private void setViewOnClickListener() {
        mUnbindTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mUnbindTextView) {
            PopupUtils.showToast("解除绑定");
        }
    }
}
