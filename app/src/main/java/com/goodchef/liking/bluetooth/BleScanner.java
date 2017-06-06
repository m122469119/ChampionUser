package com.goodchef.liking.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import android.support.annotation.RequiresApi;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;

/**
 * 低功耗蓝牙设备搜索器
 * Created on 17/1/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BleScanner {
    public static final int SCANNER_DELAY_MILLIS = 45000;
    private Context mContext;
    private boolean mIsScanning = false;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    public BleScanner(Context context) {
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mContext = context;
    }

    public void setLeScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
        mLeScanCallback = leScanCallback;
    }

    /**
     * 扫描低功耗蓝牙设备
     *
     * @param enable
     */
    public void scanLeDevice(final boolean enable) {
        if (!BleUtils.isSupportBleDevice(mContext)) {
            PopupUtils.showToast(mContext, mContext.getString(R.string.devices_no_support_ble));
            return;
        }
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCANNER_DELAY_MILLIS);
            mIsScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mIsScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }



    /**
     * 判断是否正在扫描中
     *
     * @return true: 是
     */
    public boolean isScanning() {
        return mIsScanning;
    }

    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mContext = null;
        mLeScanCallback = null;
        mBluetoothAdapter = null;
    }
}
