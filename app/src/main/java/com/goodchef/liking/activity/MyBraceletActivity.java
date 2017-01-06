package com.goodchef.liking.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.DisplayUtils;
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
    public static final String KEY_BRACELET_NAME = "key_bracelet_name";
    public static final String KEY_BRACELET_ADDRESS = "key_bracelet_address";
    public static final String KEY_BRACELET_FIRMWARE_INFO = "key_bracelet_firmware_info";
    public static final String KEY_BRACELET_POWER = "key_bracelet_power";

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

    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBrancePower;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bracelet);
        ButterKnife.bind(this);
        setTitle("我的手环");
        getInitData();
        setViewOnClickListener();
        initData();
    }

    private void getInitData() {
        mBindDevicesName = getIntent().getStringExtra(KEY_BRACELET_NAME);
        mBindDevicesAddress = getIntent().getStringExtra(KEY_BRACELET_ADDRESS);
        mFirmwareInfo = getIntent().getStringExtra(KEY_BRACELET_FIRMWARE_INFO);
        mBrancePower = getIntent().getIntExtra(KEY_BRACELET_POWER, 0);
    }

    private void initData() {
        mCurrentDevicesNameTextView.setText(mBindDevicesName);
        mDevicesAddressTextView.setText(mBindDevicesAddress);
        mDevicesVersionTextView.setText(mFirmwareInfo);
        synchronizationInfo();
    }

    private void setViewOnClickListener() {
        mUnbindTextView.setOnClickListener(this);
    }


    private void synchronizationInfo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mMyBraceletImageView.getLayoutParams();
                layoutParams.width = DisplayUtils.dp2px(128);
                layoutParams.height = DisplayUtils.dp2px(104);
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn);
                mMyBraceletImageView.setLayoutParams(layoutParams);
                mMyBraceletTextView.setText("同步中...");
            }
        }, 2000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mMyBraceletImageView.getLayoutParams();
                layoutParams.width = DisplayUtils.dp2px(128);
                layoutParams.height = DisplayUtils.dp2px(104);
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn_success);
                mMyBraceletTextView.setText("同步完成");
            }
        }, 4000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn_success);
                mMyBraceletTextView.setText(mBrancePower + "");
            }
        }, 5000);
    }

    @Override
    public void onClick(View v) {
        if (v == mUnbindTextView) {
            PopupUtils.showToast("解除绑定");
        }
    }
}
