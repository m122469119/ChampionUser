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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DeviceUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleManager;
import com.goodchef.liking.bluetooth.BleService;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.mvp.presenter.BindDevicesPresenter;
import com.goodchef.liking.mvp.view.BindDevicesView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.RoundImageView;
import com.goodchef.liking.widgets.WhewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:绑定手环
 * Author : shaozucheng
 * Time: 下午3:28
 * version 1.0.0
 */

public class BingBraceletActivity extends AppBarActivity implements View.OnClickListener, BindDevicesView {

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


    private BindDevicesPresenter mBindDevicesPresenter;
    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBraceletPower;//电量
    private boolean isSendRequest = false;//是否发送过请求
    private BluetoothDevice mBluetoothDevice;

    private String myBraceletMac;//我的手环地址
    private String muuId;//UUID
    private Handler mHandler = new Handler();
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private Map<String, BluetoothDevice> map = new HashMap<>();
    private boolean mConnectionState = false;
    private BluetoothGattCharacteristic writecharacteristic;
    private BluetoothGattCharacteristic readcharacteristic;
    private boolean isLoginSuccess = false;
    private boolean connectFail = false;//是否连接失败
    private int clickSearch;
    private boolean isSearchSuccess = false;
    private BleManager mBleManager;
    private int connectState;

    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            LogUtils.i(TAG, "needName = " + myBraceletMac + "searchName = " + device.getName() + " mac = " + device.getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //在这里可以把搜索到的设备保存起来
                    if (Math.abs(rssi) <= 150) {//过滤掉信号强度小于-150的设备
                        if (!TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress())) {
                            if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                                LogUtils.i(TAG, "找到匹配的手环设备: " + myBraceletMac);
                                map.clear();
                                map.put(device.getAddress(), device);
                                setBlueToothDevicesList();
                            }
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));
        mBleManager = new BleManager(this, mLeScanCallback);
        mBleManager.bind();
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
        showPromptDialog();
        clickSearch = 0;
        setRightIcon(R.drawable.icon_blue_tooth_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectState == 1) {
                    PopupUtils.showToast(getString(R.string.connect_not_jump_help));
                    return;
                }
                startActivity(BlueToothHelpActivity.class);
            }
        });
        setViewOnClickListener();
        if (!mBleManager.isOpen()) {
            noOpenBlueToothView();
        }
        mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
        mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
        mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
    }


    private void setViewOnClickListener() {
        mBlueToothRoundImageView.setOnClickListener(this);
        mOpenBlueToothTextView.setOnClickListener(this);
        mConnectBlueToothTextView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        openBluetooth();
        mLayoutBlueOpenState.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setText(R.string.bluetooth_no_open);
        mOpenBlueToothTextView.setVisibility(View.VISIBLE);
        mOpenBlueToothTextView.setText(R.string.open_bluetooth);
        mOpenBlueToothTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        if (mBleManager != null) {
            mBleManager.getBleUtils().openBlueTooth(this);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBleManager != null && mBleManager.getBleUtils() != null && mBleManager.getBleUtils().isOpen()) {
                    mLayoutBlueOpenState.setVisibility(View.GONE);
                } else {
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);
                }
            }
        }, 4000);
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
        contentTextView.setText(getString(R.string.send_brancelet_bing_prompt_left) + "\n" + getString(R.string.send_brancelet_bing_prompt_middle) + "\n" + getString(R.string.send_brancelet_bing_prompt_right));
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
        if (mBleManager != null) {
            mBleManager.release();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mBlueToothRoundImageView) {
            startSearchBlueTooth();
        } else if (v == mOpenBlueToothTextView) {
            String open = mOpenBlueToothTextView.getText().toString();
            if (open.equals(getString(R.string.open_bluetooth)) || !mBleManager.isOpen()) {
                openBluetooth();
            } else {
                startSearchBlueTooth();
            }
        } else if (v == mConnectBlueToothTextView) {//连接蓝牙
            if (!mBleManager.isOpen()) {
                mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
                mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
                mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
                noOpenBlueToothView();
                return;
            }
            if (mConnectionState) {
                sendLogin();
            } else {
                mConnectBlueToothTextView.setText(getString(R.string.connect_bluetooth_ing));
                mConnectBluetoothProgressBar.setVisibility(View.VISIBLE);
                connectBlueTooth();
                connectState = 1;
                mConnectBlueToothTextView.setEnabled(false);
            }
        }
    }

    /**
     * 开启搜索蓝牙
     */
    private void startSearchBlueTooth() {
        if (!mBleManager.isOpen()) {
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
            mBluetoothDeviceList.clear();//清空装载蓝牙的集合
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
        mBleManager.doScan(enable);
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

    private void setBlueToothDevicesList() {
        mBluetoothDeviceList.clear();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            BluetoothDevice value = (BluetoothDevice) entry.getValue();
            System.out.println("Key = " + key + ", name = " + value.getName() + "address = " + value.getAddress());
            mBluetoothDeviceList.add(value);
        }
        if (!isSearchSuccess) {
            setSearchBlueToothDevices();
        }
    }

    private void setSearchBlueToothDevices() {
        if (mBluetoothDeviceList != null && mBluetoothDeviceList.size() > 0) {//搜索到设备了
            mBluetoothDevice = mBluetoothDeviceList.get(0);//获取第一个设备
            if (mBluetoothDevice != null) {
                isSearchSuccess = true;
            }
            if (!StringUtils.isEmpty(mBluetoothDevice.getName())) {
                Preference.setBlueToothName(mBluetoothDevice.getAddress(), mBluetoothDevice.getName());
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopBlueToothWhewView();
                }
            }, 1500);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);//展示会员的设备
                    mLayoutBlueToothBracelet.setVisibility(View.GONE);//隐藏搜索提示
                    mLayoutBlueBooth.setVisibility(View.VISIBLE);

                    mClickSearchTextView.setText(R.string.click_search);//显示点击搜索
                    mBluetoothStateTextView.setText(R.string.member_bluetooth_devices);

                    mOpenBlueToothTextView.setVisibility(View.GONE);
                    mBlueToothNameTextView.setText(mBluetoothDevice.getName());//展示蓝牙名称

                    mConnectBluetoothProgressBar.setVisibility(View.GONE);//连接的动画关闭
                    mConnectBlueToothTextView.setText(R.string.connect_blue_tooth);//展示连接文案
                    mConnectBlueToothTextView.setEnabled(true);
                }
            });
        }
    }

    private void stopBlueToothWhewView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //1.5s后停止动画
                mBlueToothWhewView.stop();
            }
        });
    }

    /**
     * 连接蓝牙
     */
    private void connectBlueTooth() {
        if (mBlueToothWhewView.isStarting()) {
            mBlueToothWhewView.stop();
        }
        if (mBluetoothDevice != null && !StringUtils.isEmpty(mBluetoothDevice.getAddress())) {
            mBindDevicesName = mBluetoothDevice.getName();
            mBindDevicesAddress = mBluetoothDevice.getAddress();
            mBleManager.connect(mBluetoothDevice.getAddress());
        }
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!mBleManager.isOpen()) {//连接之前先判断蓝牙的状态
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
                    connectBlueTooth();
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
                mConnectionState = true;
                connectState = 2;
                mBleManager.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnectionState = false;
                connectState = 0;
                LogUtils.i("BleService", "连接失败");
                sendConnect();
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //在这里可以对服务进行解析，寻找到你需要的服务
                getBlueToothServices();
            } else if (BleService.ACTION_CHARACTERISTIC_CHANGED.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BleService.EXTRA_DATA);
                if (data != null) {
                    LogUtils.i("BleService", "收到通知:");
                }
                for (int i = 0; i < data.length; i++) {
                    LogUtils.i("BleService", " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
                }
                doCharacteristicOnePackageData(data);
                LogUtils.i("BleService", "--------onCharacteristicChanged-----");
            }
        }
    };


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
                mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getBindBytes(uuId));
                if (readcharacteristic != null) {
                    mBleManager.setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }

    /**
     * 处理单包蓝牙数据，在这个界面值涉及到单包的数据
     *
     * @param data
     */
    private void doCharacteristicOnePackageData(byte[] data) {
        if (data.length >= 3) {
            if ((data[1] & 0xff) == 0x33) {//绑定
                if (data[4] == 0x00) {
                    LogUtils.i("BleService", "绑定成功");
                    sendLogin();
                    setLoginTimeOut();
                } else if (data[4] == 0x01) {
                    LogUtils.i("BleService", "绑定失败");
                }
            } else if ((data[1] & 0xff) == 0x35) {
                if (data[4] == 0x00) {
                    LogUtils.i("BleService", "登录成功");
                    isLoginSuccess = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mOpenBlueToothTextView.setVisibility(View.GONE);
                            mConnectBluetoothProgressBar.setVisibility(View.GONE);
                            mConnectBlueToothTextView.setText(R.string.connect_bluetooth_success);
                        }
                    });
                    setBlueToothTime();
                    sendBindDeviceRequest();
                } else if (data[4] == 0x01) {
                    LogUtils.i("BleService", "登录失败");
                }
            } else if ((data[1] & 0xff) == 0x0D) {
                if (data[4] == 0x00) {
                    LogUtils.i("BleService", "解绑成功");
                } else if (data[4] == 0x01) {
                    LogUtils.i("BleService", "解绑失败");
                }
            } else if ((data[1] & 0xff) == 0x09) {//电量
                LogUtils.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
                mBraceletPower = (data[4] & 0xff);
            } else if ((data[1] & 0xff) == 0x27) {
                LogUtils.i(TAG, "心率 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                LogUtils.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                getFirmwareInfo(data);
            }

        }
    }

    private void setLoginTimeOut() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOpenBlueToothTextView.setVisibility(View.GONE);
                        mConnectBluetoothProgressBar.setVisibility(View.GONE);
                        mConnectBlueToothTextView.setText(R.string.loging_out_fail);
                        mConnectBlueToothTextView.setEnabled(true);
                    }
                });
            }
        }, 10000);
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
     * 发送绑定请求到后端
     */
    private void sendBindDeviceRequest() {
        if (isLoginSuccess && !isSendRequest) {
            isSendRequest = true;
            sendDevicesRequest();
        }
    }

    /**
     * 发送登录
     */
    private void sendLogin() {
        if (writecharacteristic != null) {
            byte[] uuId = muuId.getBytes();
            LogUtils.i("BleService", "sendLogin: " + muuId);
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getLoginBytes(uuId));
        }
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
        }
    }

    /**
     * 发送绑定手环信息
     */
    private void sendDevicesRequest() {
        if (mBindDevicesPresenter == null) {
            mBindDevicesPresenter = new BindDevicesPresenter(this, this);
        }
        String osName = Build.MODEL;
        String osVersion = Build.VERSION.RELEASE;
        mBindDevicesPresenter.bindDevices(mBindDevicesName, mFirmwareInfo, SecurityUtils.MD5.get16MD5String(DeviceUtils.getDeviceInfo(BaseApplication.getInstance())), "android", osName, osVersion);
    }

    @Override
    public void updateBindDevicesView() {
        isSendRequest = true;
        jumpMyBraceletActivity();
    }

    private void jumpMyBraceletActivity() {
        Intent intent = new Intent(this, MyBraceletActivity.class);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, mBindDevicesName);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, mBindDevicesAddress);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_FIRMWARE_INFO, mFirmwareInfo);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_POWER, mBraceletPower);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "BingBraceletActivity");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BleUtils.isSupportBleDevice(this) && mBleManager.isOpen() && mConnectionState && writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getDisconnectBlueTooth());
        }
        unregisterReceiver(mGattUpdateReceiver);
    }
}
