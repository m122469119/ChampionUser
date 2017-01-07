package com.goodchef.liking.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.UnBindDevicesDialog;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.mvp.presenter.UnBindDevicesPresenter;
import com.goodchef.liking.mvp.view.UnBindDevicesView;
import com.goodchef.liking.widgets.MyCustomCircleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:我的手环
 * Author : shaozucheng
 * Time: 下午3:09
 * version 1.0.0
 */

public class MyBraceletActivity extends AppBarActivity implements View.OnClickListener, UnBindDevicesView {
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
    @BindView(R.id.bracelet_power_MyCustomCircleView)
    MyCustomCircleView mBraceletPowerMyCustomCircleView;
    @BindView(R.id.my_power_TextView)
    TextView mMyPowerTextView;

    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBrancePower = 0;
    private UnBindDevicesPresenter mUnBindDevicesPresenter;
    private String myBraceletMac;//我的手环mac地址

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
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
    }

    private void initData() {
        if (!StringUtils.isEmpty(mBindDevicesName)) {
            mCurrentDevicesNameTextView.setText(mBindDevicesName);
        }
        if (!StringUtils.isEmpty(mBindDevicesAddress)) {
            mDevicesAddressTextView.setText(mBindDevicesAddress);
        }
        if (!StringUtils.isEmpty(mBindDevicesAddress)) {
            mDevicesVersionTextView.setText(mFirmwareInfo);
        }
        synchronizationInfo();
    }

    private void setViewOnClickListener() {
        mUnbindTextView.setOnClickListener(this);
    }


    private void synchronizationInfo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBraceletView();
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn);
                mMyBraceletTextView.setText("同步中...");
            }
        }, 2000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBraceletView();
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn_success);
                mMyBraceletTextView.setText("同步完成");
            }
        }, 4000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMyBraceletImageView.setVisibility(View.GONE);
                mMyBraceletTextView.setVisibility(View.GONE);
                mBraceletPowerMyCustomCircleView.setVisibility(View.VISIBLE);
                mMyPowerTextView.setVisibility(View.VISIBLE);
                mBraceletPowerMyCustomCircleView.setCurrentCount(100, mBrancePower);
            }
        }, 5000);
    }

    private void setBraceletView() {
        mMyBraceletImageView.setVisibility(View.VISIBLE);
        mMyBraceletTextView.setVisibility(View.VISIBLE);
        mBraceletPowerMyCustomCircleView.setVisibility(View.GONE);
        mMyPowerTextView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mMyBraceletImageView.getLayoutParams();
        layoutParams.width = DisplayUtils.dp2px(128);
        layoutParams.height = DisplayUtils.dp2px(104);
        mMyBraceletImageView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        if (v == mUnbindTextView) {
            showUnbindDialog();
        }
    }

    /**
     * 展示解除绑定对话框
     */
    private void showUnbindDialog() {
        UnBindDevicesDialog devicesDialog = new UnBindDevicesDialog(this);
        devicesDialog.setCancelClickListener(new UnBindDevicesDialog.CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });

        devicesDialog.setConfirmClickListener(new UnBindDevicesDialog.ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                sendUnBindRequest();
                dialog.dismiss();
            }
        });

    }

    private void sendUnBindRequest() {
        if (mUnBindDevicesPresenter == null) {
            mUnBindDevicesPresenter = new UnBindDevicesPresenter(this, this);
        }
        mUnBindDevicesPresenter.unBindDevices();
    }

    @Override
    public void updateUnBindDevicesView() {
        finish();
    }
}
