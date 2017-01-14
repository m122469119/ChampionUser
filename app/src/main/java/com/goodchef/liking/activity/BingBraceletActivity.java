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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.DeviceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BlueToothAdapter;
import com.goodchef.liking.bluetooth.DealWithBlueTooth;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.mvp.presenter.BindDevicesPresenter;
import com.goodchef.liking.mvp.view.BindDevicesView;
import com.goodchef.liking.bluetooth.BlueDataUtil;
import com.goodchef.liking.widgets.RoundImageView;
import com.goodchef.liking.widgets.WhewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @BindView(R.id.blue_booth_RecyclerView)
    RecyclerView mBlueBoothRecyclerView;
    @BindView(R.id.open_blue_tooth_TextView)
    TextView mOpenBlueToothTextView;
    @BindView(R.id.layout_blue_open_state)
    RelativeLayout mLayoutBlueOpenState;


    private BindDevicesPresenter mBindDevicesPresenter;
    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBraceletPower;//电量
    private boolean isSendRequest = false;//是否发送过请求
    private BlueToothAdapter mAdapter;//蓝牙适配器

    private String myBraceletMac;//我的手环地址
    private String muuId;//UUID
    private DealWithBlueTooth mDealWithBlueTooth;//手环处理类
    private Handler mHandler = new Handler();
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private Map<String, BluetoothDevice> map = new HashMap<>();
    private int mConnectionState = DealWithBlueTooth.STATE_DISCONNECTED;
    private BluetoothGattCharacteristic writecharacteristic;
    private BluetoothGattCharacteristic readcharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
        showPromptDialog();

        setRightIcon(R.drawable.icon_blue_tooth_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BlueToothHelpActivity.class);
            }
        });
        setViewOnClickListener();
        mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);
        mBlueBoothRecyclerView.setVisibility(View.GONE);
        initBlueToothRecycleView();
        mDealWithBlueTooth = new DealWithBlueTooth(this);
        initBlueTooth();
    }


    private void initBlueToothRecycleView() {
        mBlueBoothRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mAdapter == null) {
            mAdapter = new BlueToothAdapter(this);
        }
        mBlueBoothRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListenr(mConnectListener);
    }


    private void setViewOnClickListener() {
        mBlueToothRoundImageView.setOnClickListener(this);
        mOpenBlueToothTextView.setOnClickListener(this);
    }

    /**
     * 初始化蓝牙
     */
    public void initBlueTooth() {
        if (!mDealWithBlueTooth.isSupportBlueTooth()) {
            return;
        }
        if (!mDealWithBlueTooth.isOpen()) {
            mLayoutBlueOpenState.setVisibility(View.VISIBLE);
            openBluetooth();
        } else {
            mLayoutBlueOpenState.setVisibility(View.GONE);
        }
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        mDealWithBlueTooth.openBlueTooth(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDealWithBlueTooth.isOpen()) {
                    mLayoutBlueOpenState.setVisibility(View.GONE);
                } else {
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }


    /**
     * 连接蓝牙
     */
    private View.OnClickListener mConnectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BluetoothDevice device = (BluetoothDevice) v.getTag();
            if (device != null && !StringUtils.isEmpty(device.getAddress())) {
                mBindDevicesName = device.getName();
                mBindDevicesAddress = device.getAddress();
                mDealWithBlueTooth.connect(device.getAddress(), mGattCallback);
                mConnectionState = DealWithBlueTooth.STATE_CONNECTING;
            }
        }
    };

    /**
     * 展示蓝牙提示
     */
    private void showPromptDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_one_content, null, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        builder.setCustomView(view);
        titleTextView.setText(R.string.notice_prompt);
        contentTextView.setText(R.string.send_brancelet_bing_prompt);
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
        mDealWithBlueTooth.removeHandleMessage();
    }

    @Override
    public void onClick(View v) {
        if (v == mBlueToothRoundImageView) {
            mBlueBoothRecyclerView.setVisibility(View.VISIBLE);
            mLayoutBlueToothBracelet.setVisibility(View.GONE);
            mLayoutBlueOpenState.setVisibility(View.GONE);
            searchBlueTooth();
        } else if (v == mOpenBlueToothTextView) {
            openBluetooth();
        }
    }


    /**
     * 搜索蓝牙
     */
    private void searchBlueTooth() {
        if (mBlueToothWhewView.isStarting()) {
            mClickSearchTextView.setVisibility(View.VISIBLE);
            //如果动画正在运行就停止，否则就继续执行
            mBlueToothWhewView.stop();
            //结束进程
            mDealWithBlueTooth.removeHandleMessage();
            scanLeDevice(false);
        } else {
            mBluetoothDeviceList.clear();//清空装载蓝牙的集合
            // 执行动画
            mBlueToothWhewView.start();
            mClickSearchTextView.setVisibility(View.GONE);
            mDealWithBlueTooth.sendHandleMessage();
            mAdapter.setData(mBluetoothDeviceList);
            scanLeDevice(true);
        }
    }

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDealWithBlueTooth.stopLeScan(mLeScanCallback);
                    mBlueToothWhewView.stop();
                    mClickSearchTextView.setVisibility(View.VISIBLE);
                    // setBlueToothDevicesList();
                }
            }, 10000); //10秒后停止搜索
            UUID[] uuids = new UUID[1];
            UUID uuid = UUID.fromString("0000FEA0-0000-1000-8000-00805f9b34fb");
            uuids[0] = uuid;
            mDealWithBlueTooth.startLeScan(mLeScanCallback);
        } else {
            mDealWithBlueTooth.stopLeScan(mLeScanCallback);
        }
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
                            if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                                LogUtils.i(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
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
        mAdapter.setData(mBluetoothDeviceList);
        mAdapter.notifyDataSetChanged();
    }

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
        }

        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue() != null) {
                System.out.println("收到通知:");
            }
            Log.i(TAG, "Characteristic.getUuid == " + characteristic.getUuid().toString());
            byte[] data = characteristic.getValue();
            for (int i = 0; i < data.length; i++) {
                LogUtils.i(TAG, " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
            }
            doCharacteristicOnePackageData(data);
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

    /**
     * 处理单包蓝牙数据，在这个界面值涉及到单包的数据
     *
     * @param data
     */
    private void doCharacteristicOnePackageData(byte[] data) {
        if (data.length >= 3) {
            if ((data[1] & 0xff) == 0x33) {//绑定
                if (data[4] == 0x00) {
                    LogUtils.i(TAG, "绑定成功");
                    sendLogin();
                } else if (data[4] == 0x01) {
                    LogUtils.i(TAG, "绑定失败");
                }
            } else if ((data[1] & 0xff) == 0x35) {
                if (data[4] == 0x00) {
                    LogUtils.i(TAG, "登录成功");
                } else if (data[4] == 0x01) {
                    LogUtils.i(TAG, "登录失败");
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
                if (!isSendRequest) {
                    isSendRequest = true;
                    sendDevicesRequest();
                }
            } else if ((data[1] & 0xff) == 0x27) {
                LogUtils.i(TAG, "心率 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                LogUtils.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                getFirmwareInfo(data);
            }

        }
    }

    /**
     * 发送登录
     */
    private void sendLogin() {
        if (writecharacteristic != null) {
            byte[] uuId = muuId.getBytes();
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getLoginBytes(uuId));
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
            mFirmwareInfo = BlueDataUtil.getUTF8XMLString(str);
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
        String osName = android.os.Build.MODEL;
        String osVersion = android.os.Build.VERSION.RELEASE;
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
        mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getDisconnectBlueTooth());
    }
}
