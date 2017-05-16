package com.goodchef.liking.module.brace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleService;
import com.goodchef.liking.dialog.CancelOnClickListener;
import com.goodchef.liking.dialog.ConfirmOnClickListener;
import com.goodchef.liking.dialog.UnBindDevicesDialog;
import com.goodchef.liking.eventmessages.ServiceConnectionMessage;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.widgets.MyCustomCircleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 说明:我的手环
 * Author : shaozucheng
 * Time: 下午3:09
 * version 1.0.0
 */

public class MyBraceletActivity extends AppBarActivity implements MyBraceContract.MyBraceView {
    public static final String KEY_BRACELET_NAME = "key_bracelet_name";
    public static final String KEY_BRACELET_ADDRESS = "key_bracelet_address";
    public static final String KEY_BRACELET_FIRMWARE_INFO = "key_bracelet_firmware_info";
    public static final String KEY_BRACELET_POWER = "key_bracelet_power";
    public static final String KEY_BRACELET_SOURCE = "key_bracelet_source";

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
    @BindView(R.id.bluetooth_scan_fail_TextView)
    TextView mBluetoothScanFailTextView;
    @BindView(R.id.bluetooth_scan_fail_retry_TextView)
    TextView mBluetoothScanFailRetryTextView;
    @BindView(R.id.layout_bluetooth_connect_fail)
    RelativeLayout mLayoutBluetoothConnectFail;

    private MyBraceContract.MyBracePresenter mPresenter;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_my_bracelet));
        mPresenter = new MyBraceContract.MyBracePresenter(this, this);
        getInitData();
        mPresenter.initBlueTooth(this);
        if (mPresenter.getSource().equals("BingBraceletActivity")) {
            mMyBraceletTextView.setText(R.string.binding_finish);
            synchronizationInfo();
            initData();
            mUnbindTextView.setVisibility(View.VISIBLE);
        } else {
            mPresenter.searchBlueTooth(this);
            mUnbindTextView.setVisibility(View.GONE);
            mMyBraceletImageView.setBackgroundResource(R.drawable.icon_my_blue_tooth);
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    private void getInitData() {
        mPresenter.setBindDevicesName(getIntent().getStringExtra(KEY_BRACELET_NAME));
        mPresenter.setBindDevicesAddress(getIntent().getStringExtra(KEY_BRACELET_ADDRESS));
        mPresenter.setFirmwareInfo(getIntent().getStringExtra(KEY_BRACELET_FIRMWARE_INFO));
        mPresenter.setBraceletPower(getIntent().getIntExtra(KEY_BRACELET_POWER, 0));
        mPresenter.setMyBraceletMac(getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC));
        mPresenter.setUUID(getIntent().getStringExtra(LikingMyFragment.KEY_UUID));
        mPresenter.setSource(getIntent().getStringExtra(KEY_BRACELET_SOURCE));
    }

    private void initData() {
        if (!StringUtils.isEmpty(mPresenter.getBindDevicesName())) {
            mCurrentDevicesNameTextView.setText(mPresenter.getBindDevicesName());
        }
        if (!StringUtils.isEmpty(mPresenter.getBindDevicesAddress())) {
            mDevicesAddressTextView.setText(mPresenter.getBindDevicesAddress());
        }
        if (!StringUtils.isEmpty(mPresenter.getFirmwareInfo())) {
            mDevicesVersionTextView.setText(mPresenter.getFirmwareInfo());
        }
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(ServiceConnectionMessage message) {
        if (!mPresenter.getSource().equals("BingBraceletActivity")
                && mPresenter.initBlueTooth(this)
                && !StringUtils.isEmpty(mPresenter.getMyBraceletMac())) {
            mPresenter.connect(this);
        }
    }

    private void synchronizationInfo() {
        setOnSynchronizationView();
        setSynchronizationSuccessView(2000);
        setSynchronizationPowerView(3000);
    }

    /**
     * 设置同步中view
     */
    private void setOnSynchronizationView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBraceletView();
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn);
                mMyBraceletTextView.setText(R.string.synchronization_ing);
            }
        }, 1000);
    }

    /**
     * 同步完成
     */
    private void setSynchronizationSuccessView(int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBraceletView();
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_syn_success);
                mMyBraceletTextView.setText(R.string.synchongrozation_finish);
            }
        }, time);
    }

    @Override
    public void setSynchronizationPowerView(int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMyBraceletImageView.setVisibility(View.GONE);
                mMyBraceletTextView.setVisibility(View.GONE);
                mBraceletPowerMyCustomCircleView.setVisibility(View.VISIBLE);
                mMyPowerTextView.setVisibility(View.VISIBLE);
                mBraceletPowerMyCustomCircleView.setCurrentCount(100, mPresenter.getBraceletPower());
            }
        }, time);
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




    /**
     * 在连接之前判断是否蓝牙断开，此时view的展示
     */
    @Override
    public void beforeConnectBlueToothView(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBraceletPowerMyCustomCircleView.setVisibility(View.GONE);
                mMyPowerTextView.setVisibility(View.GONE);
                mMyBraceletImageView.setVisibility(View.VISIBLE);
                mMyBraceletImageView.setBackgroundResource(R.drawable.icon_my_blue_tooth);
                mLayoutBluetoothConnectFail.setVisibility(View.VISIBLE);
                mBluetoothScanFailTextView.setText(string);
                mBluetoothScanFailRetryTextView.setText(R.string.retry);
                mUnbindTextView.setVisibility(View.GONE);
            }
        });
        setConnectView(getString(R.string.connect_fial));
    }

    /**
     * 设置连接是的view
     */
    @Override
    public void setConnectView(final String connectStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMyBraceletTextView.setVisibility(View.VISIBLE);
                mMyBraceletTextView.setText(connectStr);
                mCurrentDevicesNameTextView.setText("--");
                mDevicesAddressTextView.setText("--");
                mDevicesVersionTextView.setText("--");
            }
        });
    }

    @Override
    public void showLoginFail() {
        if (isFinishing()) {
            return;
        }
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.bluetooth_login_fail));
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.sendLogin();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 第一次绑定成功后提示用户在我的界面查看蓝牙数据
     */
    @Override
    public void showFirstCheckPromptDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_check_blue_tooth_data) + "\n" + getString(R.string.prompt_check_blue_tooth_data_send));
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        LikingPreference.setFirstBindBracelet(false);
    }

    @OnClick({R.id.unbind_TextView,
            R.id.bluetooth_scan_fail_retry_TextView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unbind_TextView:
                showUnbindDialog();
                break;
            case R.id.bluetooth_scan_fail_retry_TextView://重试 -- 连接蓝牙
                if (!mPresenter.initBlueTooth(this)) {
                    return;
                }
                if (!StringUtils.isEmpty(mPresenter.getMyBraceletMac())) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayoutBluetoothConnectFail.setVisibility(View.GONE);
                            mPresenter.setIsConnect(false);
                            mPresenter.connect(MyBraceletActivity.this);
                        }
                    }, 100);
                }
                break;
        }
    }

    /**
     * 展示解除绑定对话框
     */
    private void showUnbindDialog() {
        UnBindDevicesDialog devicesDialog = new UnBindDevicesDialog(this);
        devicesDialog.setCancelClickListener(new CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });

        devicesDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                sendUnBindRequest();
                dialog.dismiss();
            }
        });

    }

    private void sendUnBindRequest() {
        mPresenter.unBindDevices(JPushInterface.getUdid(this));
    }

    @Override
    public void updateUnBindDevicesView() {
        mPresenter.pauseBlue();
        LikingPreference.setIsBind("0");
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pauseBlue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mPresenter.releaseBlue();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "连接成功");
                mPresenter.setConnectionState(true);
                mPresenter.setConnectState(2);
                mPresenter.setBluetoothDevice();
                mPresenter.setConnectSuccessView();
                mPresenter.discoverServicesBlue();
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mPresenter.setConnectionState(false);
                mPresenter.setIsScanDevices(false);
                mPresenter.setConnectState(0);
                LogUtils.i(TAG, "连接失败");
                mPresenter.sendConnect(MyBraceletActivity.this);
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //在这里可以对服务进行解析，寻找到你需要的服务
                mPresenter.getBlueToothServices();
            } else if (BleService.ACTION_CHARACTERISTIC_CHANGED.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BleService.EXTRA_DATA);
                if (data != null) {
                    System.out.println("收到通知:");
                }
                for (int i = 0; i < data.length; i++) {
                    LogUtils.i(TAG, " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
                }
                if (!mPresenter.isGetAllData()) {
                    mPresenter.doCharacteristicData(data);
                }
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_CHARACTERISTIC_CHANGED);
        return intentFilter;
    }


    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void setLayoutBluetoothConnectFailVisibility(int visibility) {
        mLayoutBluetoothConnectFail.setVisibility(visibility);
    }

    @Override
    public void setDevicesVersionTextViewText(String text) {
        mDevicesVersionTextView.setText(text);
    }

    @Override
    public void setCurrentDevicesNameTextViewText(String text) {
        mCurrentDevicesNameTextView.setText(text);
    }

    @Override
    public void setDevicesAddressTextViewText(String text) {
        mDevicesAddressTextView.setText(text);
    }

    @Override
    public void setUnbindTextViewVisibility(int visibility) {
        mUnbindTextView.setVisibility(visibility);
    }

    @Override
    public void setMyBraceletTextViewText(String text) {
        mMyBraceletTextView.setText(text);
    }


}
