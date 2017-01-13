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
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BlueDataUtil;
import com.goodchef.liking.bluetooth.BlueToothBytesToStringUtil;
import com.goodchef.liking.bluetooth.DealWithBlueTooth;
import com.goodchef.liking.dialog.HeartRateDialog;
import com.goodchef.liking.fragment.LikingMyFragment;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.http.result.data.SportData;
import com.goodchef.liking.mvp.presenter.SportPresenter;
import com.goodchef.liking.mvp.view.SportDataView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

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
    private String myBraceletMac;//我的手环Mac地址
    private String muuId;
    private DealWithBlueTooth mDealWithBlueTooth;//手环处理类
    Handler mHandler = new Handler();
    private boolean isConnect = false;//是否连接
    public BluetoothGattCharacteristic writecharacteristic;
    public BluetoothGattCharacteristic readcharacteristic;
    private boolean isHistory;//同步数据是否开始
    private List<byte[]> mSportMoreDataList = new ArrayList<>();
    private int sportByteLength = 0;
    private HeartRateDialog heartDialog;
    private String sportDate;
    private String heartRateDate;
    private boolean isLoginFail = false;
    private boolean connectFile = false;

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
        searchBlueTooth();
    }

    private void getIntentData() {
        myBraceletMac = getIntent().getStringExtra(LikingMyFragment.KEY_MY_BRACELET_MAC);
        muuId = getIntent().getStringExtra(LikingMyFragment.KEY_UUID);
    }

    private void setViewOnClickListener() {
        mTodayHeartRateLayout.setOnClickListener(this);
    }

    private void setTodayDataView() {
        initTodayView(mTodayStepLayout, "今日步数", " Step", false);
        initTodayView(mTodayDistanceLayout, "距离", " Km", false);
        initTodayView(mTodayKcalLayout, "卡路里", " Kcal", false);
        initTodayView(mTodayHeartRateLayout, "平均心率", " Bpm", true);
    }

    private void setTotalDataView() {
        initTotalView(mTotalStepLayout, "总步数", " Step");
        initTotalView(mTotalDistanceLayout, "总距离", " Km");
        initTotalView(mTotalKcalLayout, "总消耗", " Kcal");
        initTotalView(mTotalHeartRateLayout, "平均心率", " Bpm");
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
        } else {
            searchBlueTooth();
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
        if (!isConnect) {
            isConnect = true;
            mSynchronizationSateTextView.setText("连接中...");
            mSynchronizationSateTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
            mDealWithBlueTooth.connect(myBraceletMac, mGattCallback);
        }
    }

    /**
     * 第二次连接
     */
    private void sendConnect() {
        if (!connectFile) {
            connectFile = true;
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
                sendConnect();
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
                Log.i(TAG, " 回复 data length = " + data.length + " 第" + i + "个字符 " + (data[i] & 0xff));
            }
            readOnCharacteristicChangedData(data);
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
                byte[] uuId = muuId.getBytes();
                mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getLoginBytes(uuId));
                if (readcharacteristic != null) {
                    mDealWithBlueTooth.setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }

    /**
     * 发送运动数据同步
     */
    private void sendSportDataSynchronization() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getSportSynchronizationBytes());
        }
//        if (readcharacteristic != null) {
//            mDealWithBlueTooth.setCharacteristicNotification(readcharacteristic, true);
//        }
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSynchronizationSateTextView.setText("正在同步...");
                            mSynchronizationSateTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
                        }
                    });
                    sendSportDataSynchronization();
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
     * 设置心率
     *
     * @param heartRate 测量的心率数
     */
    private void setHeartRate(final int heartRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendSportDataRequest("", "", "", heartRate + "", heartRateDate);
                dismissHeartRateDialog();
                mTodayAverageHeartRateTextView.setText(heartRate + "");
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
                doSportData();
            } else if ((mSportMoreDataList.get(0)[1] & 0xff) == 0x27) {//心率多包数据
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
        heartRateDate = BlueToothBytesToStringUtil.getSportDate(data);//获取运动时间
        int heartRate = (data[10] & 0xff);
        LogUtils.i(TAG, "单包心率值 == " + heartRate);
        setHeartRate(heartRate);
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
        heartRateDate = BlueToothBytesToStringUtil.getSportDate(sportbyts);//获取运动时间
        List<byte[]> heartRateList = BlueToothBytesToStringUtil.getHeartRateList(sportbyts);
        if (heartRateList != null && heartRateList.size() > 0) {
            byte[] heartRateBytes = heartRateList.get(heartRateList.size() - 1);
            int heartRate = BlueToothBytesToStringUtil.getHeartRate(heartRateBytes);
            LogUtils.i(TAG, "多包心率值中最后一组值 == " + heartRate);
            setHeartRate(heartRate);
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
        sportDate = BlueToothBytesToStringUtil.getSportDate(sportbyts);//获取运动时间
        List<byte[]> sportDataList = BlueToothBytesToStringUtil.getSportDataList(sportbyts);
        if (sportDataList != null && sportDataList.size() > 0) {
            byte[] newSportBytes = sportDataList.get(sportDataList.size() - 1);
            final int step = BlueToothBytesToStringUtil.getSportStep(newSportBytes);
            final String kcal = BlueToothBytesToStringUtil.getSportKcal(newSportBytes);
            final String distance = BlueToothBytesToStringUtil.getSportDistance(newSportBytes);
            sendSportDataRequest(step + "", kcal, distance, "", sportDate);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSynchronizationSateTextView.setText("同步完成");
                    mSynchronizationSateTextView.setTextColor(ResourceUtils.getColor(R.color.c4A90E2));
                    mTodayStepTextView.setText(step + "");
                    mTodayKcalTextView.setText(kcal + "");
                    mTodayDistanceTextView.setText(distance + "");
                }
            });

            if (isHistory) {
                respondSportData();
            }
        }
    }


    /**
     * 回应收到运动数据
     */
    private void respondSportData() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getSportRespondBytes(BlueDataUtil.ZERO));
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
        mSportPresenter.sendSportData(sportDataStr, JPushInterface.getUdid(EveryDaySportActivity.this));
    }


    @Override
    public void updateSendSportDataView() {
        PopupUtils.showToast("上传成功");
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
        String avgBpm = allData.getLastBpm();

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
            sendHeartRateSynchronization();
        }
    }

    /**
     * 展示获取心率dialog
     */
    private void showPromptHeartRateDialog() {
        if (writecharacteristic == null) {
            PopupUtils.showToast("请连接手环");
            return;
        }

        heartDialog = new HeartRateDialog(this);
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
    private void showSharkItOffDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sendCloseSynchronization();
        sendDisconnectBlueTooth();
    }

    /**
     * 发送心率数据同步
     */
    private void sendHeartRateSynchronization() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getHeartRateSynchronizationBytes());
        }
//        if (readcharacteristic != null) {
//            mDealWithBlueTooth.setCharacteristicNotification(readcharacteristic, true);
//        }
    }


    /**
     * 发送关闭蓝牙命令
     */
    private void sendDisconnectBlueTooth() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getDisconnectBlueTooth());
        }
    }

    /**
     * 发送关闭实时同步运动数据
     */
    private void sendCloseSynchronization() {
        if (writecharacteristic != null) {
            mDealWithBlueTooth.wirteCharacteristic(writecharacteristic, BlueDataUtil.getCloseSynchronizationBytes());
        }
    }
}
