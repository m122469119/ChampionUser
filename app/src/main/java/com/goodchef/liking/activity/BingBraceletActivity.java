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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BlueToothAdapter;
import com.goodchef.liking.mvp.presenter.BindDevicesPresenter;
import com.goodchef.liking.mvp.view.BindDevicesView;
import com.goodchef.liking.utils.BlueDataUtil;
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

    public static final UUID SERVER_UUID = UUID.fromString("C3E6FEA0-E966-1000-8000-BE99C223DF6A");
    public static final UUID TX_UUID = UUID.fromString("C3E6FEA1-E966-1000-8000-BE99C223DF6A");
    public static final UUID RX_UUID = UUID.fromString("C3E6FEA2-E966-1000-8000-BE99C223DF6A");


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

    private static final int Nou = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
            }
        }
    };


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BlueToothAdapter mAdapter;
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    Map<String, BluetoothDevice> map = new HashMap<>();

    private boolean mScanning;

    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;
    private static final int STATE_DISCONNECTED = 0; //设备无法连接
    private static final int STATE_CONNECTING = 1;  //设备正在连接状态
    private static final int STATE_CONNECTED = 2;   //设备连接完毕
    private int mConnectionState = STATE_DISCONNECTED;

    BluetoothGattCharacteristic writecharacteristic;
    BluetoothGattCharacteristic readcharacteristic;

    private BindDevicesPresenter mBindDevicesPresenter;
    private String mBindDevicesName;//绑定的设备名称
    private String mBindDevicesAddress;//绑定的设备地址
    private String mFirmwareInfo;//固件版本信息
    private int mBraceletPower;//电量
    private boolean isSendRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));
        showPromptDialog();
        initBlueTooth();
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

    }

    private boolean isSupportBlueTooth() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            PopupUtils.showToast("设备不支持蓝牙");
            return false;
        } else {
            return true;
        }
    }

    private void initBlueTooth() {
        if (!isSupportBlueTooth()) {
            return;
        }
        if (!isOpen()) {
            mLayoutBlueOpenState.setVisibility(View.VISIBLE);
            openBluetooth();
        } else {
            mLayoutBlueOpenState.setVisibility(View.GONE);
        }
    }


    private void initBlueToothRecycleView() {
        mBlueBoothRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mAdapter == null) {
            mAdapter = new BlueToothAdapter(this);
        }
        mBlueBoothRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListenr(mConnectListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setViewOnClickListener() {
        mBlueToothRoundImageView.setOnClickListener(this);
        mOpenBlueToothTextView.setOnClickListener(this);
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
                connect(device.getAddress());
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
        titleTextView.setText("提示");
        contentTextView.setText("只有二代手环才能绑定哟！");
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
        // TODO Auto-generated method stub
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(Nou);
        }
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
     * 打开蓝牙
     */
    public void openBluetooth() {
        if (!isSupportBlueTooth()) {
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpen()) {
                    mLayoutBlueOpenState.setVisibility(View.GONE);
                } else {
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }


    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        mBluetoothAdapter.disable();
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return mBluetoothAdapter.isEnabled();
    }


    private void searchBlueTooth() {
        if (mBlueToothWhewView.isStarting()) {
            mClickSearchTextView.setVisibility(View.VISIBLE);
            //如果动画正在运行就停止，否则就继续执行
            mBlueToothWhewView.stop();
            //结束进程
            handler.removeMessages(Nou);
            scanLeDevice(false);
        } else {
            mBluetoothDeviceList.clear();//清空装载蓝牙的集合
            // 执行动画
            mBlueToothWhewView.start();
            mClickSearchTextView.setVisibility(View.GONE);
            handler.sendEmptyMessage(Nou);
            mAdapter.setData(mBluetoothDeviceList);
            scanLeDevice(true);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mBlueToothWhewView.stop();
                    mClickSearchTextView.setVisibility(View.VISIBLE);
                    setBlueToothDevicesList();
                }
            }, 10000); //10秒后停止搜索
            mScanning = true;
            UUID[] uuids = new UUID[1];
            UUID uuid = UUID.fromString("0000FEA0-0000-1000-8000-00805f9b34fb");
            uuids[0] = uuid;
            mBluetoothAdapter.startLeScan(mLeScanCallback); //开始搜索
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止搜索
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
                            mBluetoothDeviceAddress = device.getAddress();
                            map.put(device.getAddress(), device);
                            LogUtils.d(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
                            setBlueToothDevicesList();
                        }
                    }
                }
            });
        }
    };

    private void setBlueToothDevicesList() {
        if (!mScanning) {
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                BluetoothDevice value = (BluetoothDevice) entry.getValue();
                System.out.println("Key = " + key + ", name = " + value.getName() + "address = " + value.getAddress());
                mBluetoothDeviceList.add(value);
            }
        }
        mAdapter.setData(mBluetoothDeviceList);
        mAdapter.notifyDataSetChanged();
    }


    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.d(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        // 该函数才是真正的去进行连接 一个Context对象，自动连接（boolean值,表示只要BLE设备可用是否自动连接到它）
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        //  mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                Log.i(TAG, "连接成功");
                mConnectionState = STATE_CONNECTED;
                mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "连接失败");
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                Log.i(TAG, "service size = " + getSupportedGattServices().size() + "");
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
            BluetoothGattService service = mBluetoothGatt.getService(SERVER_UUID);
            if (service != null) {
                BluetoothGattCharacteristic readcharacteristic = service.getCharacteristic(RX_UUID);
//                mBluetoothGatt.setCharacteristicNotification(readcharacteristic, true);
                if (readcharacteristic != null) {
                    setCharacteristicNotification(readcharacteristic, true);
                }
            }
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
                        sendDevicesRequest();
                    }
                } else if ((data[1] & 0xff) == 0x27) {
                    Log.i(TAG, "心率 == " + (data[4] & 0xff));
                } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                    Log.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
                } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                    getFirmwareInfo(data);
                }

            }
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

    private void getBlueToothServices() {
        BluetoothGattService service = mBluetoothGatt.getService(SERVER_UUID);
        if (service != null) {
            writecharacteristic = service.getCharacteristic(TX_UUID);
            readcharacteristic = service.getCharacteristic(RX_UUID);
            if (writecharacteristic != null) {
                writecharacteristic.setValue(BlueDataUtil.getBindBytes());
                mBluetoothGatt.writeCharacteristic(writecharacteristic);
                if (readcharacteristic != null) {
                    setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }


    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();   //此处返回获取到的服务列表
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
        for (BluetoothGattDescriptor dp : descriptors) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(dp);
        }
    }


    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] bytes) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        characteristic.setValue(bytes);
        mBluetoothGatt.writeCharacteristic(characteristic);
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
        startActivity(intent);
        finish();
    }
}
