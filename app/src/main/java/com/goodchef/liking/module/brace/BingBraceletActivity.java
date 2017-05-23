package com.goodchef.liking.module.brace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleService;
import com.goodchef.liking.module.home.myfragment.LikingMyFragment;
import com.goodchef.liking.widgets.RoundImageView;
import com.goodchef.liking.widgets.WhewView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:绑定手环
 * Author : shaozucheng
 * Time: 下午3:28
 * version 1.0.0
 */

public class BingBraceletActivity extends AppBarActivity implements BindBraceContract.BindBraceView {

    @BindView(R.id.blue_tooth_WhewView)
    WhewView mBlueToothWhewView;
    @BindView(R.id.blue_tooth_RoundImageView)
    RoundImageView mBlueToothRoundImageView;
    @BindView(R.id.click_search_TextView)
    TextView mClickSearchTextView;
    @BindView(R.id.layout_blue_tooth_bracelet)
    LinearLayout mLayoutBlueToothBracelet;
    @BindView(R.id.open_blue_tooth_TextView)
    TextView mOpenBlueToothTextView;
    @BindView(R.id.layout_blue_open_state)
    RelativeLayout mLayoutBlueOpenState;
    @BindView(R.id.blue_tooth_name_TextView)
    TextView mBlueToothNameTextView;
    @BindView(R.id.connect_blue_tooth_TextView)
    TextView mConnectBlueToothTextView;
    @BindView(R.id.layout_blue_booth)
    RelativeLayout mLayoutBlueBooth;
    @BindView(R.id.connect_bluetooth_ProgressBar)
    ProgressBar mConnectBluetoothProgressBar;
    @BindView(R.id.bluetooth_state_TextView)
    TextView mBluetoothStateTextView;
    @BindView(R.id.no_search_devices_TextView)
    TextView mNoSearchDevicesTextView;

    private Handler mHandler = new Handler();
    private boolean connectFail = false;//是否连接失败
    private int clickSearch;
    private boolean isSearchSuccess = false;
    //private BleManager mBleManager;
    private int connectState;

    BindBraceContract.BindBracePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        mPresenter = new BindBraceContract.BindBracePresenter(this, this);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));

        mPresenter.setBraceletMac(getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC));
        mPresenter.setMuuid(getIntent().getStringExtra(LikingMyFragment.KEY_UUID));

        showPromptDialog();
        clickSearch = 0;
        setRightIcon(R.drawable.icon_blue_tooth_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectState == 1) {
                    showToast(getString(R.string.connect_not_jump_help));
                    return;
                }
                startActivity(BlueToothHelpActivity.class);
            }
        });

        if (!mPresenter.isBleManagerOpen()) {
            noOpenBlueToothView();
        }

        mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
        mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
        mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_CHARACTERISTIC_CHANGED);
        return intentFilter;
    }

    /**
     * 没有打开蓝牙
     */
    private void noOpenBlueToothView() {
        mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
        mPresenter.openBluetooth(this);
        mLayoutBlueOpenState.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setText(R.string.bluetooth_no_open);
        mOpenBlueToothTextView.setVisibility(View.VISIBLE);
        mOpenBlueToothTextView.setText(R.string.open_bluetooth);
        mOpenBlueToothTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
    }

    /**
     * 展示蓝牙提示
     */
    private void showPromptDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_one_content, null, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        builder.setCustomView(view);
        titleTextView.setText(getString(R.string.notice_prompt));
        contentTextView.setText(getString(R.string.send_brancelet_bing_prompt_left) + "\n"
                + getString(R.string.send_brancelet_bing_prompt_middle)
                + "\n" + getString(R.string.send_brancelet_bing_prompt_right));
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseBleConnect();
    }


    @OnClick({R.id.blue_tooth_RoundImageView,
            R.id.open_blue_tooth_TextView,
            R.id.connect_blue_tooth_TextView})
    public void onClick(View v) {
        if (v == mBlueToothRoundImageView) {
            startSearchBlueTooth();
        } else if (v == mOpenBlueToothTextView) {
            String open = mOpenBlueToothTextView.getText().toString();
            if (open.equals(getString(R.string.open_bluetooth)) || !mPresenter.isBleManagerOpen()) {
                mPresenter.openBluetooth(this);
            } else {
                startSearchBlueTooth();
            }
        } else if (v == mConnectBlueToothTextView) {//连接蓝牙
            if ( !mPresenter.isBleManagerOpen()) {
                mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
                mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
                mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
                noOpenBlueToothView();
                return;
            }
            if (mPresenter.isConnectionState()) {
                mPresenter.sendLogin();
            } else {
                mConnectBlueToothTextView.setText(getString(R.string.connect_bluetooth_ing));
                mConnectBluetoothProgressBar.setVisibility(View.VISIBLE);
                mPresenter.connectBlueTooth();
                connectState = 1;
                mConnectBlueToothTextView.setEnabled(false);
            }
        }
    }

    /**
     * 开启搜索蓝牙
     */
    private void startSearchBlueTooth() {
        if ( !mPresenter.isBleManagerOpen()) {
            mLayoutBlueOpenState.setVisibility(View.VISIBLE);//您的设备界面初始化隐藏
            mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
            mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
            noOpenBlueToothView();
            return;
        }
        mLayoutBlueBooth.setVisibility(View.GONE);
        mLayoutBlueOpenState.setVisibility(View.GONE);
        searchBlueTooth();
    }


    /**
     * 搜索蓝牙
     */
    private void searchBlueTooth() {
        if (mBlueToothWhewView.isStarting()) {
            //如果动画正在运行就停止，否则就继续执行
            mBlueToothWhewView.stop();
            //结束进程
            mLayoutBlueOpenState.setVisibility(View.GONE);
            mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);
            mLayoutBlueBooth.setVisibility(View.GONE);
            scanLeDevice(false);
        } else {
            mPresenter.bluetoothDeviceListClear();//清空装载蓝牙的集合
            // 执行动画
            mBlueToothWhewView.start();
            mClickSearchTextView.setText(R.string.searching);

            mLayoutBlueOpenState.setVisibility(View.GONE);
            mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);
            mLayoutBlueBooth.setVisibility(View.GONE);

            scanLeDevice(true);
            clickSearch++;//记录搜索的次数
        }
    }


    public void scanLeDevice(final boolean enable) {
        mPresenter.isBleManagerDoScan(enable);
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isSearchSuccess) {
                        if (clickSearch == 3) {
                            mNoSearchDevicesTextView.setVisibility(View.VISIBLE);
                            clickSearch = 0;
                        } else {
                            mNoSearchDevicesTextView.setVisibility(View.GONE);
                        }
                        mLayoutBlueOpenState.setVisibility(View.VISIBLE);
                        mLayoutBlueBooth.setVisibility(View.GONE);
                        mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);

                        mBluetoothStateTextView.setVisibility(View.VISIBLE);
                        mBluetoothStateTextView.setText(R.string.member_bluetooth_devices);//会员的设备提示文案
                        mOpenBlueToothTextView.setVisibility(View.VISIBLE);
                        mOpenBlueToothTextView.setText(R.string.no_search_bluetooth_devices);//展示没有搜到的文案
                        mOpenBlueToothTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                        mBlueToothWhewView.stop();
                        mClickSearchTextView.setText(R.string.click_search);
                    }
                }
            }, 45000); //45秒后停止搜索
        } else {
            mBlueToothWhewView.stop();
            mClickSearchTextView.setText(R.string.click_search);
        }
    }

    @Override
    public void stopBlueToothWhewView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //1.5s后停止动画
                mBlueToothWhewView.stop();
            }
        });
    }

    @Override
    public void setLayoutBlueOpenStateVisibility(int visibility) {
        mLayoutBlueOpenState.setVisibility(visibility);
    }

    @Override
    public void setLayoutBlueToothBraceletVisibility(int visibility) {
        mLayoutBlueToothBracelet.setVisibility(visibility);
    }

    @Override
    public void setLayoutBlueBoothVisibility(int visibility) {
        mLayoutBlueBooth.setVisibility(visibility);
    }

    @Override
    public void setOpenBlueToothTextViewVisibility(int visibility) {
        mOpenBlueToothTextView.setVisibility(visibility);
    }

    @Override
    public void setConnectBluetoothProgressBarVisibility(int visibility) {
        mConnectBluetoothProgressBar.setVisibility(visibility);
    }

    @Override
    public void setClickSearchTextViewText(String text) {
        mClickSearchTextView.setText(text);
    }

    @Override
    public void setBluetoothStateTextViewText(String text) {
        mBluetoothStateTextView.setText(text);
    }

    @Override
    public void setBlueToothNameTextViewText(String text) {
        mBlueToothNameTextView.setText(text);
    }

    @Override
    public void setConnectBlueToothTextViewText(String text) {
        mConnectBlueToothTextView.setText(text);
    }

    @Override
    public void setConnectBlueToothTextViewEnable(boolean isEnable) {
        mConnectBlueToothTextView.setEnabled(isEnable);
    }

    @Override
    public boolean isBlueToothWhewViewStarting() {
        return mBlueToothWhewView.isStarting();
    }

    @Override
    public void blueToothWhewViewStop() {
        mBlueToothWhewView.stop();
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!mPresenter.isBleManagerOpen()) {//连接之前先判断蓝牙的状态
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
                    mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
                    mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
                    noOpenBlueToothView();
                }
            });
            return;
        }
        if (!connectFail) {
            connectFail = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPresenter.connectBlueTooth();
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);//展示会员的设备
                    mLayoutBlueToothBracelet.setVisibility(View.GONE);//隐藏搜索提示
                    mLayoutBlueBooth.setVisibility(View.VISIBLE);
                    mConnectBluetoothProgressBar.setVisibility(View.VISIBLE);
                    mConnectBlueToothTextView.setText(R.string.connect_bluetooth_ing);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    beforeConnectBlueToothView();
                }
            });
        }
    }

    private void beforeConnectBlueToothView() {
        mLayoutBlueOpenState.setVisibility(View.VISIBLE);//展示会员的设备
        mLayoutBlueToothBracelet.setVisibility(View.GONE);//隐藏搜索提示
        mLayoutBlueBooth.setVisibility(View.VISIBLE);

        mBluetoothStateTextView.setVisibility(View.GONE);
        mOpenBlueToothTextView.setVisibility(View.VISIBLE);
        mOpenBlueToothTextView.setText(R.string.connect_fial);

        mConnectBluetoothProgressBar.setVisibility(View.GONE);//连接的动画关闭
        mConnectBlueToothTextView.setText(R.string.connect_again);//展示连接文案

        mBluetoothStateTextView.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setText(R.string.member_bluetooth_devices);//会员的设备提示文案
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                LogUtils.i("BleService", "连接成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOpenBlueToothTextView.setVisibility(View.GONE);
                        mConnectBluetoothProgressBar.setVisibility(View.GONE);
                    }
                });
                mPresenter.setConnectionState(true);
                connectState = 2;
                mPresenter.bleManagerDiscoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mPresenter.setConnectionState(false);
                connectState = 0;
                LogUtils.i("BleService", "连接失败");
                sendConnect();
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //在这里可以对服务进行解析，寻找到你需要的服务
                mPresenter.getBlueToothServices();
            } else if (BleService.ACTION_CHARACTERISTIC_CHANGED.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BleService.EXTRA_DATA);
                if (data != null) {
                    LogUtils.i("BleService", "收到通知:");
                }
                for (int i = 0; i < data.length; i++) {
                    LogUtils.i("BleService", " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
                }
                mPresenter.doCharacteristicOnePackageData(data);
                LogUtils.i("BleService", "--------onCharacteristicChanged-----");
            }
        }
    };



    @Override
    public void updateBindDevicesView() {
        mPresenter.setIsSendRequest(true);
        jumpMyBraceletActivity();
    }

    private void jumpMyBraceletActivity() {
        Intent intent = new Intent(this, MyBraceletActivity.class);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, mPresenter.getBindDevicesName());
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, mPresenter.getBindDevicesAddress());
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_FIRMWARE_INFO, mPresenter.getFirmwareInfo());
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_POWER, mPresenter.getBraceletPower());
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "BingBraceletActivity");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pauseBle();
        unregisterReceiver(mGattUpdateReceiver);
    }
}
