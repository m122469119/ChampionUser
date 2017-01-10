package com.goodchef.liking.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
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
import com.goodchef.liking.bluetooth.DealWithBlueTooth;
import com.goodchef.liking.dialog.UnBindDevicesDialog;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.mvp.presenter.UnBindDevicesPresenter;
import com.goodchef.liking.mvp.view.UnBindDevicesView;
import com.goodchef.liking.bluetooth.BlueDataUtil;
import com.goodchef.liking.widgets.MyCustomCircleView;

import java.util.UUID;

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

    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBraceletPower = 0;
    private UnBindDevicesPresenter mUnBindDevicesPresenter;
    private String myBraceletMac;//我的手环mac地址
    private String source;

    Handler mHandler = new Handler();
    private DealWithBlueTooth mDealWithBlueTooth;//手环处理类
    private boolean mScanning;
    public BluetoothGattCharacteristic writecharacteristic;
    public BluetoothGattCharacteristic readcharacteristic;
    private int mConnectionState = DealWithBlueTooth.STATE_DISCONNECTED;
    private boolean isSendRequest = false;//是否发送过请求
    private boolean isLoginFail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bracelet);
        ButterKnife.bind(this);
        setTitle("我的手环");
        getInitData();
        mDealWithBlueTooth = new DealWithBlueTooth(this);
        initData();
        initBlueTooth();
        if (source.equals("BingBraceletActivity")) {
            synchronizationInfo();
        } else {
            setOnSynchronizationView();
        }
        setViewOnClickListener();
        searchBlueTooth();
    }

    private void getInitData() {
        mBindDevicesName = getIntent().getStringExtra(KEY_BRACELET_NAME);
        mBindDevicesAddress = getIntent().getStringExtra(KEY_BRACELET_ADDRESS);
        mFirmwareInfo = getIntent().getStringExtra(KEY_BRACELET_FIRMWARE_INFO);
        mBraceletPower = getIntent().getIntExtra(KEY_BRACELET_POWER, 0);
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
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
    }


    private void synchronizationInfo() {
        setOnSynchronizationView();
        setSynchronizationSuccessView(4000);
        setSynchronizationPowerView(5000);
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
                mMyBraceletTextView.setText("同步中...");
            }
        }, 2000);
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
                mMyBraceletTextView.setText("同步完成");
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
    public void initBlueTooth() {
        if (!mDealWithBlueTooth.isSupportBlueTooth()) {
            return;
        }
        if (!mDealWithBlueTooth.isOpen()) {
            openBluetooth();
        }
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        mDealWithBlueTooth.openBlueTooth(this);
    }

    /**
     * 搜索蓝牙
     */
    private void searchBlueTooth() {
        if (mDealWithBlueTooth.isOpen()) {
            scanLeDevice(true);
        } else {
            initBlueTooth();
            scanLeDevice(false);
        }
    }


    public void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mDealWithBlueTooth.stopLeScan(mLeScanCallback);
                    mDealWithBlueTooth.connect(myBraceletMac, mGattCallback);
                }
            }, 10000); //10秒后停止搜索
            mScanning = true;
            UUID[] uuids = new UUID[1];
            UUID uuid = UUID.fromString("0000FEA0-0000-1000-8000-00805f9b34fb");
            uuids[0] = uuid;
            mDealWithBlueTooth.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mDealWithBlueTooth.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            Log.d(TAG, "name1111 = " + device.getName() + " mac = " + device.getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //在这里可以把搜索到的设备保存起来
                    if (Math.abs(rssi) <= 90) {//过滤掉信号强度小于-90的设备
                        if (!TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress())) {
                            LogUtils.d(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
                            if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                                myBraceletMac = device.getAddress();
                                mBindDevicesAddress = device.getAddress();
                                mBindDevicesName = device.getName();
                            }
                        }
                    }
                }
            });
        }
    };

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                Log.i(TAG, "连接成功");
                mConnectionState = DealWithBlueTooth.STATE_CONNECTED;
                mDealWithBlueTooth.mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                Log.i(TAG, "Attempting to start service discovery:" + mDealWithBlueTooth.mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                mConnectionState = DealWithBlueTooth.STATE_DISCONNECTED;
                Log.i(TAG, "连接失败");
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                Log.i(TAG, "service size = " + mDealWithBlueTooth.getSupportedGattServices().size() + "");
                //  displayGattServices(getSupportedGattServices());
                getBlueToothServices();
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
            }
        }

        @Override //当向设备Descriptor中写数据时，会回调该函数
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            System.out.println("onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
//            BluetoothGattService service = mBluetoothGatt.getService(SERVER_UUID);
//            if (service != null) {
//                BluetoothGattCharacteristic readcharacteristic = service.getCharacteristic(RX_UUID);
////                mBluetoothGatt.setCharacteristicNotification(readcharacteristic, true);
//                if (readcharacteristic != null) {
//                    setCharacteristicNotification(readcharacteristic, true);
//                }
//            }
        }

        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue() != null) {
                System.out.println("收到通知:");
            }
            Log.i(TAG, "Characteristic.getUuid == " + characteristic.getUuid().toString());
            byte[] data = characteristic.getValue();
            for (int i = 0; i < data.length; i++) {
                if (i == 1) {
                    Log.i(TAG, "命令：" + data[1]);
                }
                Log.i(TAG, " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
            }
            doCharacteristicData(data);
            System.out.println("--------onCharacteristicChanged-----");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            System.out.println("rssi = " + rssi);
        }

        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("--------write success----- status:" + status);
            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "写入失败");
            } else if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "写入成功666666");
            }
        }
    };

    /**
     * 处理蓝牙返回的数据
     */
    private void doCharacteristicData(byte[] data) {
        if (data != null && data.length >= 3) {
            if ((data[1] & 0xff) == 0x33) {//绑定
                if (data[4] == 0x00) {
                    Log.i(TAG, "绑定成功");
                    writecharacteristic.setValue(BlueDataUtil.getLoginBytes());
                } else if (data[4] == 0x01) {
                    Log.i(TAG, "绑定失败");
                }
            } else if ((data[1] & 0xff) == 0x35) {
                if (data[4] == 0x00) {
                    Log.i(TAG, "登录成功");
                } else if (data[4] == 0x01) {
                    Log.i(TAG, "登录失败");
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
                    Log.i(TAG, "解绑成功");
                } else if (data[4] == 0x01) {
                    Log.i(TAG, "解绑失败");
                }
            } else if ((data[1] & 0xff) == 0x09) {//电量
                Log.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
                mBraceletPower = (data[4] & 0xff);
                if (!isSendRequest) {
                    isSendRequest = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setSynchronizationSuccessView(1000);
                            initData();
                            setSynchronizationPowerView(2000);
                        }
                    });
                }
            } else if ((data[1] & 0xff) == 0x27) {
                Log.i(TAG, "心率 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                Log.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                getFirmwareInfo(data);
            }
        }
    }


    private void getBlueToothServices() {
        BluetoothGattService service = mDealWithBlueTooth.mBluetoothGatt.getService(mDealWithBlueTooth.SERVER_UUID);
        if (service != null) {
            writecharacteristic = service.getCharacteristic(mDealWithBlueTooth.TX_UUID);
            readcharacteristic = service.getCharacteristic(mDealWithBlueTooth.RX_UUID);
            if (writecharacteristic != null) {
                mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getBindBytes());
                if (readcharacteristic != null) {
                    mDealWithBlueTooth.setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }

    private void showLoginFail() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage("蓝牙登入失败，重新发送登入请求？");
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getBlueToothServices();
                dialog.dismiss();
            }
        });
        builder.create().show();
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
            mFirmwareInfo = BlueDataUtil.getUTF8XMLString(str);
            LogUtils.i(TAG, "固件信息= " + mFirmwareInfo);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!StringUtils.isEmpty(mFirmwareInfo)) {
                        mDevicesVersionTextView.setText(mFirmwareInfo);
                    }
                }
            });
        }
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
