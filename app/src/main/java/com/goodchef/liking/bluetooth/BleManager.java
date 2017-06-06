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

import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.eventmessages.ServiceConnectionMessage;

import java.util.List;

import de.greenrobot.event.EventBus;

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
    private boolean mIsBind = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mIsBind = true;
            mBleService = ((BleService.BleServiceBinder) service).getService();
            if (!mBleService.initialize()) {
                LogUtils.e(TAG, "Unable to initialize Bluetooth");
                EventBus.getDefault().post(new ServiceConnectionMessage());
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
        if (mBleUtils == null || !mBleUtils.isOpen()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isScanning() {
        return mBleScanner.isScanning();
    }

    public void bind() {
        Intent gattServiceIntent = new Intent(mContext, BleService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void release() {
        if (mBleService != null)
            mBleService.disconnect();
        mBleScanner.setLeScanCallback(null);
        if (mIsBind) {
            mContext.unbindService(mServiceConnection);
            mIsBind = false;
        }
        mBleService = null;
        mBleScanner = null;
        mBleUtils = null;
    }

    public void doScan(boolean enable) {
        if (mBleScanner ==null){
            return;
        }
        if (mBleScanner.isScanning()) {
            return;
        }
        mBleScanner.scanLeDevice(enable);
    }

    public void startScan() {
        doScan(true);
    }

    public void stopScan() {
        doScan(false);
    }

    public void connect(String address) {
        if (mBleService != null) {
            mBleService.connect(address);
        }
    }

    public void discoverServices() {
        if (mBleService != null) {
            mBleService.discoverServices();
        }
    }

    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        if (mBleService != null) {
            mBleService.wirteCharacteristic(characteristic, data);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic readcharacteristic, boolean enable) {
        if (mBleService != null) {
            mBleService.setCharacteristicNotification(readcharacteristic, enable);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        return mBleService.getSupportedGattServices();
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBleService.getBluetoothGatt();
    }
}
