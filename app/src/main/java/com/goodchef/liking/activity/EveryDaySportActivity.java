package com.goodchef.liking.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
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
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BlueDataUtil;
import com.goodchef.liking.bluetooth.DealWithBlueTooth;
import com.goodchef.liking.dialog.HeartRateDialog;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.mvp.presenter.SportPresenter;
import com.goodchef.liking.mvp.view.SportDataView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:每日运动
 * Author : shaozucheng
 * Time: 下午6:46
 * version 1.0.0
 */

public class EveryDaySportActivity extends AppBarActivity implements View.OnClickListener, SportDataView {

    @BindView(R.id.layout_today_step)
    RelativeLayout mTodayStepLayout;
    @BindView(R.id.layout_today_distance)
    RelativeLayout mTodayDistanceLayout;
    @BindView(R.id.layout_today_kcal)
    RelativeLayout mTodayKcalLayout;
    @BindView(R.id.layout_average_heart_rate)
    RelativeLayout mTodayHeartRateLayout;

    @BindView(R.id.layout_today_total_step)
    RelativeLayout mTotalStepLayout;
    @BindView(R.id.layout_today_total_distance)
    RelativeLayout mTotalDistanceLayout;
    @BindView(R.id.layout_today_total_kcal)
    RelativeLayout mTotalKcalLayout;
    @BindView(R.id.layout_today_total_average_heart_rate)
    RelativeLayout mTotalHeartRateLayout;

    TextView mTodayStepTextView;
    TextView mTodayDistanceTextView;
    TextView mTodayKcalTextView;
    TextView mTodayAverageHeartRateTextView;

    TextView mTotoalStepTextView;
    TextView mTotalDistanceTextView;
    TextView mTotalKcalTextView;
    TextView mTotalAverageHeartRateTextView;
    ImageView mClikHeartRateImageView;

    @BindView(R.id.synchronization_sate_TextView)
    TextView mSynchronizationSateTextView;

    private SportPresenter mSportPresenter;
    private String myBraceletMac;
    private DealWithBlueTooth mDealWithBlueTooth;//手环处理类
    Handler mHandler = new Handler();
    private boolean isConnect = false;
    public BluetoothGattCharacteristic writecharacteristic;
    public BluetoothGattCharacteristic readcharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_sport);
        ButterKnife.bind(this);
        getIntentData();
        mDealWithBlueTooth = new DealWithBlueTooth(this);
        setTitle("日常运动");
        setTodayDataView();
        setTotalDataView();
        setViewOnClickListener();
        sendRequest();
        initBlueTooth();
    }

    private void getIntentData() {
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
    }

    private void setViewOnClickListener() {
        mTodayHeartRateLayout.setOnClickListener(this);
    }

    private void setTodayDataView() {
        initTodayView(mTodayStepLayout, "今日步数", "Step", false);
        initTodayView(mTodayDistanceLayout, "距离", "Km", false);
        initTodayView(mTodayKcalLayout, "卡路里", "Kcal", false);
        initTodayView(mTodayHeartRateLayout, "平均心率", "Bpm", true);
    }

    private void setTotalDataView() {
        initTotalView(mTotalStepLayout, "总步数", "Step");
        initTotalView(mTotalDistanceLayout, "总距离", "Km");
        initTotalView(mTotalKcalLayout, "总消耗", "Kcal");
        initTotalView(mTotalHeartRateLayout, "平均心率", "Bpm");
    }

    private void initTodayView(View view, String title, String unit, boolean showImageView) {
        TextView titleTextView = (TextView) view.findViewById(R.id.every_day_data_title);
        TextView unitTextView = (TextView) view.findViewById(R.id.every_day_data_unit);
        TextView contentTextView = (TextView) view.findViewById(R.id.every_day_data_content);
        ImageView imageView = (ImageView) view.findViewById(R.id.click_heart_rate_ImageView);
        titleTextView.setText(title);
        unitTextView.setText(unit);
        if (showImageView) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        switch (view.getId()) {
            case R.id.layout_today_step:
                mTodayStepTextView = contentTextView;
                break;
            case R.id.layout_today_distance:
                mTodayDistanceTextView = contentTextView;
                break;
            case R.id.layout_today_kcal:
                mTodayKcalTextView = contentTextView;
                break;
            case R.id.layout_average_heart_rate:
                mTodayAverageHeartRateTextView = contentTextView;
                mClikHeartRateImageView = imageView;
                break;
        }

    }


    private void initTotalView(View view, String title, String unit) {
        TextView titleTextView = (TextView) view.findViewById(R.id.every_day_total_data_title);
        TextView unitTextView = (TextView) view.findViewById(R.id.every_day_total_data_unit);
        TextView contentTextView = (TextView) view.findViewById(R.id.every_day_total_data_content);
        titleTextView.setText(title);
        unitTextView.setText(unit);
        switch (view.getId()) {
            case R.id.layout_today_total_step:
                mTotoalStepTextView = contentTextView;
                break;
            case R.id.layout_today_total_distance:
                mTotalDistanceTextView = contentTextView;
                break;
            case R.id.layout_today_total_kcal:
                mTotalKcalTextView = contentTextView;
                break;
            case R.id.layout_today_total_average_heart_rate:
                mTotalAverageHeartRateTextView = contentTextView;
                break;
        }
    }

    private void sendRequest() {
        if (mSportPresenter == null) {
            mSportPresenter = new SportPresenter(this, this);
        }
        mSportPresenter.getSortData();
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
                    mDealWithBlueTooth.stopLeScan(mLeScanCallback);
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
                                connect();
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
        if (!isConnect) {
            isConnect = true;
            mDealWithBlueTooth.connect(myBraceletMac, mGattCallback);
        }
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                Log.i(TAG, "连接成功");
                mDealWithBlueTooth.mBluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                Log.i(TAG, "Attempting to start service discovery:" + mDealWithBlueTooth.mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
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
//                    if (!isLoginFail) {
//                        isLoginFail = true;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showLoginFail();
//                            }
//                        });
//                    }
                }
            } else if ((data[1] & 0xff) == 0x0D) {
                if (data[4] == 0x00) {
                    Log.i(TAG, "解绑成功");
                } else if (data[4] == 0x01) {
                    Log.i(TAG, "解绑失败");
                }
            } else if ((data[1] & 0xff) == 0x09) {//电量
                Log.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
            } else if ((data[1] & 0xff) == 0x27) {
                Log.i(TAG, "心率 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                Log.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
            }
        }
    }


    @Override
    public void updateSendSportDataView() {

    }

    @Override
    public void updateGetSportDataView(SportDataResult.SportData sportData) {
        SportDataResult.SportData.AllData allData = sportData.getAll();
        SportDataResult.SportData.TodayData todayData = sportData.getToday();

        setTodayData(todayData);
        setAllData(allData);
    }

    /**
     * 设置今天的数据
     *
     * @param todayData
     */
    private void setTodayData(SportDataResult.SportData.TodayData todayData) {
        String todayStepNum = todayData.getStepNum();
        String todayDistance = todayData.getDistance();
        String todayKacl = todayData.getKcal();

        if (!StringUtils.isEmpty(todayStepNum)) {
            mTodayStepTextView.setText(todayStepNum);
        }
        if (!StringUtils.isEmpty(todayDistance)) {
            mTodayDistanceTextView.setText(todayDistance);
        }
        if (!StringUtils.isEmpty(todayKacl)) {
            mTodayKcalTextView.setText(todayKacl);
        }
    }

    /**
     * 设置全部数据
     *
     * @param allData
     */
    private void setAllData(SportDataResult.SportData.AllData allData) {
        String allStep = allData.getAllStep();
        String allDistance = allData.getAllDistance();
        String allKcal = allData.getAllKcal();
        String avgBpm = allData.getAvgBpm();

        if (!StringUtils.isEmpty(allStep)) {
            mTotoalStepTextView.setText(allStep);
        }
        if (!StringUtils.isEmpty(allDistance)) {
            mTotalDistanceTextView.setText(allDistance);
        }

        if (!StringUtils.isEmpty(allKcal)) {
            mTotalKcalTextView.setText(allKcal);
        }

        if (!StringUtils.isEmpty(avgBpm)) {
            mTotalAverageHeartRateTextView.setText(avgBpm);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTodayHeartRateLayout) {
            showPromptHeartRateDialog();
        }
    }

    private void showPromptHeartRateDialog() {
        HeartRateDialog heartDialog = new HeartRateDialog(this);
        heartDialog.setCancelClickListener(new HeartRateDialog.CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });
    }
}
