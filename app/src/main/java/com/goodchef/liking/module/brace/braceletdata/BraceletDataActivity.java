package com.goodchef.liking.module.brace.braceletdata;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleManager;
import com.goodchef.liking.bluetooth.BleService;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.bluetooth.BlueToothBytesToStringUtil;
import com.goodchef.liking.dialog.HeartRateDialog;
import com.goodchef.liking.dialog.ShakeSynchronizationDialog;
import com.goodchef.liking.eventmessages.ServiceConnectionMessage;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.http.result.data.SportData;
import com.goodchef.liking.module.home.myfragment.LikingMyFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 说明:手环数据
 * Author : shaozucheng
 * Time: 下午6:46
 * version 1.0.0
 */

public class BraceletDataActivity extends AppBarActivity implements View.OnClickListener, BraceletDataContract.BraceletDataView {

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
    @BindView(R.id.synchronization_sate_TextView)
    TextView mSynchronizationSateTextView;

    private TextView mTodayStepTextView;
    private TextView mTodayStepUnitTextView;
    private TextView mTodayDistanceTextView;
    private TextView mTodayDistanceUnitTextView;
    private TextView mTodayKcalTextView;
    private TextView mTodayKcalUnitTextView;
    private TextView mTodayAverageHeartRateTextView;
    private TextView mTodayAverageHeartRateUnitTextView;

    private ProgressBar mTodayStepProgressBar;
    private ProgressBar mTodayDistanceProgressBar;
    private ProgressBar mTodayKcalProgressBar;
    private ProgressBar mTodayHeartRateProgressBar;

    private TextView mTotoalStepTextView;
    private TextView mTotalDistanceTextView;
    private TextView mTotalKcalTextView;
    private TextView mTotalAverageHeartRateTextView;

    private BraceletDataContract.BraceletDataPresenter mBraceletDataPresenter;
    private String myBraceletMac;//我的手环Mac地址
    private String muuId;
    private Handler mHandler = new Handler();
    private boolean isConnect = false;//是否连接
    public BluetoothGattCharacteristic writecharacteristic;
    public BluetoothGattCharacteristic readcharacteristic;
    private boolean isHistory;//同步数据是否开始
    private List<byte[]> mSportMoreDataList = new ArrayList<>();
    private int sportByteLength = 0;
    private HeartRateDialog heartDialog;

    private String sportDate = "";//运动数据时间
    private int sportSetp = 0;//运动步数
    private String sportKcal = "";//运动kcal
    private String sportDistance = "";//运动距离
    private String mHeartRate = "";//心率
    private int currentSportStep = 0;
    private String currentSportKcal = "";
    private String currentSportDistance = "";

    private boolean isLoginFail = false;//是否登录失败
    private boolean connectFile = false;//是否连接失败
    private ShakeSynchronizationDialog mShakeSynchronizationDialog;//摇一摇对话框
    private boolean isSynchronization = false;//是否同步完成
    private boolean isFirsSendSportData = true;//是否是第一次上传运动数据
    private boolean mConnectionState = false;

    private BleManager mBleManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_sport);
        ButterKnife.bind(this);
        mBraceletDataPresenter = new BraceletDataContract.BraceletDataPresenter(this,this);
        getIntentData();
        mBleManager = new BleManager(this, mLeScanCallback);
        mBleManager.bind();
        setTitle(getString(R.string.title_every_day_sport));
        setTodayDataView();
        mSynchronizationSateTextView.setVisibility(View.GONE);
        setTotalDataView();
        setViewOnClickListener();
        sendRequest();
        searchBlueTooth();
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
        if (initBlueTooth() && !StringUtils.isEmpty(myBraceletMac)) {
            connect();
        } else {
            mSynchronizationSateTextView.setVisibility(View.VISIBLE);
            setSynchronizationSate(getString(R.string.connect_fial), ResourceUtils.getColor(R.color.c4A90E2));
        }
    }


    private void showProgressBar(boolean show) {
        if (show) {
            mTodayStepProgressBar.setVisibility(View.VISIBLE);
            mTodayDistanceProgressBar.setVisibility(View.VISIBLE);
            mTodayKcalProgressBar.setVisibility(View.VISIBLE);
            mTodayHeartRateProgressBar.setVisibility(View.VISIBLE);
        } else {
            mTodayStepProgressBar.setVisibility(View.GONE);
            mTodayDistanceProgressBar.setVisibility(View.GONE);
            mTodayKcalProgressBar.setVisibility(View.GONE);
            mTodayHeartRateProgressBar.setVisibility(View.GONE);
        }
    }

    private void getIntentData() {
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
    }

    private void setViewOnClickListener() {
        mTodayHeartRateLayout.setOnClickListener(this);
    }

    private void setTodayDataView() {
        initTodayView(mTodayStepLayout, getString(R.string.today_step), getString(R.string.total_setp_unit), false);
        initTodayView(mTodayDistanceLayout, getString(R.string.today_distance), getString(R.string.total_distance_unit), false);
        initTodayView(mTodayKcalLayout, getString(R.string.today_kcal), getString(R.string.tota_kcal_unit), false);
        initTodayView(mTodayHeartRateLayout, getString(R.string.today_heart_rate), getString(R.string.history_heart_rate_unit), true);
    }

    private void setTotalDataView() {
        initTotalView(mTotalStepLayout, getString(R.string.total_setp), getString(R.string.total_setp_unit));
        initTotalView(mTotalDistanceLayout, getString(R.string.total_distance), getString(R.string.total_distance_unit));
        initTotalView(mTotalKcalLayout, getString(R.string.total_kacl), getString(R.string.tota_kcal_unit));
        initTotalView(mTotalHeartRateLayout, getString(R.string.history_heart_rate), getString(R.string.history_heart_rate_unit));
    }

    private void initTodayView(View view, String title, String unit, boolean showImageView) {
        TextView titleTextView = (TextView) view.findViewById(R.id.every_day_data_title);
        TextView unitTextView = (TextView) view.findViewById(R.id.every_day_data_unit);
        TextView contentTextView = (TextView) view.findViewById(R.id.every_day_data_content);
        ImageView imageView = (ImageView) view.findViewById(R.id.click_heart_rate_ImageView);
        ProgressBar mProgressBar = (ProgressBar) view.findViewById(R.id.every_day_ProgressBar);
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
                mTodayStepUnitTextView = unitTextView;
                mTodayStepProgressBar = mProgressBar;
                break;
            case R.id.layout_today_distance:
                mTodayDistanceTextView = contentTextView;
                mTodayDistanceUnitTextView = unitTextView;
                mTodayDistanceProgressBar = mProgressBar;
                break;
            case R.id.layout_today_kcal:
                mTodayKcalTextView = contentTextView;
                mTodayKcalUnitTextView = unitTextView;
                mTodayKcalProgressBar = mProgressBar;
                break;
            case R.id.layout_average_heart_rate:
                mTodayAverageHeartRateTextView = contentTextView;
                mTodayAverageHeartRateUnitTextView = unitTextView;
                mTodayHeartRateProgressBar = mProgressBar;
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
        mBraceletDataPresenter.getSportData();
    }

    /**
     * 初始化蓝牙
     */
    public boolean initBlueTooth() {
        if (!mBleManager.isOpen()) {
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
                            if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                                LogUtils.i(TAG, "name = " + device.getName() + " mac = " + device.getAddress());
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
        if (!initBlueTooth()) {
            return;
        }
        if (!isConnect) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSynchronizationSateTextView.getVisibility() == View.GONE) {
                        mSynchronizationSateTextView.setVisibility(View.VISIBLE);
                    }
                    setTodayRightView(false);
                    showProgressBar(true);
                    setSynchronizationSate(getString(R.string.connect_ing), ResourceUtils.getColor(R.color.c4A90E2));
                }
            });
            isConnect = true;
            mBleManager.connect(myBraceletMac);
        }
    }

    /**
     * 设置右边显示的文字和动画切换
     */
    private void setTodayRightView(boolean showUnit) {
        if (showUnit) {
            mTodayStepTextView.setVisibility(View.VISIBLE);
            mTodayStepUnitTextView.setVisibility(View.VISIBLE);
            mTodayDistanceTextView.setVisibility(View.VISIBLE);
            mTodayDistanceUnitTextView.setVisibility(View.VISIBLE);
            mTodayKcalTextView.setVisibility(View.VISIBLE);
            mTodayKcalUnitTextView.setVisibility(View.VISIBLE);
            mTodayAverageHeartRateTextView.setVisibility(View.VISIBLE);
            mTodayAverageHeartRateUnitTextView.setVisibility(View.VISIBLE);
        } else {
            mTodayStepTextView.setVisibility(View.GONE);
            mTodayStepUnitTextView.setVisibility(View.GONE);
            mTodayDistanceTextView.setVisibility(View.GONE);
            mTodayDistanceUnitTextView.setVisibility(View.GONE);
            mTodayKcalTextView.setVisibility(View.GONE);
            mTodayKcalUnitTextView.setVisibility(View.GONE);
            mTodayAverageHeartRateTextView.setVisibility(View.GONE);
            mTodayAverageHeartRateUnitTextView.setVisibility(View.GONE);
        }

    }


    /**
     * 设置日常运动状态
     *
     * @param str
     * @param color
     */
    private void setSynchronizationSate(final String str, final int color) {
        if (mConnectionState) {
            if (mBleManager != null) {
                mBleManager.stopScan();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSynchronizationSateTextView.getVisibility() == View.VISIBLE) {
                    mSynchronizationSateTextView.setText(str);
                    mSynchronizationSateTextView.setTextColor(color);
                }
            }
        });
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!initBlueTooth()) {
            return;
        }
        if (!connectFile) {
            connectFile = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTodayRightView(false);
                    showProgressBar(true);
                }
            });
            mBleManager.connect(myBraceletMac);
        }
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

    /**
     * 设置连接状态view，连接成功或者失败的view
     */
    private void setConnectStateView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                setTodayRightView(true);
            }
        });
    }

    /**
     * 发送运动数据同步
     */
    private void sendSportDataSynchronization() {
        if (writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getSportSynchronizationBytes());
        }
    }

    /**
     * 读取蓝牙返回的数据
     *
     * @param data
     */
    private void readOnCharacteristicChangedData(byte[] data) {
        if ((data[0] & 0xff) == 169) {//只有一个包
            doCharacteristicOnePackage(data);
        } else {//分包处理
            doCharacteristicMorePackageOtherData(data);
        }
    }


    private void doCharacteristicOnePackage(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }
        if (data.length > 3) {//
            int dataLength = (data[3] & 0xff);
            LogUtils.i(TAG, "  数据包长度==  " + dataLength);
            if ((dataLength + 5) == data.length) {
                doCharacteristicOnePackageData(data);
            } else {
                doCharacteristicMorePackageFirstData(data);
            }
        }

    }

    private void doCharacteristicMorePackageFirstData(byte[] data) {
        mSportMoreDataList.clear();
        sportByteLength = 0;
        sportByteLength = (data[3] & 0xff) + 5;
        LogUtils.i(TAG, "ByteLength == " + sportByteLength);
        mSportMoreDataList.add(data);
    }

    /**
     * 处理蓝牙返回的数据 一个包的数据
     */
    private void doCharacteristicOnePackageData(byte[] data) {
        if (data != null && data.length >= 3) {
            if ((data[1] & 0xff) == 0x33) {//绑定
                if (data[4] == 0x00) {
                    Log.i(TAG, "绑定成功");
                } else if (data[4] == 0x01) {
                    Log.i(TAG, "绑定失败");
                }
            } else if ((data[1] & 0xff) == 0x35) {
                if (data[4] == 0x00) {
                    Log.i(TAG, "登录成功");
                    setBlueToothTime();
                    loginSuccess();
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
            } else if ((data[1] & 0xff) == 0x27) {
                doOnePackageHeartRate(data);
            } else if ((data[1] & 0xff) == 0x21) {//运动数据返回

            } else if ((data[1] & 0xff) == 0x31) {//固件版本信息

            } else if ((data[1] & 0xff) == 0x25) {//历史数据同步指示
                Log.i(TAG, "历史数据同步指示 == " + (data[4] & 0xff));
                if ((data[4] & 0xff) == 0) {//开始同步
                    isHistory = true;
                } else if ((data[4] & 0xff) == 1) {//同步结束
                    isHistory = false;
                }
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
     * 登录成功
     */
    private void loginSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showShakeDialog();
            }
        });
        //设置状态
        setSynchronizationSate(getString(R.string.synchronization_ing), ResourceUtils.getColor(R.color.c4A90E2));
        isSynchronization = false;
        sendSportDataSynchronization();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSynchronization) {
                    dismissShakeDialog();
                    setSynchronizationSate(getString(R.string.synchronization_fial), ResourceUtils.getColor(R.color.red));
                }
            }
        }, 30000);
    }


    private void showLoginFail() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.login_fail));
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
     * 设置心率
     *
     * @param heartRate 测量的心率数
     */
    private void setHeartRate(final String heartRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendSportDataRequest("", "", "", heartRate, sportDate);
                dismissHeartRateDialog();
                mTodayAverageHeartRateTextView.setText(heartRate);
            }
        });
    }

    /**
     * 蓝牙数据，分包的数据
     *
     * @param data
     */
    private void doCharacteristicMorePackageOtherData(byte[] data) {
        if (getMoreListDataLength() < sportByteLength) {
            mSportMoreDataList.add(data);
            if (getMoreListDataLength() == sportByteLength) {
                doMoreSportData();
            }
        } else {
            doMoreSportData();
        }
    }

    /**
     * 多包数据长度
     *
     * @return
     */
    private int getMoreListDataLength() {
        int sum = 0;
        for (int i = 0; i < mSportMoreDataList.size(); i++) {
            sum += mSportMoreDataList.get(i).length;
        }
        LogUtils.i(TAG, "MoreLength == " + sportByteLength + "  sum ==" + sum);
        return sum;
    }

    /**
     * 处理多包数据
     */
    private void doMoreSportData() {
        if (mSportMoreDataList.size() > 0) {
            LogUtils.i(TAG, "多包数据返回命令 ==" + (mSportMoreDataList.get(0)[1] & 0xff));
            if ((mSportMoreDataList.get(0)[1] & 0xff) == 0x21) {//运动数据
                isSynchronization = true;
                setConnectStateView();
                doSportData();
            } else if ((mSportMoreDataList.get(0)[1] & 0xff) == 0x27) {//心率多包数据
                setConnectStateView();
                doMorePackageHeartRate();
            }
        }
    }

    /**
     * 处理单包心率数据
     *
     * @param data
     */
    private void doOnePackageHeartRate(byte[] data) {
        sportDate = BlueToothBytesToStringUtil.getSportDate(data);//获取运动时间
        int rate = (data[10] & 0xff);
        mHeartRate = rate + "";
        LogUtils.i(TAG, "单包心率值 == " + mHeartRate);
        setHeartRate(mHeartRate);
    }

    /**
     * 处理多包心率数据
     */
    private void doMorePackageHeartRate() {
        int size = (mSportMoreDataList.get(0)[3] & 0xff) + 5;
        byte[] sportbyts = new byte[size];
        int leng = 0;
        for (int i = 0; i < mSportMoreDataList.size(); i++) {
            byte[] bytes = mSportMoreDataList.get(i);
            for (int j = 0; j < bytes.length; j++) {
                sportbyts[leng] = bytes[j];
                leng += 1;
            }
        }
        Log.i(TAG, "多包心率数据长度 == ***** " + sportbyts.length);
        sportDate = BlueToothBytesToStringUtil.getSportDate(sportbyts);//获取运动时间
        List<byte[]> heartRateList = BlueToothBytesToStringUtil.getHeartRateList(sportbyts);
        if (heartRateList != null && heartRateList.size() > 0) {
            byte[] heartRateBytes = heartRateList.get(heartRateList.size() - 1);
            int heartRate = BlueToothBytesToStringUtil.getHeartRate(heartRateBytes);
            LogUtils.i(TAG, "多包心率值中最后一组值 == " + heartRate);
            mHeartRate = heartRate + "";
            setHeartRate(mHeartRate);
        }
    }

    /**
     * 处理运动数据
     */
    private void doSportData() {
        int size = (mSportMoreDataList.get(0)[3] & 0xff) + 5;
        byte[] sportbyts = new byte[size];
        int leng = 0;
        for (int i = 0; i < mSportMoreDataList.size(); i++) {
            byte[] bytes = mSportMoreDataList.get(i);
            for (int j = 0; j < bytes.length; j++) {
                sportbyts[leng] = bytes[j];
                leng += 1;
            }
        }
        Log.i(TAG, "多包运动数据长度 == ***** " + sportbyts.length);
        String date = BlueToothBytesToStringUtil.getSportDate(sportbyts);//获取运动时间
        sportDate = date;
        List<byte[]> sportDataList = BlueToothBytesToStringUtil.getSportDataList(sportbyts);
        if (sportDataList != null && sportDataList.size() > 0) {
            byte[] newSportBytes = sportDataList.get(sportDataList.size() - 1);
            final int step = BlueToothBytesToStringUtil.getSportStep(newSportBytes);
            final String kcal = BlueToothBytesToStringUtil.getSportKcal(newSportBytes);
            final String distance = BlueToothBytesToStringUtil.getSportDistance(newSportBytes);
            currentSportStep = step;
            currentSportDistance = distance;
            currentSportKcal = kcal;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dosendSportData(step, kcal, distance);
                }
            }, 60000);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissShakeDialog();
                    mTodayStepTextView.setText(step + "");
                    mTodayKcalTextView.setText(kcal + "");
                    mTodayDistanceTextView.setText(distance + "");
                }
            });
            setSynchronizationSate(getString(R.string.synchongrozation_finish), ResourceUtils.getColor(R.color.c4A90E2));
            hideSynchronizationSateView();
            if (isHistory) {
                respondSportData();
            }
        }
    }

    private void hideSynchronizationSateView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSynchronizationSateTextView.getVisibility() == View.VISIBLE) {
                    mSynchronizationSateTextView.setVisibility(View.GONE);
                }
            }
        }, 5000);
    }

    /**
     * 处理并上报运动数据
     *
     * @param step
     * @param kcal
     * @param distance
     */
    private void dosendSportData(int step, String kcal, String distance) {
        if (isFirsSendSportData) {//是否是第一次上包运动数据
            isFirsSendSportData = false;
            sportSetp = step;
            sportKcal = kcal;
            sportDistance = distance;
            sendSportDataRequest(sportSetp + "", sportKcal, sportDistance, "", sportDate);
        } else {
            if (sportSetp != 0 && sportSetp != step) {
                sportSetp = step;
                sendSportDataRequest(sportSetp + "", "", "", "", sportDate);
            }
            if (!StringUtils.isEmpty(sportKcal) && Double.parseDouble(sportKcal) != Double.parseDouble(kcal)) {
                sportKcal = kcal;
                sendSportDataRequest("", sportKcal, "", "", sportDate);
            }
            if (!StringUtils.isEmpty(sportDistance) && Double.parseDouble(sportDistance) != Double.parseDouble(distance)) {
                sportDistance = distance;
                sendSportDataRequest("", "", sportDistance, "", sportDate);
            }
        }
    }


    /**
     * 回应收到运动数据
     */
    private void respondSportData() {
        if (writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getSportRespondBytes(BlueCommandUtil.ZERO));
        }
    }


    /**
     * 上报运动数据
     *
     * @param setpNum  步数
     * @param kcal     卡路里
     * @param distance 距离
     * @param bpm      心率
     * @param time     时间
     */
    private void sendSportDataRequest(String setpNum, String kcal, String distance, String bpm, String time) {
        List<SportData> sportDataList = new ArrayList<>();
        SportData sportData = new SportData();
        if (!StringUtils.isEmpty(setpNum)) {
            sportData.setStep_num(setpNum);
        }
        if (!StringUtils.isEmpty(kcal)) {
            sportData.setKcal(kcal);
        }
        if (!StringUtils.isEmpty(distance)) {
            sportData.setDistance(distance);
        }
        if (!StringUtils.isEmpty(bpm)) {
            sportData.setBpm(bpm);
        }
        sportData.setCreate_time(time);
        sportDataList.add(sportData);
        Gson gson = new Gson();
        String sportDataStr = gson.toJson(sportDataList);
        mBraceletDataPresenter.sendSportData(sportDataStr, JPushInterface.getUdid(BraceletDataActivity.this));
    }


    @Override
    public void updateSendSportDataView() {
        // PopupUtils.showToast("上传成功");
    }

    @Override
    public void updateGetSportDataView(SportDataResult.SportData sportData) {
        SportDataResult.SportData.AllData allData = sportData.getAll();
        SportDataResult.SportData.TodayData todayData = sportData.getToday();
        setTodayData(todayData);//设置今日数据
        setAllData(allData);//设置总运动数据
    }

    /**
     * 设置今天的数据
     *
     * @param todayData
     */
    private void setTodayData(SportDataResult.SportData.TodayData todayData) {
        showProgressBar(false);
        setTodayRightView(true);
        String todayStepNum = todayData.getStepNum();
        String todayDistance = todayData.getDistance();
        String todayKacl = todayData.getKcal();
        String todaybpm = todayData.getBpm();

        if (!StringUtils.isEmpty(todayStepNum)) {
            mTodayStepTextView.setText(todayStepNum);
        } else {
            mTodayStepTextView.setText("-");
        }
        if (!StringUtils.isEmpty(todayDistance)) {
            mTodayDistanceTextView.setText(todayDistance);
        } else {
            mTodayDistanceTextView.setText("-");
        }
        if (!StringUtils.isEmpty(todayKacl)) {
            mTodayKcalTextView.setText(todayKacl);
        } else {
            mTodayKcalTextView.setText("-");
        }
        if (!StringUtils.isEmpty(todaybpm)) {
            mTodayAverageHeartRateTextView.setText(todaybpm);
        } else {
            mTodayAverageHeartRateTextView.setText("-");
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
        String avgBpm = allData.getLastBpm();

        if (!StringUtils.isEmpty(allStep)) {
            mTotoalStepTextView.setText(allStep);
        } else {
            mTotoalStepTextView.setText("-");
        }
        if (!StringUtils.isEmpty(allDistance)) {
            mTotalDistanceTextView.setText(allDistance);
        } else {
            mTotalDistanceTextView.setText("-");
        }

        if (!StringUtils.isEmpty(allKcal)) {
            mTotalKcalTextView.setText(allKcal);
        } else {
            mTotalKcalTextView.setText("-");
        }

        if (!StringUtils.isEmpty(avgBpm)) {
            mTotalAverageHeartRateTextView.setText(avgBpm);
        } else {
            mTotalAverageHeartRateTextView.setText("-");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTodayHeartRateLayout) {
            showPromptHeartRateDialog();
            sendHeartRateSynchronization();
        }
    }

    /**
     * 展示获取心率dialog
     */
    private void showPromptHeartRateDialog() {
        if (writecharacteristic == null) {
            showToast(getString(R.string.connect_bluetooth_heart_rate));
            return;
        }
        if (heartDialog == null) {
            heartDialog = new HeartRateDialog(this);
        } else {
            heartDialog.show();
        }
        heartDialog.setCancelClickListener(new HeartRateDialog.CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                sendSportDataSynchronization();
                dialog.dismiss();
            }
        });
    }


    private void dismissHeartRateDialog() {
        sendSportDataSynchronization();
        if (heartDialog != null) {
            heartDialog.dismiss();
        }
    }

    /**
     * 显示摇一摇对话框
     */
    private void showShakeDialog() {
        if (mShakeSynchronizationDialog == null) {
            mShakeSynchronizationDialog = new ShakeSynchronizationDialog(this);
        }
        mShakeSynchronizationDialog.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShakeSynchronizationDialog.dismiss();
            }
        });
    }

    private void dismissShakeDialog() {
        if (mShakeSynchronizationDialog != null) {
            mShakeSynchronizationDialog.dismiss();
        }
        mSynchronizationSateTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBleManager != null) {
            mBleManager.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isConnect = false;
        if (!StringUtils.isEmpty(sportDate)) {
            sendSportDataRequest(currentSportStep + "", currentSportKcal, currentSportDistance, mHeartRate + "", sportDate);
        }
        sendCloseSynchronization();
        sendDisconnectBlueTooth();
        unregisterReceiver(mGattUpdateReceiver);
    }

    /**
     * 发送心率数据同步
     */
    private void sendHeartRateSynchronization() {
        if (writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getHeartRateSynchronizationBytes());
        }
    }

    /**
     * 发送关闭蓝牙命令
     */
    private void sendDisconnectBlueTooth() {
        if (mBleManager.isOpen() && mConnectionState && writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getDisconnectBlueTooth());
        }
    }

    /**
     * 发送关闭实时同步运动数据
     */
    private void sendCloseSynchronization() {
        if (mBleManager.isOpen() && mConnectionState && writecharacteristic != null) {
            mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getCloseSynchronizationBytes());
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "连接成功");
                mConnectionState = true;
                setSynchronizationSate(getString(R.string.connect_success), ResourceUtils.getColor(R.color.c4A90E2));
                setConnectStateView();
                mBleManager.discoverServices();
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnectionState = false;
                setSynchronizationSate(getString(R.string.connect_fial), ResourceUtils.getColor(R.color.c4A90E2));
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
                readOnCharacteristicChangedData(data);
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
