package com.goodchef.liking.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.aaron.android.codelibrary.utils.LogUtils;

import java.util.List;

/**
 * Created on 17/1/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BleManager {
    private static final String TAG = "BleManager";
    private BleService mBleService;
    private BleScanner mBleScanner;
    private BleUtils mBleUtils;
    private Context mContext;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBleService = ((BleService.BleServiceBinder) service).getService();
            if (!mBleService.initialize()) {
                LogUtils.e(TAG, "Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBleService = null;
        }
    };

    public BleManager(Context context, BluetoothAdapter.LeScanCallback leScanCallback) {
        mBleScanner = new BleScanner(context);
        mBleScanner.setLeScanCallback(leScanCallback);
        mBleUtils = new BleUtils();
        mContext = context;
    }

    public BleUtils getBleUtils() {
        return mBleUtils;
    }

    /**
     * 初始化蓝牙
     */
    public boolean isOpen() {
        if (!mBleUtils.isOpen()) {
            return false;
        } else {
            return true;
        }
    }


    public void bind() {
        Intent gattServiceIntent = new Intent(mContext, BleService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void release() {
        mBleScanner.setLeScanCallback(null);
        mContext.unbindService(mServiceConnection);
        mBleService = null;
        mBleScanner = null;
        mBleUtils = null;
    }

    public void doScan(boolean enable) {
        mBleScanner.scanLeDevice(enable);
    }

    public void startScan() {
        doScan(true);
    }

    public void stopScan() {
        doScan(false);
    }

    public void connect(String address) {
        mBleService.connect(address);
    }

    public void discoverServices() {
        mBleService.discoverServices();
    }

    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        mBleService.wirteCharacteristic(characteristic, data);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic readcharacteristic, boolean enable) {
        mBleService.setCharacteristicNotification(readcharacteristic, enable);
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        return mBleService.getSupportedGattServices();
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBleService.getBluetoothGatt();
    }
}