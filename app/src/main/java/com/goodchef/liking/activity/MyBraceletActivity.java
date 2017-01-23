package com.goodchef.liking.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleManager;
import com.goodchef.liking.bluetooth.BleService;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.dialog.UnBindDevicesDialog;
import com.goodchef.liking.eventmessages.ServiceConnectionMessage;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.mvp.presenter.UnBindDevicesPresenter;
import com.goodchef.liking.mvp.view.UnBindDevicesView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.MyCustomCircleView;

import java.util.List;

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


    private String mBindDevicesName = "";//绑定的设备名称
    private String mBindDevicesAddress = "";//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBraceletPower = 0;
    private UnBindDevicesPresenter mUnBindDevicesPresenter;
    private String myBraceletMac;//我的手环mac地址
    private String muuId;
    private String source;

    private Handler mHandler = new Handler();
    private BluetoothGattCharacteristic writecharacteristic;
    private BluetoothGattCharacteristic readcharacteristic;
    private boolean mConnectionState = false;
    private boolean isSendRequest = false;//是否发送过请求
    private boolean isLoginFail = false;//是否连接失败
    private boolean isConnect = false;//是否连接
    private boolean connectFail = false;//是否连接失败
    private boolean isPause = false;
    private boolean isScanDevices = false;
    private boolean isGetAllData = false;
    private BluetoothDevice mBluetoothDevice;
    private BleManager mBleManager;
    private int connectState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_my_bracelet));
        mBleManager = new BleManager(this, mLeScanCallback);
        mBleManager.bind();
        getInitData();
        initBlueTooth();
        if (source.equals("BingBraceletActivity")) {
            mMyBraceletTextView.setText(R.string.binding_finish);
            synchronizationInfo();
            initData();
            mUnbindTextView.setVisibility(View.VISIBLE);
        } else {
            isScanDevices = false;
            searchBlueTooth();
            mUnbindTextView.setVisibility(View.GONE);
            mMyBraceletImageView.setBackgroundResource(R.drawable.icon_my_blue_tooth);
        }
        setViewOnClickListener();
    }

    private void getInitData() {
        mBindDevicesName = getIntent().getStringExtra(KEY_BRACELET_NAME);
        mBindDevicesAddress = getIntent().getStringExtra(KEY_BRACELET_ADDRESS);
        mFirmwareInfo = getIntent().getStringExtra(KEY_BRACELET_FIRMWARE_INFO);
        mBraceletPower = getIntent().getIntExtra(KEY_BRACELET_POWER, 0);
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
        source = getIntent().getStringExtra(KEY_BRACELET_SOURCE);
    }

    private void initData() {
        if (!StringUtils.isEmpty(mBindDevicesName)) {
            mCurrentDevicesNameTextView.setText(mBindDevicesName);
        }
        if (!StringUtils.isEmpty(mBindDevicesAddress)) {
            mDevicesAddressTextView.setText(mBindDevicesAddress);
        }
        if (!StringUtils.isEmpty(mFirmwareInfo)) {
            mDevicesVersionTextView.setText(mFirmwareInfo);
        }
    }

    private void setViewOnClickListener() {
        mUnbindTextView.setOnClickListener(this);
        mBluetoothScanFailRetryTextView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(ServiceConnectionMessage message) {
        if (!source.equals("BingBraceletActivity") && initBlueTooth() && !StringUtils.isEmpty(myBraceletMac)) {
            connect();
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

    private void setSynchronizationPowerView(int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMyBraceletImageView.setVisibility(View.GONE);
                mMyBraceletTextView.setVisibility(View.GONE);
                mBraceletPowerMyCustomCircleView.setVisibility(View.VISIBLE);
                mMyPowerTextView.setVisibility(View.VISIBLE);
                mBraceletPowerMyCustomCircleView.setCurrentCount(100, mBraceletPower);
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
     * 初始化蓝牙
     */
    public boolean initBlueTooth() {
        if (!mBleManager.isOpen()) {
            openBluetooth();
            return false;
        }
        return true;
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        if (mBleManager.getBleUtils() != null) {
            mBleManager.getBleUtils().openBlueTooth(this);
        }
    }

    /**
     * 搜索蓝牙
     */
    private void searchBlueTooth() {
        if (!initBlueTooth()) {
            return;
        }
        scanLeDevice(true);
    }


    public void scanLeDevice(final boolean enable) {
        mBleManager.doScan(enable);
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //在这里可以把搜索到的设备保存起来
                    if (Math.abs(rssi) <= 90) {//过滤掉信号强度小于-90的设备
                        if (!TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress())) {
                            LogUtils.d(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
                            if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                                mBindDevicesAddress = device.getAddress();
                                mBindDevicesName = device.getName();
                                myBraceletMac = device.getAddress();
                                if (!isScanDevices) {
                                    isScanDevices = true;
                                    connect();
                                }
                            }
                        }
                    }
                }
            });
        }
    };

    /**
     * 连接蓝牙
     */
    private void connect() {
        if (!initBlueTooth()) {
            beforeConnectBlueToothView(getString(R.string.bluetooth_connect_fail));
            return;
        }
        if (source.equals("BingBraceletActivity")) {
            return;
        }
        if (!isConnect) {
            isConnect = true;
            mBleManager.stopScan();
            mLayoutBluetoothConnectFail.setVisibility(View.GONE);
            setConnectView(getString(R.string.connect_bluetooth_ing));
            mBleManager.connect(myBraceletMac);
            connectState = 1;
            connectTimeOutView();
        }
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!initBlueTooth()) {//连接之前先判断蓝牙的状态
            beforeConnectBlueToothView(getString(R.string.bluetooth_connect_fail));
            return;
        }
        if (source.equals("BingBraceletActivity")) {
            return;
        }
        if (!connectFail) {
            mBleManager.stopScan();
            mLayoutBluetoothConnectFail.setVisibility(View.GONE);
            connectFail = true;
            mBleManager.connect(myBraceletMac);
            connectState = 1;
            connectTimeOutView();
            setConnectView(getString(R.string.connect_bluetooth_ing));
        } else {
            beforeConnectBlueToothView(getString(R.string.bluetooth_connect_fail));
        }
    }

    private void connectTimeOutView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (connectState == 1) {
                    beforeConnectBlueToothView(getString(R.string.connect_time_out));
                }
            }
        }, 20000);
    }

    /**
     * 在连接之前判断是否蓝牙断开，此时view的展示
     */
    private void beforeConnectBlueToothView(final String string) {
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
                mBluetoothScanFailRetryTextView.setOnClickListener(MyBraceletActivity.this);
                mUnbindTextView.setVisibility(View.GONE);
            }
        });
        setConnectView(getString(R.string.connect_fial));
    }

    /**
     * 设置连接是的view
     */
    private void setConnectView(final String connectStr) {
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


    /**
     * 处理蓝牙返回的数据
     */
    private void doCharacteristicData(byte[] data) {
        if (data != null && data.length >= 3) {
            if ((data[1] & 0xff) == 0x33) {//绑定
                if (data[4] == 0x00) {
                    LogUtils.i(TAG, "绑定成功");
                } else if (data[4] == 0x01) {
                    LogUtils.i(TAG, "绑定失败");
                }
            } else if ((data[1] & 0xff) == 0x35) {
                if (data[4] == 0x00) {
                    LogUtils.i(TAG, "登录成功");
                    setLoginTimeOut();
                    setBlueToothTime();
                } else if (data[4] == 0x01) {
                    LogUtils.i(TAG, "登录失败");
                    if (!isLoginFail) {
                        isLoginFail = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLoginFail();
                            }
                        });
                    }
                }
            } else if ((data[1] & 0xff) == 0x0D) {
                if (data[4] == 0x00) {
                    LogUtils.i(TAG, "解绑成功");
                } else if (data[4] == 0x01) {
                    LogUtils.i(TAG, "解绑失败");
                }
            } else if ((data[1] & 0xff) == 0x09) {//电量
                LogUtils.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
                mBraceletPower = (data[4] & 0xff);
                setPowerView();
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                LogUtils.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                getFirmwareInfo(data);
            }
        }
    }

    /**
     * 设置蓝牙时间
     */
    private void setBlueToothTime() {
        if (writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getTimeBytes());
        }
    }

    /**
     * 发送登录
     */
    private void sendLogin() {
        if (writecharacteristic != null) {
            byte[] uuId = muuId.getBytes();
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getLoginBytes(uuId));
        }
    }

    private void setLoginTimeOut() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isGetAllData) {
                            showLoginFail();
                        }
                    }
                });
            }
        }, 10000);
    }

    /**
     * 连接成功
     */
    private void setConnectSuccessView() {
        if (mConnectionState) {//连接成功，关闭扫描
            mBleManager.stopScan();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!StringUtils.isEmpty(mBluetoothDevice.getName())) {
                    Preference.setBlueToothName(mBluetoothDevice.getAddress(), mBluetoothDevice.getName());
                }
                mMyBraceletTextView.setText(R.string.connect_success);
            }
        });
        connectSuccessTimeOut();
    }

    private void connectSuccessTimeOut() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isGetAllData) {
                    sendLogin();
                }
            }
        }, 20000);
    }

    /**
     * 设置电量view
     */
    private void setPowerView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isGetAllData = true;
                mUnbindTextView.setVisibility(View.VISIBLE);
                setSynchronizationPowerView(500);
                if (Preference.getFirstBindBracelet()) {
                    showFirstCheckPromptDialog();
                }
            }
        });
    }


    private void getBlueToothServices() {
        List<BluetoothGattService> bluetoothGattServices = mBleManager.getSupportedGattServices();
        BluetoothGattService bleService = null;
        for (BluetoothGattService service : bluetoothGattServices) {
            if (service.getUuid().equals(BlueCommandUtil.Constants.SERVER_UUID)) {
                bleService = service;
                break;
            }
        }
        if (bleService != null) {
            writecharacteristic = bleService.getCharacteristic(BlueCommandUtil.Constants.TX_UUID);
            readcharacteristic = bleService.getCharacteristic(BlueCommandUtil.Constants.RX_UUID);
            if (writecharacteristic != null) {
                byte[] uuId = muuId.getBytes();
                mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getLoginBytes(uuId));
                if (readcharacteristic != null) {
                    mBleManager.setCharacteristicNotification(writecharacteristic, true);
                    mBleManager.setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }

    private void showLoginFail() {
        if (isFinishing()) {
            return;
        }
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.bluetooth_login_fail));
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendLogin();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 第一次绑定成功后提示用户在我的界面查看蓝牙数据
     */
    private void showFirstCheckPromptDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_check_blue_tooth_data) + "\n" + getString(R.string.prompt_check_blue_tooth_data_send));
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        Preference.setFirstBindBracelet(false);
    }


    /**
     * 获取固件信息
     *
     * @param data
     */
    private void getFirmwareInfo(byte[] data) {
        if (data.length > 10) {
            byte[] bytes = new byte[6];
            for (int i = 4; i < 10; i++) {
                bytes[i - 4] = data[i];
            }
            String str = new String(bytes);
            mFirmwareInfo = BlueCommandUtil.getUTF8XMLString(str);
            LogUtils.i(TAG, "固件信息= " + mFirmwareInfo);
            final String BlueToothName = mBluetoothDevice.getName();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!StringUtils.isEmpty(mFirmwareInfo)) {
                        mLayoutBluetoothConnectFail.setVisibility(View.GONE);
                        mDevicesVersionTextView.setText(mFirmwareInfo);
                        if (StringUtils.isEmpty(BlueToothName)) {
                            mCurrentDevicesNameTextView.setText(Preference.getBlueToothName(mBluetoothDevice.getAddress()));
                        } else {
                            mCurrentDevicesNameTextView.setText(BlueToothName);
                        }
                        mDevicesAddressTextView.setText(mBluetoothDevice.getAddress());
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unbind_TextView:
                showUnbindDialog();
                break;
            case R.id.bluetooth_scan_fail_retry_TextView://重试 -- 连接蓝牙
                if (!initBlueTooth()) {
                    return;
                }
                if (!StringUtils.isEmpty(myBraceletMac)) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLayoutBluetoothConnectFail.setVisibility(View.GONE);
                            isConnect = false;
                            connect();
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
        devicesDialog.setCancelClickListener(new UnBindDevicesDialog.CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });

        devicesDialog.setConfirmClickListener(new UnBindDevicesDialog.ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
//                if (EnvironmentUtils.Config.isDebugMode()) {
//                    mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getUnBindBytes());
//                }
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
        if (mBleManager.isOpen() && mConnectionState && writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getDisconnectBlueTooth());
        }
        Preference.setIsBind("0");
        finish();
    }

    @Override
    protected void onPause() {
        isPause = true;
        isConnect = false;
        if (mBleManager.isOpen() && mConnectionState && writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getDisconnectBlueTooth());
        }
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDealWithBlueTooth.removeHandleMessage();
        mBleManager.release();
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "连接成功");
                mConnectionState = true;
                connectState = 2;
                mBluetoothDevice = mBleManager.getBluetoothGatt().getDevice();
                setConnectSuccessView();
                mBleManager.discoverServices();

            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnectionState = false;
                isScanDevices = false;
                connectState = 0;
                LogUtils.i(TAG, "连接失败");
                sendConnect();
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //在这里可以对服务进行解析，寻找到你需要的服务
                getBlueToothServices();
            } else if (BleService.ACTION_CHARACTERISTIC_CHANGED.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BleService.EXTRA_DATA);
                if (data != null) {
                    System.out.println("收到通知:");
                }
                for (int i = 0; i < data.length; i++) {
                    LogUtils.i(TAG, " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
                }
                if (!isGetAllData) {
                    doCharacteristicData(data);
                }
                System.out.println("--------onCharacteristicChanged-----");
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


}
