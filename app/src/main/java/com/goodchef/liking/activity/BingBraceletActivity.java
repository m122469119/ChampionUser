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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.goodchef.liking.bluetooth.BlueDataUtil;
import com.goodchef.liking.bluetooth.DealWithBlueTooth;
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
    private DealWithBlueTooth mDealWithBlueTooth;//手环处理类
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
        showPromptDialog();
        clickSearch = 0;
        setRightIcon(R.drawable.icon_blue_tooth_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BlueToothHelpActivity.class);
            }
        });
        setViewOnClickListener();
        mDealWithBlueTooth = new DealWithBlueTooth(this);
        if (!initBlueTooth()) {
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
    }

    /**
     * 没有打开蓝牙
     */
    private void noOpenBlueToothView() {
        mLayoutBlueOpenState.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setVisibility(View.VISIBLE);
        mBluetoothStateTextView.setText(R.string.bluetooth_no_open);
        mOpenBlueToothTextView.setVisibility(View.VISIBLE);
        mOpenBlueToothTextView.setText(R.string.open_bluetooth);
        mOpenBlueToothTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
    }

    /**
     * 初始化蓝牙
     */
    public boolean initBlueTooth() {
        if (!mDealWithBlueTooth.isSupportBlueTooth()) {
            return false;
        }
        if (!mDealWithBlueTooth.isOpen()) {
            mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
            openBluetooth();
            return false;
        } else {
            return true;
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
        titleTextView.setText(R.string.notice_prompt);
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
        mDealWithBlueTooth.removeHandleMessage();
    }

    @Override
    public void onClick(View v) {
        if (v == mBlueToothRoundImageView) {
            startSearchBlueTooth();
        } else if (v == mOpenBlueToothTextView) {
            String open = mOpenBlueToothTextView.getText().toString();
            if (open.equals(getString(R.string.open_bluetooth)) || !mDealWithBlueTooth.isOpen()) {
                openBluetooth();
            } else {
                startSearchBlueTooth();
            }

        } else if (v == mConnectBlueToothTextView) {//连接蓝牙
            if (!initBlueTooth()) {
                mLayoutBlueOpenState.setVisibility(View.GONE);//您的设备界面初始化隐藏
                mLayoutBlueToothBracelet.setVisibility(View.VISIBLE);//搜索提示初始化显示
                mLayoutBlueBooth.setVisibility(View.GONE);//搜索到的设备界面初始化隐藏
                noOpenBlueToothView();
                return;
            }
            mConnectBlueToothTextView.setText(R.string.connect_bluetooth_ing);
            mConnectBluetoothProgressBar.setVisibility(View.VISIBLE);
            connectBlueTooth();
        }
    }

    /**
     * 开启搜索蓝牙
     */
    private void startSearchBlueTooth() {
        if (!initBlueTooth()) {
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
            mDealWithBlueTooth.removeHandleMessage();

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

            mDealWithBlueTooth.sendHandleMessage();
            scanLeDevice(true);
            clickSearch++;//记录搜索的次数
        }
    }


    public void scanLeDevice(final boolean enable) {
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
                        stopLeScanView();
                    }
                }
            }, 45000); //45秒后停止搜索
            UUID[] uuids = new UUID[1];
            UUID uuid = UUID.fromString("0000FEA0-0000-1000-8000-00805f9b34fb");
            uuids[0] = uuid;
            mDealWithBlueTooth.startLeScan(uuids, mLeScanCallback);
        } else {
            stopLeScanView();
        }
    }

    private void stopLeScanView() {
        mBlueToothWhewView.stop();
        mClickSearchTextView.setText(R.string.click_search);
        mDealWithBlueTooth.stopLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            LogUtils.i(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //在这里可以把搜索到的设备保存起来
                    if (Math.abs(rssi) <= 150) {//过滤掉信号强度小于-90的设备
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
            mDealWithBlueTooth.connect(this, mBluetoothDevice.getAddress(), mGattCallback);
        }
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!initBlueTooth()) {//连接之前先判断蓝牙的状态
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
                    mLayoutBlueOpenState.setVisibility(View.VISIBLE);//展示会员的设备
                    mLayoutBlueToothBracelet.setVisibility(View.GONE);//隐藏搜索提示
                    mLayoutBlueBooth.setVisibility(View.VISIBLE);

                    mConnectBlueToothTextView.setText(R.string.connect_bluetooth_ing);
                    mConnectBluetoothProgressBar.setVisibility(View.VISIBLE);
                    connectBlueTooth();
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
    }


    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                LogUtils.i(TAG, "连接成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOpenBlueToothTextView.setVisibility(View.GONE);
                        mConnectBluetoothProgressBar.setVisibility(View.GONE);
                        mConnectBlueToothTextView.setText(R.string.connect_bluetooth_success);
                    }
                });
                mConnectionState = true;
                mDealWithBlueTooth.mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                LogUtils.i(TAG, "Attempting to start service discovery:" + mDealWithBlueTooth.mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                mConnectionState = false;
                LogUtils.i(TAG, "连接失败");
                sendConnect();
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                LogUtils.i(TAG, "service size = " + mDealWithBlueTooth.getSupportedGattServices().size() + "");
                //  displayGattServices(getSupportedGattServices());
                getBlueToothServices();
            } else {
                LogUtils.d(TAG, "onServicesDiscovered received: " + status);
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
                LogUtils.i(TAG, "写入失败");
            } else if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.i(TAG, "写入成功666666");
            }
        }
    };


    private void getBlueToothServices() {
        BluetoothGattService service = mDealWithBlueTooth.mBluetoothGatt.getService(mDealWithBlueTooth.SERVER_UUID);
        if (service != null) {
            writecharacteristic = service.getCharacteristic(mDealWithBlueTooth.TX_UUID);
            readcharacteristic = service.getCharacteristic(mDealWithBlueTooth.RX_UUID);
            if (writecharacteristic != null) {
                byte[] uuId = muuId.getBytes();
                mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getBindBytes(uuId));
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
                    isLoginSuccess = true;
                    setBlueToothTime();
                    sendBindDeviceRequest();
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
     * 设置蓝牙时间
     */
    private void setBlueToothTime() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getTimeBytes());
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
        if (mDealWithBlueTooth.isSupportBlueTooth() && mDealWithBlueTooth.isOpen() && mConnectionState && mDealWithBlueTooth != null && writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getDisconnectBlueTooth());
        }
    }
}
